angular.module('rsvp', [
	'ngCookies',
	'ui.router',
  'nvd3'
]);

angular.module('rsvp').run(function(
	$rootScope,
	$state,
	_,
	RsvpAuthApi
) {
	// Login enforcement
	$rootScope.$on('$stateChangeStart', function(event, toState) {
		if (_.get(toState, 'data.requiresLogin') && !RsvpAuthApi.isLoggedIn()) {
			event.preventDefault();
			$state.go('login');
		}
	});
});
