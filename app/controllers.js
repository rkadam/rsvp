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
    apiInfo: function(req, res, next) {
      res.result = [
        {
          url: 'http://'+req.header('host')+'/api/login',
          method: 'POST',
          description: "authenticate user with {uid: 'uid', password: 'password'}",
          returns: 'success/failure object'
        }, {
          url: 'http://'+req.header('host')+'/api/logout',
          method: 'GET',
          description: "logout current user",
          returns: 'success/failure object'
        }, {
          url: 'http://'+req.header('host')+'/api/users/:uid/invitations',
          method: 'GET',
          description: "return a list of invitation objects belonging to :uid",
          returns: 'list or invitation objects'
        }, {
          url: 'http://'+req.header('host')+'/api/users/:uid/invitations',
          method: 'POST',
          post_body: {
            title: 'Title of invitation',
            response_accept_limit: '# of responses that will be accpeted (integer)',
            rsvp_by_time: 'Time event will take place (integer - unix time in ms)',
            email_to: 'list of email addresses that offer will be sent to (string)',
            method: 'method of invitation selection: one of ["random", "firstcomefirstserve"]',
            invitation_body: 'body of email that will be sent',
          },
          description: "create a new invitation owned by :uid",
          returns: 'invitation object'
        }, {
          url: 'http://'+req.header('host')+'/api/users/:uid/invitations/:invitation_id',
          method: 'GET',
          description: "return an invitation object",
          returns: 'invitation object'
        }, {
          url: 'http://'+req.header('host')+'/api/users/:uid/invitations/:invitation_id/selectWinners',
          method: 'GET',
          description: "calls response selection method",
          returns: 'updated invitation object (with response objects populated with selected property (boolean)'
        }, {
          url: 'http://'+req.header('host')+'/api/users/:uid/invitations/:invitation_id/closeInvitation',
          method: 'POST',
          post_body: {
            accepted_body: 'Body of email to send to accepected responses',
            rejected_body: 'Body of email to send to rejected responses',
          },
          description: "send inviation",
          returns: 'updated invitation object',
        }, {
          url: 'http://'+req.header('host')+'/api/users/:uid/invitations/:invitation_id',
          method: 'PUT',
          description: "update an invitation object",
          returns: 'invitation object'
        },
      ];
      next();
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
      //FIXME - check req.params.uid
      model.fetchInvitation(req.params.invitation_id).then(function(invitation) {
        res.result = invitation;
        next();
      }).fail(function(err) {
        res.error = err;
        next();
      });
    },
    fetchInvitationList: function(req, res, next) {
      model.fetchInvitationList(req.params.uid).then(function(invitations) {
        res.result = invitations;
        next();
      }).fail(function(err) {
        res.error = err;
        next();
      });
    },
    createInvitation: function(req, res, next) {
      //FIXME - check req.params.uid
      var invite = res.body;
      model.createInvitation(req.params.uid, req.body).then(function(r) {
        res.result = r;
        next();
      }).fail(function(err) {
        res.error = err;
        next();
      });
    },
    updateInvitation: function(req, res, next) {
      model.updateInvitation(req.body).then(function(r) {
        res.result = r;
        next();
      }).fail(function(err) {
        res.error = err;
        next();
      });
    },
    selectWinners: function(req, res, next) {
      //FIXME - check req.params.uid
      model.selectWinners(req.params.invitation_id).then(function(r) {
        res.result = r;
        next();
      }).fail(function(err) {
        res.error = err;
        next();
      });
    },
    closeInvitation: function(req, res, next) {
      //FIXME - check req.params.uid
      model.closeInvitation(req.params.invitation_id, req.body).then(function(r) {
        res.result = r;
        next();
      }).fail(function(err) {
        res.error = err;
        next();
      });
    },
    test: function(req, res, next) {
      res.result = {this: 'is some sample JSON'};
      next();
    },
  };
};
