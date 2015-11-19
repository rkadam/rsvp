/* jshint node:true */
'use strict';

var config = require('./config');
var redis = require('redis');
var ldap = require('./ldap');
var Q = require('q');

var redisClient = redis.createClient();
redisClient.on("error", function (e) {
  console.log("Redis error",e);
});

var namespace = 'rsvp';

var redisCommand = function(command, args) {
  args = setRedisKey(command, args);
  return Q.npost(redisClient, command, args)
    .then(function(data) {
      if ((command == 'hgetall' || command == 'hmget') && data) {
        var parsed = {};
        Object.keys(data).forEach(function(key, index) {
          var outIndex = command == 'hgetall' ? key : args[index+1];
          parsed[outIndex] = parseValue(data[key]);
        });
        data = parsed;
      }
      return data;
    }, function(e) {
      console.error("REDIS ERROR", command, args, e);
      return Q.reject(e);
    });
};

var redisMulti = function(commands) {
  var multiCommands = commands.map(function(command) {
    command[1] = setRedisKey(command[0], command[1]);
    return [command[0]].concat(command[1]);
  });
  var multi = redisClient.multi(multiCommands);
  return Q.ninvoke(multi, 'exec')
    .then(function(data) {
      data.forEach(function(item, index) {
        if (commands[index][0] === 'hgetall' && item) {
          var parsed = {};
          Object.keys(item).forEach(function(key) {
            parsed[key] = parseValue(item[key]);
          });
          data[index] = parsed;
        }
      });
      // console.log('Multi results: ', data)
      return data;
    }, function(e) {
      console.error("REDIS MULTI ERROR", commands, e);
      return Q.reject(e);
    });
};

var setRedisKey = function(command, args) {
  if (args) {
    if(typeof args[0] === 'object') {
      args[0] = args[0].join(':');
    }
    if (['flush', 'select', 'quit'].indexOf(command) == -1) {
      args[0] = namespace+':'+args[0];
    }
  }
  return args;
};

var parseValue = function(value) {
  var parsed = value;
  if (parsed !== null) {
    try {
      parsed = JSON.parse(value);
    } catch (e) {
    }
  }
  return parsed;
};

var model = {
  createInvitation: function(uid, title, response_accept_limit, rsvp_by_time, email_to, method, invitation_body) {
    var time = Date.now();
    var id = uid + '-' + time;
    var invitation = {
      id: id,
      uid: uid,
      title: title,
      response_accept_limit: response_accept_limit,
      create_time: time,
      rsvp_by_time: rsvp_by_time,
      sent_time: null,
      email_to: email_to,
      method: method,
      invitation_body: invitation_body,
      accepted_body: null,
      rejected_body: null,
      active: true,
      responses: [],
    };
    return redisMulti([
      ['sadd', [uid, id]],
      ['set', [id, JSON.stringify(invitation)]],
    ]).then(function() {
      return invitation;
    });
  },
  fetchInvitation: function(invitation_id) {
    var invitation = {};
    return redisMulti([
      ['get', [invitation_id]],
      ['smembers', [invitation_id+':responses']]
    ]).then(function(res) {
      var invitation = JSON.parse(res[0]);
      invitation.selected_count = 0;
      if (res[1] && res[1].length) {
        invitation.responses = res[1].map(function(r) {
          var response = JSON.parse(r);
          if (response && response.selected) {
            invitation.selected_count++;
          }
          return response;
        });
      }
      return invitation;
    });
  },
  updateInvitation: function(invitation) {
    return redisCommand('set', [invitation.id, JSON.stringify(invitation)]).then(function(res) {
      return model.fetchInvitation(invitation.id);
    });
  },
  fetchInvitationList: function(uid) {
    return redisCommand('smembers', [uid])
      .then(function(res) {
        var promises = res.map(function(id) {
          return model.fetchInvitation(id);
        });
        return Q.all(promises);
      }).then(function(res) {
        return res;
      });
  },
  createResponse: function(invitation_id, email_address, response_time, response_body) {
    var time = Date.now();
    var uid = email_address.split('@')[0];
    var response = {};
    return ldap.userSearch(uid)
      .then(function(userObject) {
        if (! userObject || ! userObject.uid) {
          return Q.reject("Invalid uid: "+uid);
        }
        response = {
          uid: userObject.uid,
          invitation_id: invitation_id,
          name: userObject.name,
          years: userObject.years,
          department: userObject.department,
          response_time: response_time,
          response_body: response_body,
          selected: false
        };
      return redisCommand('sadd', [invitation_id+':responses', JSON.stringify(response)]);
    }).then(function(res) {
      return response;
    });
  },
  getSampleInvitation: function(uid, id, title_append, response_count) {
    uid = uid || 'raju';
    var time = Date.now();
    id = id || uid + ' - '+ time;
    var title = 'Test invite '+title_append;
    var invitation = {
      id: id,
      uid: uid,
      title: title,
      response_accept_limit: 9,
      create_time: time,
      rsvp_by_time: time + 60*60*1000,
      sent_time: null,
      email_to: 'dist-rsvp-test@pandora.com',
      method: 'random',
      invitation_body: "Come to the best lunch evar!",
      accepted_body: "You are a winner!",
      rejected_body: "You are a loser!",
      active: true,
      responses: [],
    };
    while (response_count--) {
      invitation.responses.push(model.getSampleResponse(invitation.id));
    }
    return invitation;
  },
  getSampleResponse: function(invitation_id) {
    var uid = Math.random().toString(26).substr(2, 7);
    var time = Date.now();
    var response = {
      uid: uid,
      invitation_id: invitation_id,
      name: 'Tester '+uid,
      years: 3,
      department: 'Technical Operations',
      response_time: time,
      response_body: 'Pleeease let me come to lunch with you',
      selected: false,
    };
    return response;
  },
  close: function() {
    ldap.close();
    return redisCommand('quit');
  },
  flush: function() {
    return redisCommand('flushdb');
  }
};

module.exports = model;