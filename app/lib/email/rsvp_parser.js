'use strict';

var MailParser = require("mailparser").MailParser;

var EmailSender = require('./email_sender');

/**
 * Pares the subject line for the invitation id
 *
 * @param {String} subject
 * @returns {String}
 */
function parseInvitationId(subject){
    var matches = subject.match(/\(Ref:.*\)/);
    if(!matches && matches.length === 0){
        return '';
    }
    return matches[0].replace('(Ref:', '').replace(')', '');
}

/**
 * Get invitation details from email
 *
 * @param {String} message
 * @returns {bluebird} Promise
 */
module.exports = function parse(message){
    return new Promise(function(resolve){
        var mailParser = new MailParser();
        mailParser.on("end", function(mail){
            mailParser.removeAllListeners();
            var invitation_id = mail.headers[EmailSender.INVITATION_HEADER] || parseInvitationId(mail.subject);
            resolve({
                email: mail.from[0].address,
                body: mail.html ? mail.html : mail.text,
                timestamp: mail.date ? mail.date.getTime() : Date.parse(mail.headers.date),
                invitation_id: invitation_id
            });
        });
        mailParser.write(message);
        mailParser.end(null, null);
    });
};
