(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ScheduleController', ScheduleController);

    ScheduleController.$inject = ['$scope', '$state', '$translate', '$rootScope', 'Event'];

    function ScheduleController($scope, $state, $translate, $rootScope, Event) {
        var vm = this;
        vm.eventSources = [];
        vm.uiConfig = {
            calendar: {
                editable: true,
                header: {
                    left: 'month agendaWeek agendaDay',
                    center: 'title',
                    right: 'today prev,next'
                },
                lang: $translate.proposedLanguage()
            }
        };

        $rootScope.$on('$translateChangeSuccess', function () {
            vm.uiConfig.calendar.lang = $translate.proposedLanguage();
        });

        loadAll();

        function loadAll() {
            Event.query({}, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.queryCount = vm.totalItems;
            vm.events = data.map(function (event) {
                var hallName = event.hall ? event.hall.hallName: '';
                return {
                    title: event.eventName + '  -  [' + hallName + ']' ,
                    start: event.eventStartDate,
                    end: event.eventEndDate
                }
            });
            console.log(vm.events);
            vm.eventSources.pop();
            vm.eventSources.push(vm.events);
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
