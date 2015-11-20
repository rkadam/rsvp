angular.module('rsvp').config(function(
	$locationProvider,
	$stateProvider,
	$urlMatcherFactoryProvider,
	$urlRouterProvider
) {
	'use strict';

	$stateProvider
		.state('login', {
			url: '/login',
			template: '<rsvp-login-view></rsvp-login-view>',
		})
		.state('logout', {
			url: '/logout',
			template: '<rsvp-logout-view></rsvp-logout-view>',
			data: { requiresLogin: true },
		})
		.state('createInvite', {
			url: '/invites/create',
			template: '<rsvp-create-invite-view></rsvp-create-invite-view>',
			data: { requiresLogin: true },
		})
		.state('invites', {
			url: '/invites',
			template: '<rsvp-invites-view></rsvp-invites-view>',
			data: { requiresLogin: true },
		})
		.state('invites.detail', {
			url: '/{inviteId}',
			views: {
				detail: {
					template: '<rsvp-invite-detail-view></rsvp-invite-detail-view>'
				}
			}
		});

	$urlRouterProvider.otherwise('/invites');

	$urlMatcherFactoryProvider.strictMode(false);
	$locationProvider.html5Mode(true);
});
