angular.module('rsvp').directive('rsvpDeptChart', function() {
	'use strict';
	return {
		restrict: 'E',
		scope: {
			invite:'=',
			selected: '=',
		},
		templateUrl: 'components/rsvpDeptChart/rsvpDeptChart.html',
		link: function(scope, iElement, attrs) {
			scope.options = {
				chart: {
					type: 'pieChart',
					donut: true,
					donutRatio: 0.4,
					// height: 300,
					margin : {
						top: 20,
						right: 20,
						bottom: 50,
						left: 55
					},
					x: function(d){return d.label;},
					y: function(d){return d.value;},
					showValues: true,
					valueFormat: function(d){
							return parseInt(d);
					},
					color: function(d, i) {
						var color = d3.rgb('#224099').brighter(i*0.7);
						return color;
					},
					legendPosition: 'right',
					duration: 500,
					showLabels: false,
					xAxis: {
						tickFormat : function(d) {
							return d !== '0' ? d : '< 1';
						}
					},
				}
			};

			scope.data = [];
			var updateData = function() {
				if (! scope.invite) { return; }
				scope.data.length = 0;
				var departments = {};
				scope.invite.responses.forEach(function(response) {
					if( scope.selected && ! response.selected) {
						return;
					}
					var dept = response.department;
					if (! departments[dept]) {
						departments[dept] = 0;
					}
					departments[dept]++;
				});
				var values = Object.keys(departments).map(function(dept) {
					return {label: dept, value: departments[dept]};
				});
				scope.data = values;
			};

			scope.$watch('invite.responses', updateData);
			scope.$watch('selected', updateData);
		}
	};
});
