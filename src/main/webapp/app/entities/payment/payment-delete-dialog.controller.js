(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('PaymentDeleteController', PaymentDeleteController);

    PaymentDeleteController.$inject = ['$uibModalInstance', '$scope', 'entity', 'Payment'];

    function PaymentDeleteController($uibModalInstance, $scope, entity, Payment) {
        var vm = this;

        vm.payment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            Payment.delete({
                    id: id
                },
                function() {
                    $scope.$emit('royalhallsApp:contractUpdate', {
                        id: id
                    });
                    $uibModalInstance.close(true);
                });
        }
    }
})();
