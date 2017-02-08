(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventExtraOptionDialogController', EventExtraOptionDialogController);

    EventExtraOptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'optionType', 'EventExtraOption', 'Event', 'ExtraOption', 'ExtraOptionVariant', 'ExtraOptionColor'];

    function EventExtraOptionDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, optionType, EventExtraOption, Event, ExtraOption, ExtraOptionVariant, ExtraOptionColor) {
        var vm = this;

        vm.eventExtraOption = entity;
        if (optionType === undefined) {
            optionType = 'BASIC'
        }

        if (entity.option !== undefined) {
            optionType = entity.option.optionType
        }

        vm.clear = clear;
        vm.save = save;
        vm.extraoptions = ExtraOption.queryByType({type: optionType}, onOptionLoad);
        vm.extraoptionvariants = [];
        vm.extraoptioncolors = [];

        vm.selectOption = selectOption;
        vm.selectVariant = selectVariant;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.eventExtraOption.id !== null) {
                EventExtraOption.update(vm.eventExtraOption, onSaveSuccess, onSaveError);
            } else {
                EventExtraOption.save(vm.eventExtraOption, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('royalhallsApp:eventExtraOptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function onOptionLoad() {
            selectOption();
            selectVariant();
        }

        function selectOption() {

            if (vm.eventExtraOption.option !== undefined) {
                vm.extraoptionvariants = ExtraOptionVariant.queryByOption(
                    {optionId: vm.eventExtraOption.option.id}
                );
            }
        }

        function selectVariant() {
            if (vm.eventExtraOption.option !== undefined) {
                vm.extraoptioncolors = ExtraOptionColor.queryByOption(
                    {optionId: vm.eventExtraOption.option.id}
                );
            }
        }


    }
})();
