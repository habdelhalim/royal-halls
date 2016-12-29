(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionColorDialogController', ExtraOptionColorDialogController);

    ExtraOptionColorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExtraOptionColor', 'ExtraOption'];

    function ExtraOptionColorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExtraOptionColor, ExtraOption) {
        var vm = this;

        vm.extraOptionColor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.extraoptions = ExtraOption.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.extraOptionColor.id !== null) {
                ExtraOptionColor.update(vm.extraOptionColor, onSaveSuccess, onSaveError);
            } else {
                ExtraOptionColor.save(vm.extraOptionColor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:extraOptionColorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
