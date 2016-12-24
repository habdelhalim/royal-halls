(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HallDeleteController',HallDeleteController);

    HallDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hall'];

    function HallDeleteController($uibModalInstance, entity, Hall) {
        var vm = this;

        vm.hall = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hall.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
