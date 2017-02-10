(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionDialogController', ExtraOptionDialogController);

    ExtraOptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExtraOption', 'ExtraOptionVariant', 'ExtraOptionColor'];

    function ExtraOptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExtraOption, ExtraOptionVariant, ExtraOptionColor) {
        var vm = this;

        vm.extraOption = entity;
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
            if (vm.extraOption.id !== null) {
                ExtraOption.update(vm.extraOption, onSaveSuccess, onSaveError);
            } else {
                ExtraOption.save(vm.extraOption, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:extraOptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
