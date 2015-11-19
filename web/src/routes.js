angular.module('rsvp').config(function(
	$locationProvider,
	$stateProvider,
	$urlMatcherFactoryProvider,
	$urlRouterProvider
) {
	'use strict';

	$stateProvider
		.state('home', {
			url: '/',
			template: '<rsvp-invite-summary></rsvp-invite-summary>',
		});

	$urlRouterProvider.otherwise('/');

	$urlMatcherFactoryProvider.strictMode(false);
	$locationProvider.html5Mode(true);
});
