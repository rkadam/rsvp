angular.module('rsvp').directive('rsvpInvitesView', function(
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpInvitesView/rsvpInvitesView.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {
			var ctrl = this;

			RsvpInviteApi.fetchInvites().then(function(invites) {
				ctrl.invites = invites;
			});
		},
	};
});
