/* jshint node:true */
'use strict';
var Q = require('q');

module.exports = function(model) {
  return {
    //This is run before every API request
    apiSetup: function(req, res, next) {
      next();
    },
    //This is the universal API reponse handler - it's run after every API request
    sendResponse: function(req,res,next) {
      var response = {
        success: res.error ? false : true,
        message: res.error || "Success",
        updated: res.time,
        data: res.result || {}
      };
      res.send(response);
    },
    login: function(req, res, next) {
      if (! req.body || ! req.body.uid) {
        res.error = 'Missing user id';
      } else {
        var uid = req.body.uid;
        res.result = {uid: uid, method: 'login'};
      }
      next();
    },
    logout: function(req, res, next) {
      res.result = {method: 'login'};
      next();
    },
    fetchInvitation: function(req, res, next) {
      res.result = {this: 'is an invitation'};
      next();
    },
    fetchInvitationList: function(req, res, next) {
      res.result = ['list', 'of', 'invitations'];
      next();
    },
    createInvitation: function(req, res, next) {
      res.result = {this: 'is a new invitation'};
      next();
    },
    updateInvitation: function(req, res, next) {
      res.result = {this: 'is an updated invitation'};
      next();
    },
    test: function(req, res, next) {
      res.result = {this: 'is some sample JSON'};
      next();
    },
  };
};
