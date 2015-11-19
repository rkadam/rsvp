angular.module('rsvp').directive('rsvpUser', function() {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			user: '=',
		},
		templateUrl: 'components/rsvpUser/rsvpUser.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {},
	};
});
