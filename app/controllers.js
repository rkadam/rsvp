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
        success: req.error ? false : true,
        message: req.error || "Success",
        updated: req.time,
        data: req.data || {}
      };
      res.send(response);
    },
    test: function(req, res, next) {
      req.data = {this: 'is some sample JSON'};
      next();
    },
  };
};
