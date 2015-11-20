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

			function setInvites(invites) {
				ctrl.invites = _.sortByOrder(invites, ['active', 'desc'], ['rsvp_by_time', 'asc']);

				if (_.isEmpty(ctrl.invites)) {
					$state.go('start');
				}
				else {
					$state.go('invites.detail', { inviteId: ctrl.invites[0].id });
				}
			}

			RsvpInviteApi.onUpdateInvites(setInvites);
			RsvpInviteApi.fetchInvites().then(setInvites);
		},
	};
});
