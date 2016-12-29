(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionColorDeleteController',ExtraOptionColorDeleteController);

    ExtraOptionColorDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExtraOptionColor'];

    function ExtraOptionColorDeleteController($uibModalInstance, entity, ExtraOptionColor) {
        var vm = this;

        vm.extraOptionColor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExtraOptionColor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
