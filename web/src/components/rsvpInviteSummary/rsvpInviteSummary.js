angular.module('rsvp').directive('rsvpInviteSummary', function(
	_,
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			invite: '=',
		},
		templateUrl: 'components/rsvpInviteSummary/rsvpInviteSummary.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function($scope) {
			var ctrl = this;

			$scope.$watch('ctrl.invite.responses', function() {
				ctrl.numChosen = RsvpInviteApi.getNumResponsesChosen(ctrl.invite);
			}, true);

			RsvpInviteApi.onUpdateInvite(function(invite) {
				ctrl.numChosen = RsvpInviteApi.getNumResponsesChosen(ctrl.invite);
			});
		},
	};
});
