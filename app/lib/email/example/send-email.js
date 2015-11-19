'use strict';

var fs = require('fs');

var EmailSender = require('../email_sender');

var configuration = JSON.parse(fs.readFileSync('../configuration.json', 'utf8'));

var client = new EmailSender(configuration);

client.connect()
    .then(function(){
        return client.sendMessage("someaddress@pandora.com", "test invitation subject", "test message", "invitation_id", "ivo@pandora.com");
    })
    .then(function(){
        client.disconnect();
    })
    .catch(function(error){
        throw new Error("Unable to retrieve & parse emails: " + JSON.stringify(error));
    });