var model = require('./model');
var Q = require('q');

var users = ['gmichalec', 'raju', 'ccong', 'ivo', 'mpetrovich'];
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
          var limit = randomBetween(5, 10);
          var rsvp_time = Date.now() + (randomBetween(0, 10) * 60 * 60 * 1000);
          return model.createInvitation(uid, title, limit, rsvp_time, 'dist-rsvp-test@pandora.com', 'random', 'This is the invitation body. Participate in this offer!')
            .then(function(invite) {
              console.log('created invite '+invite.id);
              return Q.all(users.map(function(uid, index) {
                var response_time = Date.now() + (index * 60 * 60 *1000);
                return model.createResponse(invite.id, uid+'@pandora.com', response_time, 'This is response body. Let me join!')
                  .then(function(response) {
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

