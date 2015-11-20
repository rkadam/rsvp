angular.module('rsvp').directive('rsvpInvitesView', function(
	$state,
	_,
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpInvitesView/rsvpInvitesView.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {
			var ctrl = this;

			RsvpInviteApi.fetchInvites().then(function(invites) {
				ctrl.invites = _.sortByOrder(invites, ['active', 'desc'], ['rsvp_by_time', 'asc']);
				$state.go('invites.detail', { inviteId: ctrl.invites[0].id });
			});
		},
	};
});
