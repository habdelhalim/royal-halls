(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ScheduleController', ScheduleController);

    ScheduleController.$inject = ['$scope', '$compile', '$translate', '$rootScope', '$state', 'Event'];

    function ScheduleController($scope, $compile, $translate, $rootScope, $state, Event) {
        var vm = this;
        vm.eventSources = [];
        vm.eventRender = eventRender;
        vm.eventClick = eventClick;

        vm.uiConfig = {
            calendar: {
                editable: true,
                header: {
                    left: 'month agendaWeek agendaDay listYear',
                    center: 'title',
                    right: 'today next,prev'
                },
                lang: $translate.proposedLanguage(),
                eventRender: vm.eventRender,
                eventClick: vm.eventClick
            }
        };

        var unsubscribe = $rootScope.$on('$translateChangeSuccess', function () {
            vm.uiConfig.calendar.lang = $translate.proposedLanguage();
        });

        //cleanup rootScope listener
        $scope.$on('$destroy', function () {
            if (unsubscribe) {
                unsubscribe();
            }
        });

        loadAll();

        function eventClick(event, element, view) {
            if (event.contract !== undefined) {
                $state.go('contract-edit', {
                    id: event.contract
                });
            }
        }

        function eventRender(event, element, view) {
            element.attr({
                'uib-tooltip': event.title,
                'tooltip-placement': 'top'
            });
            $compile(element)($scope);
        }

        function loadAll() {
            Event.query({size: 1000}, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.queryCount = vm.totalItems;
            vm.events = data.map(function (event) {
                var hallName = event.hall ? event.hall.hallName : '';
                var hallId = event.hall ? event.hall.id - 1 : 0;
                var colors = ['#0086AB', '#485357', '#F59958', '#FF8787'];
                var color = event.hall && event.hall.colour ? event.hall.colour : colors[hallId];

                return {
                    title: event.eventName + '  -  [' + hallName + ']',
                    start: event.eventStartDate,
                    end: event.eventEndDate,
                    contract: event.contract.id,
                    color: color
                }
            });
            vm.eventSources.pop();
            vm.eventSources.push(vm.events);
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
