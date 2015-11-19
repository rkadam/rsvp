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
				ctrl.numChosen = getNumChosen(ctrl.invite);
			}, true);

			RsvpInviteApi.onUpdateInvite(function(invite) {
				if (invite.id === ctrl.invite.id) {
					ctrl.numChosen = getNumChosen(invite);
				}
			});
		},
	};

	function getNumChosen(invite) {
		return _.filter(invite.responses, 'selected').length;
	}
});
