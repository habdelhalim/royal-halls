(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventExtraOptionDialogController', EventExtraOptionDialogController);

    EventExtraOptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EventExtraOption', 'Event', 'ExtraOption', 'ExtraOptionVariant', 'ExtraOptionColor'];

    function EventExtraOptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EventExtraOption, Event, ExtraOption, ExtraOptionVariant, ExtraOptionColor) {
        var vm = this;

        vm.eventExtraOption = entity;
        vm.clear = clear;
        vm.save = save;
        vm.events = Event.query();
        vm.extraoptions = ExtraOption.query();
        vm.extraoptionvariants = ExtraOptionVariant.query();
        vm.extraoptioncolors = ExtraOptionColor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.eventExtraOption.id !== null) {
                EventExtraOption.update(vm.eventExtraOption, onSaveSuccess, onSaveError);
            } else {
                EventExtraOption.save(vm.eventExtraOption, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:eventExtraOptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
