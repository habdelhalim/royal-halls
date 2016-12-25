(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventTypeDialogController', EventTypeDialogController);

    EventTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EventType'];

    function EventTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EventType) {
        var vm = this;

        vm.eventType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.eventType.id !== null) {
                EventType.update(vm.eventType, onSaveSuccess, onSaveError);
            } else {
                EventType.save(vm.eventType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:eventTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
