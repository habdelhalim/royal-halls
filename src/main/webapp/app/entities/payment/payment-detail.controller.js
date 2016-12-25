(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('PaymentDetailController', PaymentDetailController);

    PaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Payment', 'Contract'];

    function PaymentDetailController($scope, $rootScope, $stateParams, previousState, entity, Payment, Contract) {
        var vm = this;

        vm.payment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:paymentUpdate', function(event, result) {
            vm.payment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
