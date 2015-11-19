angular.module('rsvp').directive('rsvpBadge', function() {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			label: '@',
		},
		templateUrl: 'components/rsvpBadge/rsvpBadge.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {},
	};
});
