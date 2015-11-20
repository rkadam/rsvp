/**
 * Main application file
 */

'use strict';

// Set default node environment to development
process.env.NODE_ENV = process.env.NODE_ENV || 'development';
global.app_name = 'server';

var express = require('express');
var config = require('./config');
var model = require('./model');

var emailPoll = require('./lib/email/new-invitation-response-poll');
var POLL_INTERVAL_MILLIS = 15000;

// Setup server
var app = express();
app.config = config;
app.model = model;

var server = require('http').createServer(app);
require('./express_config')(app);
require('./routes')(app);

// Start server
server.listen(config.port, config.ip, function () {
  console.log('Express server listening on %d, in %s mode', config.port, app.get('env'));
});

if (config.poll_email) {
  //poll for invitation replies
  emailPoll.startPoll(model, config.poll_email_interval_millis);
}

// Expose app
exports = module.exports = app;
