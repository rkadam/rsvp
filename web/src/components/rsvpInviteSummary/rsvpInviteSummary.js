angular.module('rsvp').directive('rsvpInviteSummary', function(
	_
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

			$scope.$watch('ctrl.invite.responses', function(responses) {
				ctrl.numChosen = _.filter(responses, 'selected').length;
			}, true);
		},
	};
});
