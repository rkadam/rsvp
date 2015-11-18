/* jshint node:true */
'use strict';

var config = require('./config');
var redis = require('redis');
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
    return true;
  },
  fetchInvitation: function(uid, invitation_id) {
    return {};
  },
  updateInvitation: function(invitation) {
    return true;
  },
  createResponse: function(email_address, invitation_id, response_time, response_body) {
    return true;
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
  }
};

module.exports = model;