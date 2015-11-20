angular.module('rsvp').directive('rsvpCreateInviteView', function(
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpCreateInviteView/rsvpCreateInviteView.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {},
	};
});
