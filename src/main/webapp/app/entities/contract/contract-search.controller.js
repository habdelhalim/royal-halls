(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ContractSearchController', ContractSearchController);

    ContractSearchController.$inject = ['$timeout', '$scope', '$stateParams', 'ParseLinks', '$uibModalInstance', 'entity', 'Contract', 'Customer', 'Payment', 'Event'];

    function ContractSearchController($timeout, $scope, $stateParams, ParseLinks, $uibModalInstance, entity, Contract, Customer, Payment, Event) {
        var vm = this;
        vm.search = '';
        vm.filter = filter;

        $timeout(function () {
            angular.element('#field_search').focus();
        });

        function filter() {
            console.log('searching for ', vm.search);
            if (vm.search.length >= 3 || vm.search.length == 0) {
                loadAll();
            }
        }

        loadAll();

        function loadAll() {
            Contract.query({
                page: 0,
                size: 10,
                search: vm.search
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
                vm.page = 0;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.contract = entity;
        vm.clear = clear;
        vm.select = select;

        function select(item) {
            $uibModalInstance.close(item);
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

    }
})();
