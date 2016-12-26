(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ContractDetailController', ContractDetailController);

    ContractDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contract', 'Customer', 'Payment', 'Event'];

    function ContractDetailController($scope, $rootScope, $stateParams, previousState, entity, Contract, Customer, Payment, Event) {
        var vm = this;

        vm.contract = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:contractUpdate', function(event, result) {
            vm.contract = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
