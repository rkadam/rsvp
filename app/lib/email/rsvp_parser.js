var MailParser = require("mailparser").MailParser;

var EmailSender = require('./email_sender');

module.exports = function parse(message){
    return new Promise(function(resolve){
        var mailParser = new MailParser();
        mailParser.on("end", function(mail){
            mailParser.removeAllListeners();
            resolve({
                email: mail.from[0].address,
                body: mail.html ? mail.html : mail.text,
                timestamp: mail.date ? mail.date.getTime() : Date.parse(mail.headers.date),
                invitation_id: mail.headers[EmailSender.INVITATION_HEADER]
            });
        });
        mailParser.write(message);
        mailParser.end(null, null);
    });
};
