angular.module('rsvp').directive('rsvpStartView', function(
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpStartView/rsvpStartView.html',
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
