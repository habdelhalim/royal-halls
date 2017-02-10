(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionVariantDialogController', ExtraOptionVariantDialogController);

    ExtraOptionVariantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExtraOptionVariant', 'ExtraOption'];

    function ExtraOptionVariantDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, ExtraOptionVariant, ExtraOption) {
        var vm = this;

        vm.extraOptionVariant = entity;
        vm.clear = clear;
        vm.save = save;
        vm.extraoptions = ExtraOption.query({page: 0, size: 100});

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.extraOptionVariant.id !== null) {
                ExtraOptionVariant.update(vm.extraOptionVariant, onSaveSuccess, onSaveError);
            } else {
                ExtraOptionVariant.save(vm.extraOptionVariant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('royalhallsApp:extraOptionVariantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
