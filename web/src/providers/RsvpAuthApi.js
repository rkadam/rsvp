angular.module('rsvp').service('RsvpAuthApi', function(
	$cookies,
	_,
	RsvpApi
) {
	'use strict';

	var RsvpAuthApi = this;
	var _loginUserId;

	RsvpAuthApi.USER_ID_COOKIE = 'user_id';

	RsvpAuthApi.init = function() {
		_loginUserId = getUserIdCookie();
		setUserIdCookie(_loginUserId);  // Renews the cookie
	};

	RsvpAuthApi.isLoggedIn = function() {
		return !_.isEmpty(_loginUserId);
	};

	RsvpAuthApi.getUserId = function() {
		return _loginUserId;
	};

	RsvpAuthApi.login = function(userId, password) {
		var data = { uid: userId, password: password };

		return RsvpApi
			.post('/login', { data: data })
			.then(function(data) {
				_loginUserId = userId;
				setUserIdCookie(userId);
				return data;
			});
	};

	RsvpAuthApi.logout = function() {
		return RsvpApi
			.get('/logout')
			.then(function() {
				_loginUserId = null;
				removeUserIdCookie()
			});
	};

	function getUserIdCookie() {
		var userId = $cookies.get(RsvpAuthApi.USER_ID_COOKIE);
		return userId;
	}

	function setUserIdCookie(userId) {
		if (!userId) {
			return;
		}

		var oneWeekFromNow = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000);
		$cookies.put(RsvpAuthApi.USER_ID_COOKIE, userId, { expires: oneWeekFromNow });
	}

	function removeUserIdCookie() {
		$cookies.remove(RsvpAuthApi.USER_ID_COOKIE);
	}

	RsvpAuthApi.init();
});
