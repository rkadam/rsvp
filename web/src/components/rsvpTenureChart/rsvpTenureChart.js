angular.module('rsvp').directive('rsvpTenureChart', function() {
	'use strict';
	return {
		restrict: 'E',
		scope: {
			invite:'=',
			selected: '=',
		},
		templateUrl: 'components/rsvpTenureChart/rsvpTenureChart.html',
		link: function(scope, iElement, attrs) {
			scope.options = {
				chart: {
					type: 'discreteBarChart',
					// height: 450,
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
					duration: 500,
					showYAxis: false,
					xAxis: {
						tickFormat : function(d) {
							return d !== '0' ? d : '< 1';
						}
					},
				}
			};

			scope.data = [{
				 key: "Years at Pandora",
				 values: []
			}];
			var updateData = function() {
				if (! scope.invite) { return; }
				var years = {};
				scope.data[0].values.length = 0;
				scope.invite.responses.forEach(function(response) {
					var year = Math.floor(response.years);
					if( scope.selected && ! response.selected) {
						return;
					}
					if (! years[year]) {
						years[year] = 0;
					}
					years[year]++;
				});
				var values = Object.keys(years).map(function(year) {
					return {label: year, value: years[year]};
				});
				scope.data[0].values = values;
			};

			scope.$watch('invite.responses', updateData);
			scope.$watch('selected', updateData);

		}
	};
});
