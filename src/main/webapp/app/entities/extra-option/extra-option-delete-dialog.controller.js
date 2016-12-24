(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionDeleteController',ExtraOptionDeleteController);

    ExtraOptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExtraOption'];

    function ExtraOptionDeleteController($uibModalInstance, entity, ExtraOption) {
        var vm = this;

        vm.extraOption = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExtraOption.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
