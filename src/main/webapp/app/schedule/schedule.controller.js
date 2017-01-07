(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ScheduleController', ScheduleController);

    ScheduleController.$inject = ['$scope', '$compile', '$translate', '$rootScope', 'Event'];

    function ScheduleController($scope, $compile, $translate, $rootScope, Event) {
        var vm = this;
        vm.eventSources = [];
        vm.eventRender = eventRender;

        vm.uiConfig = {
            calendar: {
                editable: true,
                header: {
                    left: 'month agendaWeek agendaDay',
                    center: 'title',
                    right: 'today prev,next'
                },
                lang: $translate.proposedLanguage(),
                eventRender: vm.eventRender
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

                return {
                    title: event.eventName + '  -  [' + hallName + ']',
                    start: event.eventStartDate,
                    end: event.eventEndDate,
                    color: colors[hallId]
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
