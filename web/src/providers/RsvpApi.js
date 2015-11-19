angular.module('rsvp').service('RsvpApi', function(
	$http,
	$q,
	_
) {
	'use strict';

	var RsvpApi = this;

	RsvpApi.get = _.partial(makeRequest, 'GET');
	RsvpApi.post = _.partial(makeRequest, 'POST');
	RsvpApi.put = _.partial(makeRequest, 'PUT');
	RsvpApi.patch = _.partial(makeRequest, 'PATCH');
	RsvpApi.delete = _.partial(makeRequest, 'DELETE');

	function makeRequest(method, path, options) {
		var defer = $q.defer();
		var url = '/api' + path;

		$http({
			method: method,
			url: url,
			params: options.params,
			data: options.data,
			cache: options.cache,
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
