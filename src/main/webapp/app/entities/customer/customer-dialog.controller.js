(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('CustomerDialogController', CustomerDialogController);

    CustomerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance',
        'entity', 'Customer', 'Contact', 'CustomerStatus'];

    function CustomerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Customer, Contact, CustomerStatus) {
        var vm = this;

        vm.customer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.contacts = Contact.query();
        vm.statuses = CustomerStatus.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customer.id !== null) {
                Customer.update(vm.customer, onSaveSuccess, onSaveError);
            } else {
                Customer.save(vm.customer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:customerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
