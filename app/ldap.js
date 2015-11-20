/* jshint node:true */
'use strict';
var ldap = require('ldapjs');
var Q = require('q');
var config = require('./config');

var departments = {};

var search = function(base, opts) {
  var client = ldap.createClient({
    url: 'ldaps://guess.savagebeast.com:636',
    // url: 'ldaps://ldap.savagebeast.com:636',
    // timeout : 10000,
    timeout : 2000,
    maxConnections : 100,
    reconnect: true,
  });
  client.on('error', function(err) {
    console.log('LDAP client error', err);
  });
  var ldapBind = Q.nbind(client.bind, client);
  var ldapSearch = Q.nbind(client.search, client);

  return ldapBind('', '')
    .then(function(res) {
      return ldapSearch(base, opts);
    }).then(function(res) {
      var data = null;
      var deferred = Q.defer();
      res.on('searchEntry', function(res) {
        if(res && res.object) {
          data = res.object;
        }
      });
      res.on('error', function(err) {
        deferred.reject(err);
      });
      res.on('end', function(result) {
        client.unbind(function(err) {
          if (err) {
            deferred.reject(err);
          } else {
            deferred.resolve(data);
          }
          client.destroy();
        });
      });
      return deferred.promise;
    }).fail(function(err) {
      console.log('LDAP search error', err);
    });
};

var userSearch = function(uid) {
  var opts = {
    filter: "(|(uid="+uid+")(RFC822MailMember="+uid+"))",
    attributes: ['uid', 'pandoraStartDate', 'ou', 'displayName'],
    scope: 'sub'
  };
  var base = 'dc=savagebeast,dc=com';
  return search(base, opts).then(function(res) {
    if (res) {
      return {
        uid: res.uid,
        name: res.displayName,
        years: ((Date.now() - Date.parse(res.pandoraStartDate)) / (1000 * 60 * 60 * 24 * 365)).toPrecision(3),
        department: departments[res.ou],
      };
    } else {
      return Q.reject("uid "+uid+" not found");
    }

  }).fail(function(err) {
    console.log('error!', err);
  });
};

var updateDepartments = function() {
  var opts = {
    scope: 'sub',
    filter: '(cn=departments)'
  };
  var base = 'ou=sbldap,dc=savagebeast,dc=com';
  return search(base, opts).then(function(res) {
    if (res && res.pandoraListMember) {
      res.pandoraListMember.forEach(function(d) {
        var parts = d.split(',');
        if (parts[0] && parts[1]) {
          departments[parts[1]] = parts[0];
        }
      });
    }
  }).then(function() {
    return Q.delay(config.department_refresh_seconds*1000)
      .then(function() {
        updateDepartments().done();
      });
  });
};
updateDepartments().done();

// userSearch('gmichalec')
//   .then(function(res) {
//     console.log(res);
//   }).done();

module.exports = {
  userSearch: userSearch,
};
