(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$timeout', '$scope', '$rootScope', '$stateParams', '$uibModalInstance', 'entity', 'Event', 'EventExtraOption', 'EventType', 'Hall', 'Contract'];

    function EventDialogController($timeout, $scope, $rootScope, $stateParams, $uibModalInstance, entity, Event, EventExtraOption, EventType, Hall, Contract) {
        var vm = this;

        vm.event = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.eventextraoptions = EventExtraOption.query();
        vm.eventtypes = EventType.query();
        vm.halls = Hall.query();
        vm.contracts = Contract.query();


        $rootScope.$on('royalhallsApp:eventExtraOptionUpdate', function () {
            if (vm.event.id !== null) {
                Event.get({id: vm.event.id}, function (entity) {
                    vm.event = entity;
                });
            }
        });

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('royalhallsApp:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.eventStartDate = false;
        vm.datePickerOpenStatus.eventEndDate = false;
        vm.datePickerOpenStatus.createdDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
