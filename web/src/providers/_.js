angular.module('rsvp').factory('_', function(
	$window
) {
	'use strict';

	return $window._.noConflict();
});
