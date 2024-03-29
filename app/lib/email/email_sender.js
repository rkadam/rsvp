'use strict';

var SMTPConnection = require('smtp-connection');
var Promise = require("bluebird");

var configuration = require('./email_configuration').getConfig();

function EmailSender(options){
    this._options = options;
    this._connection = false;
}

/**
 * Open connection to server
 */
EmailSender.prototype.connect = function(){
    return new Promise(function(resolve, reject){
        if(this._connection){
            reject("Connection already open");
            return null;
        }

        this._connection = new SMTPConnection({
            host: this._options.host,
            secure: true,
            debug: true
        });

        this._connection.on('error', function(err){
            reject(err);
        });

        this._connection.on('log', function(){
            console.log(arguments);
        });

        this._connection.on('connect', function(){
            this._connection.login({
                user: this._options.user,
                pass: this._options.password
            }, function(err){
                if(err){
                    reject(err);
                    return;
                }
                resolve();
            });
        }.bind(this));

        this._connection.connect();
    }.bind(this));
};

/**
 * Send invitation message to recipients.
 *
 * @param {String} from
 * @param {String} subject
 * @param {String} message
 * @param {String} invitation_id
 * @param {String} recipient
 */
EmailSender.prototype.sendMessage = function(from, subject, message, invitation_id, recipient){
    return new Promise(function(resolve, reject){
        if(!this._connection){
            reject("Connection NOT open");
            return null;
        }

        var envelope = {
            to: recipient,
            from: EmailSender.INVITATION_FROM
        };
        subject = subject + " (Ref:" + invitation_id + ")";
        message = [
            "Subject: ", subject, "\r\n",
            EmailSender.INVITATION_HEADER, ": ", invitation_id, "\r\n",
            "On-Behalf-Of: ", EmailSender.INVITATION_FROM, "\r\n",
            "Sender: ", from, "\r\n",
            "Reply-To: ", EmailSender.INVITATION_FROM, "\r\n",
            message,
            "\r\n"
        ].join('');
        this._connection.send(envelope, message, function(err, info){
            if(err){
                reject(err);
                return;
            }
            resolve();
        });

    }.bind(this));
};

/**
 * Close connection to server
 */
EmailSender.prototype.disconnect = function(){
    this._connection.quit();
    this._connection.removeAllListeners();
    this._connection = null;
};

EmailSender.INVITATION_HEADER = "X-Invitation-Id";
EmailSender.INVITATION_FROM = configuration.user + "@pandora.com";

module.exports = EmailSender;