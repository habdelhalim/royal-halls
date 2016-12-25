(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HallTypeDialogController', HallTypeDialogController);

    HallTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HallType'];

    function HallTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HallType) {
        var vm = this;

        vm.hallType = entity;
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
            if (vm.hallType.id !== null) {
                HallType.update(vm.hallType, onSaveSuccess, onSaveError);
            } else {
                HallType.save(vm.hallType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:hallTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
