angular.module('rsvp').service('RsvpAuthApi', function(
	RsvpApi
) {
	'use strict';

	var RsvpAuthApi = this;
	var _loginUserId = 'mpetrovich';  // @todo Remove default value before shipping

	RsvpAuthApi.isLoggedIn = function() {
		return !_.isEmpty(_loginUserId);
	};

	RsvpAuthApi.getUserId = function() {
		return _loginUserId;
	};

	RsvpAuthApi.login = function(userId, password) {
		var data = { uid: userId, password: password };

		return RsvpApi.post('/login', { data: data })
			.then(function(data) {
				_loginUserId = userId;
				return data;
			});
	};

	RsvpAuthApi.logout = function() {
		return RsvpApi.get('/logout');
	};
});
