angular.module('rsvp').directive('rsvpLoginView', function(
	$state,
	RsvpAuthApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpLoginView/rsvpLoginView.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {
			var ctrl = this;

			ctrl.login = function(userId, password) {
				ctrl.isLoggingIn = true;
				ctrl.hasLoginError = false;

				RsvpAuthApi
					.login(userId, password)
					.then(function() {
						$state.go('invites');
					})
					.catch(function() {
						ctrl.isLoggingIn = false;
						ctrl.hasLoginError = true;
					});
			};

			if (RsvpAuthApi.isLoggedIn()) {
				$state.go('invites');
			}
		},
	};
});
