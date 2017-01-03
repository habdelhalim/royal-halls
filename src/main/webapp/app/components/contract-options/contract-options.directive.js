(function () {
    'use strict';

    var contractOptions = {
        restrict: 'E',
        bindings: {
            contract: '='
        },
        templateUrl: 'app/components/contract-options/contract-options.html',
        controller: ContractOptionsController,
        controllerAs: 'vm'
    };

    angular
        .module('royalhallsApp')
        .component('contractOptions', contractOptions);

    ContractOptionsController.$inject = ['$rootScope', 'ContractOption'];

    function ContractOptionsController($rootScope, ContractOption) {
        var vm = this;
        vm.options = [];
        vm.totalAmount = 0;
        vm.openAmount = 0;

        var unsubscribe = $rootScope.$on('royalhallsApp:contractUpdate', function () {
            update();
        });

        this.$onDestroy = function () {
            if (unsubscribe) {
                unsubscribe();
            }
        };

        update();

        function update() {
            if (vm.contract) {
                ContractOption.query({
                    id: vm.contract.id
                }, function (entity) {
                    vm.events = entity;
                    vm.totalAmount = 0;

                    if (vm.events) {
                        vm.options =
                            vm.events.map(function (ev) {
                                var options =
                                    ev.options.map(function (op) {
                                        op.event = {id: ev.id, eventName: ev.eventName};
                                        op.price = (op.variant === null) ? op.option.price : op.variant.price;
                                        op.total = (op.optionQty === null) ? op.price : op.optionQty * op.price;

                                        vm.totalAmount = vm.totalAmount + op.total;
                                        return op;
                                    });

                                return options;
                            });

                        //flatten array
                        vm.options = [].concat.apply([], vm.options);
                    }

                    if (vm.contract.payments) {
                        vm.paidAmount = 0;
                        vm.contract.payments.map(function (payment) {
                            if (payment.paymentStatus === 'PAID') {
                                vm.paidAmount = vm.paidAmount + payment.paymentAmount;
                            }
                        });

                        vm.openAmount = vm.totalAmount - vm.paidAmount;
                    }
                });
            }
        }

    }
})();
