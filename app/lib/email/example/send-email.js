'use strict';

var fs = require('fs');

var EmailSender = require('../email_sender');

var configuration = require('../email_configuration').getConfig();

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