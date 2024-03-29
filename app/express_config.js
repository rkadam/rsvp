/* jshint node:true */

/**
 * Express configuration
 */

'use strict';

var express = require('express');
var morgan = require('morgan');
var compression = require('compression');
var errorHandler = require('errorhandler');
var path = require('path');
var bodyParser = require('body-parser');

function jsonFormat(tokens, req, res) {
  return JSON.stringify({
    'remote-address': tokens['remote-addr'](req, res),
    'time': tokens.date(req, res, 'iso'),
    'method': tokens.method(req, res),
    'url': tokens.url(req, res),
    'http-version': tokens['http-version'](req, res),
    'status-code': tokens.status(req, res),
    'content-length': tokens.res(req, res, 'content-length'),
    'referrer': tokens.referrer(req, res),
    'user-agent': tokens['user-agent'](req, res),
  });
}

var handleErrors = function(err, str, req) {
  var msg = 'Error in ' + req.method + ' ' + req.url+': '+str;
  console.log(msg, err);
};

module.exports = function(app) {
  var env = app.get('env');
  var config = app.config;
  app.use(bodyParser.json());

  app.set('views', config.root + '/server/views');
  // app.engine('html', require('html').__express);
  // app.set('view engine', 'html');
  app.use(compression());

  if ('production' === env) {
    //app.use(favicon(path.join(config.root, 'public/assets/favicon.ico')));
    app.use(express.static(path.join(config.root, '../web/public/')));
    app.set('appPath', config.root + '../web/public/');
  } else {
    // app.use(require('connect-livereload')());
    app.use(express.static(path.join(config.root, '.tmp')));
    app.use(express.static(path.join(config.root, '../web/public')));
    app.set('appPath', '../web/public');
  }

  app.use(morgan('combined'));
  app.use(errorHandler({log: handleErrors})); // Error handler - has to be last
};
