/* jshint node: true*/
"use strict";

var rootDir = __dirname;

var config = {
  port: process.env.PORT || 9000,
  env: process.env.NODE_ENV,
  root: rootDir,
  ip: '127.0.0.1',
  redis_db_index: 0,
};

if (config.env !== 'production') {

}

module.exports = config;
