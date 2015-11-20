angular.module('rsvp').directive('rsvpInviteDetailView', function(
	$stateParams,
	$timeout,
	_,
	RsvpDialog,
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

			ctrl.numChosen = RsvpInviteApi.getNumChosenForInvite(inviteId);

			ctrl.toggleResponse = function(response) {
				response.selected = !response.selected;
				RsvpInviteApi.updateInvite(ctrl.invite);
			};

			ctrl.selectWinners = function() {
				ctrl.invite.active = false;
				RsvpInviteApi.notifyUpdateInvite(ctrl.invite);

				RsvpInviteApi.selectWinners(ctrl.invite.id);
			};

			ctrl.sendWrapUp = function(acceptedBody, rejectedBody) {
				ctrl.invite.sent_time = Date.now();

				RsvpInviteApi.closeInvite(ctrl.invite.id, acceptedBody, rejectedBody);

				$timeout(function() {
					RsvpDialog.close('wrapup');
				}, 750);
			};

			RsvpInviteApi.onUpdateInvite(function(invite) {
				ctrl.numChosen = RsvpInviteApi.getNumChosenForInvite(inviteId);
			});

			RsvpInviteApi.fetchInvite(inviteId).then(function(invite) {
				ctrl.invite = invite;
			});
		},
	};
});
