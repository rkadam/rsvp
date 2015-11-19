angular.module('rsvp').directive('rsvpResponse', function() {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			response: '=',
		},
		templateUrl: 'components/rsvpResponse/rsvpResponse.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {},
	};
});
