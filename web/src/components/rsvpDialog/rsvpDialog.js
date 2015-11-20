angular.module('rsvp').directive('rsvpDialog', function(
	RsvpDialog
) {
	'use strict';

	return {
		restrict: 'E',
		scope: {
			name: '@',
		},
		templateUrl: 'components/rsvpDialog/rsvpDialog.html',
		transclude: true,
		controllerAs: 'ctrl',
		bindToController: true,
		controller: function($scope, $element) {
			var ctrl = this;

			RsvpDialog.add(ctrl.name);

			$scope.$watch(function() {
				return RsvpDialog.isOpen(ctrl.name);
			}, function(isOpen, wasOpen) {
				if (isOpen !== wasOpen) {
					$element.toggleClass('-open', isOpen);
				}
			});
		},
	}
})
