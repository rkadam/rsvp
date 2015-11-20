/* jshint node:true */
'use strict';

var fs = require('fs');
var path = require('path');

var config = require('./config');
var redis = require('redis');
var ldap = require('./ldap');
var Q = require('q');

var EmailSender = require('./lib/email/email_sender');
var configuration = JSON.parse(fs.readFileSync(path.resolve(__dirname, 'lib/email/configuration.json'), 'utf8'));

var inspect = require('util').inspect;

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

var sendInvitation = function(invitation) {
  var deferred = new Q.defer();

  if (!invitation.title
      || !invitation.invitation_body
      || !invitation.id
      || !invitation.email_to) {
    console.warn("Incomplete data. Unable to send email.");
    deferred.resolve('boo');
    return deferred.promise;
  }

  var client = new EmailSender(configuration);
  client.connect()
      .then(function(){
        client.sendMessage(
            "rsvp@pandora.com",
            invitation.title,
            invitation.invitation_body,
            invitation.id,
            invitation.email_to
        ).then(function(){
          client.disconnect();
          deferred.resolve('yay');
        });
        return null;
      });

  return deferred.promise;
};

var sendResponses = function(invitation) {
  var deferred = new Q.defer();
  var client = new EmailSender(configuration);
  client.connect()
      .then(function(){
        invitation.responses.forEach(function(response){
          var message = response.selected ? invitation.accepted_body : invitation.rejected_body;
          client.sendMessage(
              "rsvp@pandora.com",
              invitation.title,
              message,
              invitation.id,
              response.uid + "@pandora.com"
          )
        });
        client.disconnect();
        deferred.resolve('yay');
      });
  return deferred.promise;
};

var model = {
  createInvitation: function(uid, invitation) {
    var time = Date.now();
    var id = uid + '-' + time;
    invitation.uid = uid;
    invitation.id = id;
    invitation.create_time = time;
    invitation.sent_time = null;
    invitation.accepted_body = null;
    invitation.rejected_body = null;
    invitation.active = true;
    invitation.responses = [];
    return sendInvitation(invitation)
      .then(function(res) {
        return redisMulti([
          ['sadd', [uid, id]],
          ['set', [id, JSON.stringify(invitation)]],
        ]);
      }).then(function() {
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
    var redisCommands = [
     ['del', [[invitation.id, 'responses']]]
    ];

    var responseUpdates = invitation.responses.forEach(function(response) {
      redisCommands.push(['sadd', [[invitation.id, 'responses'], JSON.stringify(response)]]);
    });
    invitation.responses = [];
    redisCommands.push(['set', [invitation.id, JSON.stringify(invitation)]]);
    return redisMulti(redisCommands).then(function() {
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
  selectWinners: function(invitation_id) {
    var sortMethods = {
      random: function(a, b) {
        return 0.5 - Math.random();
      },
      firstcomefirstserve: function(a, b) {
        return a.response_time > b.response_time ? 1 : -1;
      }
    };
    return model.fetchInvitation(invitation_id)
      .then(function(invitation) {
        var sortMethod = sortMethods[invitation.method];
        if (! sortMethod) {
          return Q.reject("unknown selection method: "+invitation.method);
        } else {
          var limit = invitation.response_accept_limit;
          var selected_count = 0;
          invitation.responses.sort(sortMethod).forEach(function(response) {
            if (limit > selected_count) {
              selected_count++;
              response.selected = true;
            } else {
              response.selected = false;
            }
          });
          return model.updateInvitation(invitation);
        }
      });
  },
  closeInvitation: function(invitation_id, data) {
    var invitation;
    return model.fetchInvitation(invitation_id)
      .then(function(res) {
        invitation = res;
        invitation.active = false;
        invitation.sent_time = Date.now();
        invitation.accepted_body = data.accepted_body;
        invitation.rejected_body = data.rejected_body;
        return sendResponses(invitation);
      }).then(function() {
        return model.updateInvitation(invitation);
      });
  },
  createResponse: function(response_data) {
    var time = Date.now();
    var parts = response_data.email_address.split('@');
    var uid = parts[0];
    if(parts[1] !== 'pandora.com'){
      return Q.reject("Invalid email: " + response_data.email_address);
    }
    var response = {};
    return ldap.userSearch(uid)
      .then(function(userObject) {
        if (! userObject || ! userObject.uid) {
          return Q.reject("Invalid uid: "+uid);
        }
        response = {
          uid: userObject.uid,
          invitation_id: response_data.invitation_id,
          name: userObject.name,
          years: userObject.years,
          department: userObject.department,
          response_time: response_data.response_time,
          response_body: response_data.response_body,
          selected: false,
          image_url: 'https://ray.savagebeast.com/sbldap/image.cgi?uid='+uid
        };

      // return redisMulti([
      //   ['sadd', [response_data.invitation_id+':responses', uid]],
      //   ['set', [[response_data.invitation_id, 'responses'. uid], JSON.stringify(response)]],
      // ]);
      return redisCommand('sadd', [response_data.invitation_id+':responses', JSON.stringify(response)]);
    }).then(function(res) {
      return response;
    });
  },
  // fetchResponse: function(invitation_id, uid) {
  //   return
  // },
  // updateResponse: function(response) {
  //   return redisCommand('set', [[response.invitation_id, 'responses', response.uid], JSON.stringify(response)])
  //     .then(function() {
  //       return model.fetchResponse(response.invitation_id, response.uid);
  //     });
  // },
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
    return redisCommand('quit');
  },
  flush: function() {
    return redisCommand('flushdb');
  }
};
redisCommand('select', [config.redis_db_index]);

module.exports = model;