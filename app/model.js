/* jshint node:true */
'use strict';

var config = require('../common/config');
var DashboardDatastore = require('../common/dashboard_datastore.js');
var Sifter = require('sifter');
var VMs = require('../worker/vms');
var Q = require('q');
var log = require('../common/logging');

var model = {
  DashboardData: {
    groupErrors: {},
    vmData: {},
  },
  loaded: false
};

var sifters = {
  VmSifter: '',
  VmSifterData: [],
  GroupSifter: '',
  GroupSifterData: [],
};

model.loadData = function(noDelay) {
  var groupCount = 0;
  var delay = noDelay ? 0 : config.server.update_seconds*1000;

  var promises = DashboardDatastore.dataTypes.map(function(type) {
    if (!model.DashboardData[type]) {
      model.DashboardData[type] = {};
    }
    if (type == 'environments') {
      return DashboardDatastore.getItems(type)
        .then(function(environments) {
          var environmentsList = [];
          Object.keys(environments).forEach(function(environment_name) {
            var environment = environments[environment_name];
            groupCount += Object.keys(environment.groups).length+1;
            environment.groups.allgroups = {
              vms: environment.vms,
            };
            environmentsList.push(environment);
          });
          return [type, environmentsList];
        });
    } else {
      return DashboardDatastore.getItemNames(type)
        .then(function(names) {
          if (type === 'groups') {
            names.push('allgroups');
          }
          return [type, names];
        });
    }
  });

  return Q.all(promises)
    .then(function(results) {
      var delayIncrement = delay/groupCount;
      var x = 0;
      var promises = [];

      results.forEach(function(result) {
        var type = result[0];
        var names = result[1];

        names.forEach(function(name) {
          if (type == 'environments') {
            var environment = name;
            var environment_name = environment.environment_name;
            if (! model.DashboardData.groupErrors[environment_name]) {
              model.DashboardData.groupErrors[environment_name] = {};
            }
            if (! model.DashboardData.environments[environment_name]) {
              model.DashboardData.environments[environment_name] = environment;
            } else {
              model.DashboardData.environments[environment_name].vms = environment.vms;
            }
            Object.keys(model.DashboardData.environments[environment_name].groups).forEach(function(group_name) {
              if (Object.keys(environment.groups).indexOf(group_name) < 0) {
                log.warn("removing group "+group_name);
                delete model.DashboardData.environments[environment_name].groups[group_name];
                delete model.DashboardData.groupErrors[environment_name][group_name];
              }
            });
            Object.keys(environment.groups).forEach(function(group_name) {
              var group = environment.groups[group_name];
              var delay = delayIncrement * x++;
              var p = Q.delay(delay)
                .then(function() {
                  return loadGroupData(environment_name, group_name, group);
                })
                .then(function(results) {
                  if (results[0] && results[1]) {
                    model.DashboardData.environments[environment_name].groups[group_name] = results[0];
                    model.DashboardData.groupErrors[environment_name][group_name] = results[1];
                  } else {
                    log.error('Error loading group data for '+environment_name+':'+group_name, results);
                    delete model.DashboardData.environments[environment_name].groups[group_name];
                    delete model.DashboardData.groupErrors[environment_name][group_name];
                  }
                });
              promises.push(p);
            });
          } else {
            model.DashboardData[type][name] = {};
            if (type == 'vms' && ! model.DashboardData.vmData[name]) {
              model.DashboardData.vmData[name] = {
                vm_name: name,
                data: {}
              };
            }
          }
        });
      });

      return Q.delay(delay)
        .then(function() {
          return Q.allSettled(promises);
        });
    });
};

var loadGroupData = function(environment, group_name, group) {
  return Q.all([
      DashboardDatastore.getGroupStatus(environment, [group_name]),
      DashboardDatastore.getGroupVMStatus(environment, group_name),
      DashboardDatastore.getGroupErrors(environment, group_name),
    ])
    .spread(function(status, vm_status, errors) {
      var errorInfo;
      errorInfo = updateGroupErrors(group, errors);
      group = updateGroupStatus(group, status[0][1], vm_status);
      return [group, errorInfo];
    })
    .fail(function(e) {
      log.error('Error loading group data for !!!'+environment+':'+group_name, e.stack);
    });
};

var updateGroupStatus = function(group, status, vm_status) {
  var statusInfo = [0, 0, 0, 0];
  statusInfo.forEach(function(item, index) {
    statusInfo[index] = status[index] || 0;
  });
  statusInfo[0] += group.vms.length - status.total;
  group.status = statusInfo;
  group.vm_status = vm_status;
  return group;
};

var updateGroupErrors = function(group, errors) {
  var errorInfo = {
    group: group.group_name,
    label: group.label,
    counts: {},
    errors: [],
    total: 0,
    vm_error_count: 0,
    vm_total: group && group.vms ? Object.keys(group.vms).length : 0
  };

  if (errors.errors) {
    errorInfo.total = parseInt(errors.counts.total);
    errorInfo.vm_error_count = parseInt(errors.vm_count);
    var labels = {};
    var vmLists = {};
    Object.keys(errors.errors).forEach(function(key) {
      var type = key.split(':')[0];
      if (! vmLists[type]) {
        vmLists[type] = [];
      }
      vmLists[type].push(errors.errors[key].vm_name);
      errors.errors[key].key = errors.errors[key].vm_name + errors.errors[key].label;
      errorInfo.errors.push(errors.errors[key]);
      labels[errors.errors[key].value_name] = errors.errors[key].label;

    });
    Object.keys(errors.counts).forEach(function(key) {
      var count = {
        value_name: key,
        count: parseInt(errors.counts[key]),
        label: labels[key] || key,
        vms: vmLists[key]
      };
      errorInfo.counts[key] = count;
    });
  }
  return errorInfo;
};

var updateSifterIndex = function() {
  var newGroupIndex = [];
  var newVmIndex = [];
  Object.keys(model.DashboardData.environments).forEach(function(environment_name) {
    var environment = model.DashboardData.environments[environment_name];
    Object.keys(environment.groups).forEach(function(group_name) {
      if (group_name == 'allgroups') { return; }
      var group = environment.groups[group_name];
      var data = {
        title: group.label,
        id: group.group_name,
        desc: environment_name + ' Group',
        env: environment_name,
        group: group_name,
        type: 'group',
      };
      newGroupIndex.push(data);
      group.vms.forEach(function(vm_name) {
        var data = {
          title: vm_name,
          id: vm_name,
          desc: 'VM ('+(group.label || group_name)+')',
          env: environment_name,
          group: group_name,
          type: 'vm',
        };
        newVmIndex.push(data);
      });
    });
  });

  sifters.GroupSifterData = newGroupIndex;
  sifters.GroupSifter = new Sifter(newGroupIndex);

  sifters.VmSifterData = newVmIndex;
  sifters.VmSifter = new Sifter(newVmIndex);
};

model.search = function(string, environment) {
  var searchResults = [];
  var results = [];
  ['Group', 'Vm'].forEach(function(type) {
    var typeResults = sifters[type+'Sifter'].search(string, {fields: ['title']}).items;
    if (typeResults.length) {
      results = results.concat(
        typeResults.map(function(result) {
            return sifters[type+'SifterData'][result.id];
          })
          .filter(function(item) {
            return type != 'Group' || item.env == environment;
          })
        );
    }
  });

  searchResults = results.slice(0, 10);
  return searchResults;
};

model.fetchVM = function(vm_name, since, limit) {
  var vm;
  return DashboardDatastore.getItem('vms', vm_name)
    .then(function(res) {
      vm = res;
      return DashboardDatastore.getDataItems('vms', vm_name, since, limit);
    })
    .then(function(res) {
      if (vm) {
        var data = res.map(function(dataItem) {
          return VMs.formatData(vm.vm_class, vm.host_name, dataItem);
        });
        return [vm, data];
      }
    });
};

model.fetchEnvironments = function() {
  return Object.keys(model.DashboardData.environments);
};

model.fetchEnvironment = function(environment_name) {
  return model.DashboardData.environments[environment_name];
};

model.fetchGroups = function(environment_name) {
  var environment = model.fetchEnvironment(environment_name);
  var groups = [];
  if (environment) {
    groups = Object.keys(environment.groups);
  }
  return groups;
};

model.fetchGroup = function(group_name, environment_name) {
  var environment = model.fetchEnvironment(environment_name);
  if (environment) {
    return environment.groups[group_name];
  }
};

model.fetchGroupData = function(environment_name, since, limit, fields, groups) {
  groups = groups.length ? groups : ['none'];
  return DashboardDatastore.getGroupVMDataHistory(environment_name, since, limit, fields, groups);
};

model.fetchHealthHistory = function(vm_name) {
  return DashboardDatastore.getHealthHistoryItems(vm_name);
};

model.fetchGroupErrors = function(environment_name, group_name) {
  return model.DashboardData.groupErrors[environment_name][group_name];
};

model.fetchStatusHistory = function(environment_name, groups, since_seconds) {
  return DashboardDatastore.getGroupHistory('status', environment_name, since_seconds)
    .then(function(results) {
      var aggregates = {};
      var data = {};
      var statuses = ['unknown', 'good', 'warn', 'bad'];

      groups.concat(['totals']).forEach(function(group) {
        if (group == 'allgroups') { return; }
        data[group] = [];
        statuses.forEach(function(key, status) {
          data[group][status] = {
            key: key,
            values: []
          };
        });
      });

      results.forEach(function(history) {
        var totals = [0,0,0,0];
        var total = 0;
        groups.forEach(function(group) {
          if (history[1][group]) {
            total += history[1][group].total;
            statuses.forEach(function(key, status) {
              data[group][status].values.push({
                time: history[0],
                value: history[1][group][status] / history[1][group].total *100
              });
            });
            totals.forEach(function(total, index){
              totals[index] += history[1][group][index] || 0;
            });
          }
        });
        if (total) {
          totals.forEach(function(value, status) {
            data.totals[status].values.push({
              time: history[0],
              value: value/ total * 100
            });
          });
        }
      });
      return data;
    });
};

model.fetchErrorHistory = function(environment_name, groups, since_seconds) {
  return DashboardDatastore.getGroupHistory('error', environment_name, since_seconds)
    .then(function(results) {
      var data = {
        totals: []
      };
      results.forEach(function(history) {
        var total = 0;
        groups.forEach(function(group) {
          if (! data[group]) {
            data[group] = [];
          }
          data[group].push([history[0], history[1][group]]);
          total += history[1][group] || 0;
        });
        data.totals.push([history[0], total]);
      });
      return data;
    });
};

model.updateData = function(noDelay) {
  // log.time('Load Data');
  model.loadData(noDelay)
    .then(function() {
      return updateSifterIndex();
    })
    .then(function(res) {
      // log.timeEnd('Load Data');
      model.loaded = true;
      model.updateData();
    })
    .fail(function(e) {
      log.error('error updating model: ', e.stack);
    })
    .done();
};

model.updateVms = function() {
  var delay = config.server.vm_update_seconds*1000;
  return DashboardDatastore.getItemNames('vms')
    .then(function(names) {
      delay = delay / names.length;
      var promises = names.map(function(vm, index) {
        return Q.delay(delay*index).then(function() {
          return model.fetchVM(vm, null, 1);
        }).then(function(res) {
          if (res[0] && res[1] && res[1][0]) {
            var vmData = res[0];
            vmData.data = {};
            Object.keys(res[1][0]).forEach(function(checkName) {
              var valueObj = res[1][0][checkName];
              var value = vmData.data[checkName] = {};
              if (typeof valueObj.value === 'undefined') {
                value.value = valueObj;
                value.label = checkName;
              } else {
                value.value = valueObj.value.str;
                value.isOk = valueObj.value.isOk;
                value.magnitude = valueObj.value.magnitude;
                value.url = valueObj.url;
                value.isVital = valueObj.isVital;
                value.label = valueObj.label;
              }
              if (value.value === '' || value.value === null) {
                value.value = '?';
              }
              value.isNumber = ! isNaN(value.value);
            });
            model.DashboardData.vmData[vm] = vmData;
          }
        });
      });
      return Q.all(promises).then(function() {
        model.updateVms();
      });
    });
};
module.exports = model;