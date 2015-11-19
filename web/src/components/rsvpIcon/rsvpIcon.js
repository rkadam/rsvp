angular.module('rsvp').directive('rsvpIcon', function() {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			icon: '@',
			size: '@?',
		},
		templateUrl: 'components/rsvpIcon/rsvpIcon.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {},
	}
})
