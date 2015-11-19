'use strict';

var fs = require('fs');
var path = require('path');
var inspect = require('util').inspect;

var extend = require('util-extend');

var EmailReader = require('./email_reader.js');
var parser = require('./rsvp_parser');

var Promise = require("bluebird");

Promise.config({
    longStackTraces: true,
    warnings: true
});

var configuration = JSON.parse(fs.readFileSync(path.resolve(__dirname, 'configuration.json'), 'utf8'));

var client = new EmailReader(extend(configuration, {
    //debug: function(){
    //    console.log("Debug: " + inspect(arguments));
    //}
}));

/**
 * Saves acceptances to storage.
 *
 * @param {model} storage
 * @param {String[]} emails
 * @returns {bluebird} Promise
 */
function saveAcceptedInvites(storage, emails){
    //console.log("******> about to save accepted emails: " + inspect(emails));
    return Promise.map(emails, function(email){
        return Promise.resolve(storage.createResponse(
            email.invitation_id,
            email.email,
            email.timestamp,
            email.body
        ));
    });
}

/**
 * Gets emails and saves them
 * @returns {bluebird|exports|module.exports}
 */
function getEmail(){
    return client.connect()
        .then(client.getNewMessages.bind(client))
        .then(function(messages){
            client.disconnect();
            return Promise.map(messages, function(message){
                return parser(message);
            });
        })
        .then(function(messages){
            return messages;
        })
        .catch(function(error){
            throw new Error("Unable to retrieve & parse emails: " + JSON.stringify(error));
        });
}

var polling = false;
var timeoutHandle = null;

/**
 * Starts polling for new emails.
 * @param {model} storage
 * @param {Number} interval
 */
function startPoll(storage, interval){
    if(polling){
        console.warn("Poll is already configured");
        return;
    }
    poll(storage, interval);
}

/**
 * Poll for new emails on the specified interval
 * @param {model} storage - API for saving data
 * @param {Number} interval - poll for new email every @interval millis
 */
var called = 0;
function poll(storage, interval){
    called++;
    if(called >= 10){
        return;
    }
    polling = true;

    getEmail()
        .then(saveAcceptedInvites.bind(null, storage))
        .then(function(){
            timeoutHandle = setTimeout(function(){
                poll(storage, interval);
            }, interval);
        });
}

/**
 * Cancels polling
 */
function cancelPoll(){
    clearInterval(timeoutHandle);
    timeoutHandle = null;
}

/**
 * Check whether we are polling for new emails.
 * @returns {boolean}
 */
function isPolling(){
    return timeoutHandle !== null;
}

module.exports = {
    startPoll: startPoll,
    cancelPoll: cancelPoll,
    isPolling: isPolling
};