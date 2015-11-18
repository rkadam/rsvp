/* jshint node:true */
'use strict';

var config = require('./config');
// var redis = require('redis');

var model = {
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
      reponded: time,
      response_body: 'Pleeease let me come to lunch with you',
      selected: false,
    };
    return response;
  }
};
module.exports = model;