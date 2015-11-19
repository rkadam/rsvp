angular.module('rsvp').directive('rsvpInvitesView', function(
	RsvpApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			userId: '=',
		},
		templateUrl: 'components/rsvpInvitesView/rsvpInvitesView.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {
			var ctrl = this;

			RsvpApi.getInvites(ctrl.userId).then(function(invites) {
				ctrl.invites = invites;
			});
		},
	};
});
