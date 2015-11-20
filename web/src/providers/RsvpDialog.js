angular.module('rsvp').service('RsvpDialog', function(
	$document
) {
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
		angular.element('html').css('overflow', 'hidden');
	};

	RsvpDialog.close = function(dialogName) {
		_dialogs[dialogName] = false;
		angular.element('html').css('overflow', 'auto');
	};
});
