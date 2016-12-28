(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ScheduleController', ScheduleController);

    ScheduleController.$inject = ['$scope', '$state', '$translate', '$rootScope'];

    function ScheduleController ($scope, $state, $translate, $rootScope) {
        var vm = this;
        vm.eventSources = [];
        vm.uiConfig = {
            calendar:{
                editable: true,
                header:{
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
    }
})();
