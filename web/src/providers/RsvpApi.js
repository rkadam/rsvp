angular.module('rsvp').service('RsvpApi', function(
	$http,
	$q
) {
	'use strict';

	var RsvpApi = this;
	var _userId = 'mpetrovich';

	RsvpApi.getUserId = function() {
		return _userId;
	};

	RsvpApi.login = function(userId, password) {
		return makeRequest('post', '/api/login', { uid: userId, password: password })
			.then(function(data) {
				_userId = userId;
				return data;
			});
	};

	RsvpApi.logout = function() {
		return makeRequest('get', '/api/logout');
	};

	RsvpApi.getInvites = function(userId) {
		userId = userId || _userId;
		return makeRequest('get', '/api/users/' + userId + '/invitations');
	};

	RsvpApi.postInvite = function(invite, userId) {
		userId = userId || _userId;
		return makeRequest('post', '/api/users/' + userId + '/invitations', invite);
	};

	RsvpApi.getInvite = function(inviteId, userId) {
		userId = userId || _userId;
		return makeRequest('get', '/api/users/' + userId + '/invitations/' + inviteId);
	};

	RsvpApi.updateInvite = function(invite, userId) {
		userId = userId || _userId;
		return makeRequest('put', '/api/users/' + userId + '/invitations/' + invite.id, invite);
	};

	function makeRequest(method, url, data) {
		var defer = $q.defer();

		$http({
			method: method,
			url: url,
			data: data,
		})
		.then(function(response) {
			if (response.data && response.data.success) {
				defer.resolve(response.data.data);
			}
			else {
				defer.reject(response);
			}
		})
		.catch(function(response) {
			defer.reject(response);
		});

		return defer.promise;
	}
});
