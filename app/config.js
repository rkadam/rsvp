/* jshint node: true*/
"use strict";
var extend = require('util-extend');
var fs = require('fs');

var rootDir = __dirname;

var config = {
  port: process.env.PORT || 9000,
  env: process.env.NODE_ENV,
  root: rootDir,
  ip: '127.0.0.1',
  redis_db_index: 0,
  department_refresh_seconds: 10,
  poll_email_interval_millis: 15000,
  always_respond_to: 'gmichalec@pandora.com'
};

if (config.env !== 'production') {

}

if (fs.existsSync('./config.local.json')) {
  var local_config = JSON.parse(fs.readFileSync('config.local.json', 'utf8'));
  config = extend(config, local_config);
}

module.exports = config;
