var model = require('./model');
var Q = require('q');
var config = require('./config'):

var users = [];
var response_users = [];
var titles = ['Join me for lunch!', 'Cousin! Let\'s go bowling!', 'BASE jump from 2100 Franklin!'];

var randomBetween = function(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
};
var total = users.length * titles.length * users.length;
var completed = 0;
model.flush().then(function() {
  Q.all(users.map(function(uid) {
    return Q.all(titles.map(function(title, index) {
      return Q.delay(100 * index)
        .then(function() {
          // var limit = randomBetween(5, 10);
          var limit = 3;
          var rsvp_time = Date.now() + (randomBetween(0, 10) * 60 * 60 * 1000);
          return model.createInvitation(uid, {
              title: title,
              response_accept_limit: limit,
              rsvp_by_time: rsvp_time,
              email_to: config.email.test_email,
              method: 'random',
              invitation_body: 'This is the invitation body. Participate in this offer!'
            }).then(function(invite) {
              console.log('created invite '+invite.id);
              return Q.all(response_users.map(function(uid, index) {
                var response_time = Date.now() + (index * 60 * 60 *1000);
                return model.createResponse({
                    invitation_id: invite.id,
                    email_address: uid+'@'+config.email.domain,
                    response_time: response_time,
                    response_body: 'This is response body. Let me join!'
                  }).then(function(response) {
                    completed++;
                    console.log('\tcreated response to invite '+invite.id+ ' from '+uid + ' ('+completed+' / '+total+')');
                  }).fail(function(err) {
                    console.log('unable to create response', err)
                  });
              }));
            });
        });
    }));
  })).then(function() {
    console.log('finished!')
    model.close();
  });
}).fail(function(err) {
  console.log('Heres an error', err);
}).done();

