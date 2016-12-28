(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ContractSearchController', ContractSearchController);

    ContractSearchController.$inject = ['$timeout', '$scope', '$stateParams', 'ParseLinks', '$uibModalInstance', 'entity', 'Contract', 'Customer', 'Payment', 'Event'];

    function ContractSearchController ($timeout, $scope, $stateParams,  ParseLinks, $uibModalInstance, entity, Contract, Customer, Payment, Event) {
        var vm = this;

        loadAll();

        function loadAll () {
            Contract.query({
                page: 0,
                size: 10,
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.contracts = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.contract = entity;
        vm.clear = clear;
        vm.select = select;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.customers = Customer.query();
        vm.payments = Payment.query();
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function select(item) {
            $uibModalInstance.close(item);
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contract.id !== null) {
                Contract.update(vm.contract, onSaveSuccess, onSaveError);
            } else {
                Contract.save(vm.contract, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('royalhallsApp:contractUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.contractDate = false;
        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
