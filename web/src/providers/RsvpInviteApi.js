angular.module('rsvp').service('RsvpInviteApi', function(
	$q,
	_,
	RsvpApi,
	RsvpAuthApi
) {
	'use strict';

	var RsvpInviteApi = this;
	var _invites = null;
	var _listeners = {};

	RsvpInviteApi.getInvite = function(inviteId) {
		var invite = _.find(_invites, 'id', inviteId);
		return invite;
	};

	RsvpInviteApi.getNumChosenForInvite = function(inviteId) {
		if (!_invites) {
			return 0;
		}
		else {
			var responses = RsvpInviteApi.getInvite(inviteId).responses;
			var numChosen = _.filter(responses, 'selected', true).length;
			return numChosen;
		}
	};

	RsvpInviteApi.fetchInvites = function() {
		if (_invites) {
			// Already cached
			return $q.resolve(_invites);
		}
		else {
			return RsvpApi
				.get('/users/' + RsvpAuthApi.getUserId() + '/invitations')
				.then(function(invites) {
					_invites = invites;
					RsvpInviteApi.notifyUpdateInvites(invites);
					return invites;
				});
		}
	};

	RsvpInviteApi.fetchInvite = function(inviteId) {
		return RsvpInviteApi
			.fetchInvites()
			.then(function(invites) {
				var invite = RsvpInviteApi.getInvite(inviteId);
				return invite;
			});
	};

	RsvpInviteApi.createInvite = function(invite) {
		return RsvpApi.post('/users/' + RsvpAuthApi.getUserId() + '/invitations', { data: invite });
	};

	RsvpInviteApi.updateInvite = function(invite) {
		return RsvpApi
			.put('/users/' + RsvpAuthApi.getUserId() + '/invitations/' + invite.id, { data: invite })
			.then(function(invite) {
				RsvpInviteApi.notifyUpdateInvite(invite);
			});
	};

	RsvpInviteApi.selectWinners = function(inviteId) {
		return RsvpApi
			.get('/users/' + RsvpAuthApi.getUserId() + '/invitations/' + inviteId + '/selectWinners')
			.then(function(invite) {
				RsvpInviteApi.notifyUpdateInvite(invite);
				return invite;
			});
	};

	RsvpInviteApi.closeInvite = function(inviteId, acceptedBody, rejectedBody) {
		var data = {
			accepted_body: acceptedBody,
			rejected_body: rejectedBody,
		};

		return RsvpApi.post('/users/' + RsvpAuthApi.getUserId() + '/invitations/' + inviteId + '/selectWinners', { data: data });
	};

	RsvpInviteApi.onUpdateInvites = function(listener) {
		addListener('onUpdateInvites', listener);
	};

	RsvpInviteApi.notifyUpdateInvites = function(invites) {
		notifyListeners('onUpdateInvites', invites);
	};

	RsvpInviteApi.onUpdateInvite = function(listener) {
		addListener('onUpdateInvite', listener);
	};

	RsvpInviteApi.notifyUpdateInvite = function(invite) {
		notifyListeners('onUpdateInvite', invite);
	};

	function addListener(methodName, listener) {
		_listeners[methodName] = _listeners[methodName] || [];
		_listeners[methodName].push(listener);
	}

	function notifyListeners(methodName, data) {
		_.each(_listeners[methodName], function(listener) {
			listener(data);
		});
	}
});
