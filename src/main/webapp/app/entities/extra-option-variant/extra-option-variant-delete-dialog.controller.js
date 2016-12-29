(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionVariantDeleteController',ExtraOptionVariantDeleteController);

    ExtraOptionVariantDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExtraOptionVariant'];

    function ExtraOptionVariantDeleteController($uibModalInstance, entity, ExtraOptionVariant) {
        var vm = this;

        vm.extraOptionVariant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExtraOptionVariant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
