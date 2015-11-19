var model = require('./model');
model.flush().then(function() {
  model.createInvitation('raju', 'test', 9, 0, 'gregm@pandora,com', 'random', 'hellooo')
    .then(function(res) {
      console.log(res)
      res.title = 'test updated';
      return model.updateInvitation(res);
    }).then(function(res) {
      return model.createResponse(res.id, 'ivo@pandora.com', 0, 'response');
    }).then(function(res) {
      return model.fetchInvitation(res.invitation_id);
    }).then(function(res) {
      console.log(res);
      return model.fetchInvitationList('raju');
    }).then(function(res) {
      console.log(res);

      model.close();
    });


});

