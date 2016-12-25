(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HallTypeDeleteController',HallTypeDeleteController);

    HallTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'HallType'];

    function HallTypeDeleteController($uibModalInstance, entity, HallType) {
        var vm = this;

        vm.hallType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HallType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
