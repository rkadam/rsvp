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
  console.err(msg, err);
};

module.exports = function(app) {
  var env = app.get('env');
  var config = app.config;

  app.set('views', config.root + '/server/views');
  // app.engine('html', require('html').__express);
  // app.set('view engine', 'html');
  app.use(compression());

  if ('production' === env) {
    //app.use(favicon(path.join(config.root, 'public/assets/favicon.ico')));
    app.use(express.static(path.join(config.root, '../web/dist/')));
    app.set('appPath', config.root + '../../web/dist/');
  } else {
    app.use(require('connect-livereload')());
    app.use(express.static(path.join(config.root, '.tmp')));
    app.use(express.static(path.join(config.root, '../web')));
    app.set('appPath', 'static');
  }

  app.use(morgan());
  app.use(errorHandler({log: handleErrors})); // Error handler - has to be last
};
