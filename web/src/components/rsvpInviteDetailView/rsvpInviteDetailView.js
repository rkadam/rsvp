angular.module('rsvp').directive('rsvpInviteDetailView', function(
	$stateParams,
	RsvpApi
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

			ctrl.toggleUser = function(user) {
				user.selected = !user.selected;
				RsvpApi.updateInvite(ctrl.invite);
			};

			RsvpApi.getInvite($stateParams.inviteId).then(function(invite) {
				ctrl.invite = invite;
			});
		},
	};
});
