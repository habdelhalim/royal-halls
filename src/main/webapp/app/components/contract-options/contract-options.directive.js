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

                    if (vm.events) {
                        vm.options =
                            vm.events.map(function (ev) {
                                var options =
                                    ev.options.map(function (op) {
                                        op.event = {id: ev.id, eventName: ev.eventName};
                                        return op;
                                    });

                                return options;
                            });

                        //flatten array
                        vm.options = [].concat.apply([], vm.options);
                    }
                });
            }
        }

    }
})();
