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
  image_server:'http://images.server.com/?uid=',
  email: {
    user: "smtp user",
    password: "password",
    host: "smtp.server.com",
    port: 993,
    tls: true,
    always_respond_to: 'email@server.com',
    from: 'from@server.com',
    test_email: 'test-list@server.com',
    email_domain: 'server.com'
  },
  ldap: {
    url: 'ldaps://ldap.server.com',
    timeout: 2000,
    base: 'dc=xx,dc=com',
    group_base: 'ou=xx,dc=xx,dc=com',
    start_date_field: 'startDate'
  }
};

if (config.env !== 'production') {

}

if (fs.existsSync('./config.local.json')) {
  var local_config = JSON.parse(fs.readFileSync('config.local.json', 'utf8'));
  config = extend(config, local_config);
}

module.exports = config;
