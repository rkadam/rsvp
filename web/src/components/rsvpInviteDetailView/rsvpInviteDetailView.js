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
			var inviteId = $stateParams.inviteId;

			ctrl.toggleResponse = function(response) {
				response.selected = !response.selected;
				RsvpInviteApi.updateInvite(ctrl.invite);
			};

			ctrl.numChosen = 0;

			RsvpInviteApi.onUpdateInvite(function(invite) {
				ctrl.numChosen = RsvpInviteApi.getNumChosenForInvite(inviteId);
			});

			RsvpInviteApi.fetchInvite(inviteId).then(function(invite) {
				ctrl.invite = invite;
			});
		},
	};
});
