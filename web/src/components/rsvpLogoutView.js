angular.module('rsvp').directive('rsvpLogoutView', function(
	$state,
	RsvpAuthApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		controller: function() {
			RsvpAuthApi
				.logout()
				.then(function() {
					$state.go('login');
				});
		},
	};
});
