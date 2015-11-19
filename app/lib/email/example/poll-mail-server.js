'use strict';

var inspect = require('util').inspect;
var fs = require('fs');

var extend = require('util-extend');

var EmailReader = require('../email_reader.js');
var parser = require('../rsvp_parser');

var Promise = require("bluebird");

Promise.config({
    longStackTraces: true,
    warnings: true
});

var configuration = JSON.parse(fs.readFileSync('../configuration.json', 'utf8'));

var client = new EmailReader(extend(configuration, {
    //debug: function(){
    //    console.log("Debug: " + inspect(arguments));
    //}
}));

client.connect()
    .then(client.getNewMessages.bind(client))
    .then(function(messages){
        //console.log('Parsed messages: %s', inspect(messages));
        return Promise.map(messages, function(message){
            return parser(message);
        });
    })
    .then(function(){
        console.log('about to disconnect: %s', inspect(arguments));
        client.disconnect();
    })
    .catch(function(error){
        throw new Error("Unable to retrieve & parse emails: " + JSON.stringify(error));
    });