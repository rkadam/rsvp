angular.module('rsvp').service('RsvpDialog', function() {
	'use strict';

	var RsvpDialog = this;
	var _dialogs = {};

	RsvpDialog.add = function(dialogName) {
		_dialogs[dialogName] = false;
	};

	RsvpDialog.isOpen = function(dialogName) {
		return _dialogs[dialogName];
	};

	RsvpDialog.open = function(dialogName) {
		_dialogs[dialogName] = true;
	};

	RsvpDialog.close = function(dialogName) {
		_dialogs[dialogName] = false;
	};
});
