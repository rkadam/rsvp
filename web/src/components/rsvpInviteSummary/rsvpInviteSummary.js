angular.module('rsvp').directive('rsvpInviteSummary', function(
	$templateCache
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			inviteId: '=',
		},
		templateUrl: 'components/rsvpInviteSummary/rsvpInviteSummary.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {
			var ctrl = this;
		},
	};
});
