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
			var path = '/users/' + RsvpAuthApi.getUserId() + '/invitations';

			return RsvpApi.get(path, { cache: true })
				.then(function(invites) {
					_invites = invites;
					return invites;
				});
		}
	};

	RsvpInviteApi.fetchInvite = function(inviteId) {
		return RsvpInviteApi.fetchInvites().then(function(invites) {
			var invite = RsvpInviteApi.getInvite(inviteId);
			return invite;
		});
	};

	RsvpInviteApi.createInvite = function(invite) {
		var path = '/users/' + RsvpAuthApi.getUserId() + '/invitations';
		return RsvpApi.post(path, { data: invite });
	};

	RsvpInviteApi.updateInvite = function(invite) {
		var path = '/users/' + RsvpAuthApi.getUserId() + '/invitations/' + invite.id;
		return RsvpApi.put(path, { data: invite })
			.then(function(updatedInvite) {  // @todo Rename to 'invite' once API is fixed
				notifyListeners('onUpdateInvite', invite);
			});
	};

	RsvpInviteApi.onUpdateInvite = function(listener) {
		addListener('onUpdateInvite', listener);
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
