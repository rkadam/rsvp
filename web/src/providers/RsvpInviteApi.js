angular.module('rsvp').service('RsvpInviteApi', function(
	$q,
	_,
	RsvpApi,
	RsvpAuthApi
) {
	'use strict';

	var RsvpInviteApi = this;
	var _invites = null;

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
			var invite = _.find(invites, 'id', inviteId);
			return invite;
		});
	};

	RsvpInviteApi.createInvite = function(invite) {
		var path = '/users/' + RsvpAuthApi.getUserId() + '/invitations';
		return RsvpApi.post(path, { data: invite });
	};

	RsvpInviteApi.updateInvite = function(invite) {
		var path = '/users/' + RsvpAuthApi.getUserId() + '/invitations/' + invite.id;
		return RsvpApi.put(path, { data: invite });
	};
});
