angular.module('rsvp').directive('rsvpInviteDetailView', function(
	$stateParams,
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpInviteDetailView/rsvpInviteDetailView.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {
			var ctrl = this;

			ctrl.toggleResponse = function(response) {
				response.selected = !response.selected;
				RsvpApi.updateInvite(ctrl.invite);
			};

			RsvpInviteApi.fetchInvite($stateParams.inviteId).then(function(invite) {
				ctrl.invite = invite;
			});
		},
	};
});
