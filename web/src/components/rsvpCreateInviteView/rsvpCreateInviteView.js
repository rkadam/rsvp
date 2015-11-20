angular.module('rsvp').directive('rsvpCreateInviteView', function(
	$state,
	$stateParams,
	moment,
	RsvpAuthApi,
	RsvpDialog,
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
			var rsvpDatetime = moment()
				.add(4, 'hours')
				.set({ second: 0, millisecond: 0 })
				.toDate();

			ctrl.userId = RsvpAuthApi.getUserId();

			// Defaults
			ctrl.invite = {};
			ctrl.invite.title = $stateParams.title;
			ctrl.invite.method = 'random';
			ctrl.invite.response_accept_limit = 1;
			ctrl.invite.rsvp_by_date = rsvpDatetime;
			ctrl.invite.rsvp_by_time = rsvpDatetime;

			ctrl.createInvite = function(invite) {
				ctrl.isSending = true;
				ctrl.hasSendError = false;

				var rsvpDateEpoch = invite.rsvp_by_date.getTime();
				var rsvpTimeEpoch = invite.rsvp_by_time ? invite.rsvp_by_time.getTime() : 0;
				var rsvpDatetimeEpoch = rsvpDateEpoch + rsvpTimeEpoch;

				RsvpInviteApi
					.createInvite({
						title: invite.title,
						invitation_body: invite.invitation_body,
						email_to: invite.email_to,
						rsvp_by_time: rsvpDatetimeEpoch,
						response_accept_limit: invite.response_accept_limit,
						method: invite.method || 'random',
					})
					.then(function(invite) {
						RsvpDialog.close('preview');
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
