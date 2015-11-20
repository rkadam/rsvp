angular.module('rsvp').directive('rsvpCreateInviteView', function(
	$state,
	RsvpInviteApi
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'components/rsvpCreateInviteView/rsvpCreateInviteView.html',
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function() {
			var ctrl = this;

			ctrl.createInvite = function(invite) {
				ctrl.isSending = true;

				var rsvpDateEpoch = invite.rsvp_by_date.getTime();
				var rsvpTimeEpoch = invite.rsvp_by_time.getTime();
				var rsvpDatetimeEpoch = rsvpDateEpoch + rsvpTimeEpoch;

				RsvpInviteApi.createInvite({
					title: invite.title,
					invitation_body: invite.invitation_body,
					email_to: invite.email_to,
					rsvp_by_time: rsvpDatetimeEpoch,
					response_accept_limit: invite.response_accept_limit,
					method: invite.method,
				})
				.then(function(invite) {
					$state.go('invites.detail', { inviteId: invite.id });
				})
				.catch(function() {
					ctrl.isSending = false;
					ctrl.hasSendError = true;
				});
			};
		},
	};
});
