angular.module('rsvp').config(function(
	$locationProvider,
	$stateProvider,
	$urlMatcherFactoryProvider,
	$urlRouterProvider
) {
	'use strict';

	$stateProvider
		.state('createInvite', {
			url: '/invites/create',
			template: '<rsvp-invite-form></rsvp-invite-form>',
		})
		.state('invites', {
			url: '/invites',
			template: '<rsvp-invites-view></rsvp-invites-view>',
		})
		.state('invites.detail', {
			url: '/{inviteId}',
			views: {
				detail: {
					template: '<rsvp-invite-detail-view></rsvp-invite-detail-view>'
				}
			}
		});

	// $urlRouterProvider.otherwise('/invites');

	$urlMatcherFactoryProvider.strictMode(false);
	$locationProvider.html5Mode(true);
});
