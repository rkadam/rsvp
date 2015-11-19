/* jshint node:true */

/**
 * Main application routes
 */

'use strict';

var path = require('path');
// var errors = require('./components/errors');

module.exports = function(app) {
  var controllers = require('./controllers')(app.model);
  var send404 = function(req, res) {
    res.sendfile(app.get('appPath') + '/404.html');
  };

  // Insert routes below
  app.route('/api/*').get(controllers.apiSetup);
  app.route('/api').get(controllers.apiInfo);

  //login with {uid: 'uid', password: 'password'}
  app.route('/api/auth/login').post(controllers.login);

  //logout or app
  app.route('/api/auth/logout').get(controllers.logout);

  //return a list of invitation objects belonging to :uid
  app.route('/api/users/:uid/invitations').get(controllers.fetchInvitationList);

  //create a new invitation object belonging to :uid
  app.route('/api/users/:uid/invitations').post(controllers.createInvitation);

  //create a new invitation object belonging to :uid
  app.route('/api/users/:uid/invitations/:iid').get(controllers.fetchInvitation);

  //update an invitation object belonging to :uid
  app.route('/api/users/:uid/invitations/:iid').put(controllers.updateInvitation);

  app.route('/api/*').all(controllers.sendResponse);

  // All undefined asset or api routes should return a 404
  app.route('/:url(api|auth|components|app|bower_components|assets)/*')
   .get(send404);
  // All other routes should redirect to the index.html
  app.route('/*')
    .get(function(req, res) {
      var indexFilepath = path.resolve(path.join(app.get('appPath'), 'index.html'));
      res.sendfile(indexFilepath);
    });
};
