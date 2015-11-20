'use strict';

var inspect = require('util').inspect;

var Imap = require('imap');
var Promise = require("bluebird");

function logMessage(msg){
    //console.log("*************> " + msg);
}

/**
 * Email client, retrieves all new messages.
 * @param {Object} config
 * @constructor
 */
function EmailReader(config){
    this._config = config;
    this._imap = null;
    this._connected = false;
}

/**
 * Connects to the email server
 * @returns {bluebird} Promise
 */
EmailReader.prototype.connect = function(){
    return new Promise(function(resolve, reject){
        if(this._imap){
            reject("Connection already open");
            return null;
        }
        this._imap = new Imap(this._config);

        this._imap.once('ready', function(){
            this._imap.openBox('INBOX', false, function(){
                this._connected = true;
                resolve(this);
            }.bind(this));
        }.bind(this));

        this._imap.once('error', function(err) {
            console.error(err);
            reject(err);
        });

        this._imap.connect();
    }.bind(this));
};

/**
 * Retrieve the latest unseen messages from mailbox
 * @returns {bluebird} Promise
 */
EmailReader.prototype.getNewMessages = function(){
    if(this._gettingNewMessages){
        return this._gettingNewMessages;
    }

    this._gettingNewMessages = new Promise(function(resolve, reject){
        if(!this._imap || !this._connected){
            reject("Connection NOT open");
            return null;
        }

        this._imap.search(['UNSEEN'], function(err, results) {
            logMessage("===> got unseen messages: " + inspect(results));
            if(err){
                this._gettingNewMessages = null;
                reject(err);
                return null;
            }

            if(!results || results.length <= 0){
                logMessage("resolving with nothing");
                this._gettingNewMessages = null;
                resolve([]);
                return null;
            }

            var messages = [];
            var fetchOp = this._imap.fetch(results, {
                markSeen: true,
                bodies: ''
            });
            fetchOp.on('message', function (msg, seqno) {
                logMessage('Message #%d', seqno);
                var prefix = '(#' + seqno + ') ';
                msg.on('body', function(stream, info){
                    logMessage(prefix + 'Body');
                    var buffer = '';
                    stream.on('data', function(chunk) {
                        buffer += chunk.toString('utf8');
                    });
                    stream.once('end', function() {
                        logMessage(prefix + 'Parsed header: %s', inspect(Imap.parseHeader(buffer)));
                        messages.push(buffer);
                    });
                }.bind(this));

                msg.once('attributes', function(attrs) {
                    logMessage(prefix + 'Attributes: %s', inspect(attrs, false, 8));
                });

                msg.on('end', function(stream, info){
                    logMessage(prefix + 'End');
                    msg.removeAllListeners();
                }.bind(this));
            }.bind(this));

            fetchOp.on('error', function (err) {
                this._gettingNewMessages = null;
                reject(err);
            }.bind(this));

            fetchOp.on('end', function(){
                fetchOp.removeAllListeners();
                this._gettingNewMessages = null;
                resolve(messages);
            }.bind(this));
        }.bind(this));
    }.bind(this));

    return this._gettingNewMessages;
};

/**
 * Disconnects from the email server
 */
EmailReader.prototype.disconnect = function(){
    /* close connection & trust that it will get closed */
    this._imap.end();
    this._imap.removeAllListeners();
    this._imap = null;
    this._connected = false;
    logMessage("Connection closed.");
};

module.exports = EmailReader;