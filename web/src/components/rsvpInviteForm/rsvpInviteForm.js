angular.module('rsvp').directive('rsvpInviteForm', function(
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpInviteForm/rsvpInviteForm.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {},
	};
});
