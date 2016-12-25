(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HallDialogController', HallDialogController);

    HallDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hall', 'HallType'];

    function HallDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hall, HallType) {
        var vm = this;

        vm.hall = entity;
        vm.clear = clear;
        vm.save = save;
        vm.halltypes = HallType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hall.id !== null) {
                Hall.update(vm.hall, onSaveSuccess, onSaveError);
            } else {
                Hall.save(vm.hall, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:hallUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
