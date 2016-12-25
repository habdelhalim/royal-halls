(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ContractTypeDetailController', ContractTypeDetailController);

    ContractTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ContractType'];

    function ContractTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, ContractType) {
        var vm = this;

        vm.contractType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:contractTypeUpdate', function(event, result) {
            vm.contractType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
