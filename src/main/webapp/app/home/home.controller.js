(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$rootScope', '$window','Principal', 'LoginService', '$state',
        'entity', 'Contract', 'Customer'];

    function HomeController($scope, $rootScope, $window, Principal, LoginService, $state, entity, Contract, Customer) {
        var vm = this;

        vm.account = null;
        vm.contract = entity;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.createContract = createContract;
        vm.searchContract = searchContract;
        vm.save = save;
        vm.newEvent = newEvent;
        vm.newPayment = newPayment;
        vm.findCustomer = findCustomer;
        vm.print = printPage;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        var unsubscribe = $rootScope.$on('royalhallsApp:contractUpdate', function () {
            if (vm.contract) {
                Contract.get({
                    id: vm.contract.id
                }, function (entity) {
                    vm.contract = entity;
                });
            }
        });

        $scope.$on('$destroy', function () {
            if (unsubscribe) {
                unsubscribe();
            }
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            $state.go('register');
        }

        function createContract() {
            $state.go('home.new');
        }

        function searchContract() {
            $state.go('home.search');
        }

        function save() {
            vm.isSaving = true;
            if (vm.contract.id !== null) {
                Contract.update(vm.contract, onSaveSuccess, onSaveError);
            } else {
                Contract.save(vm.contract, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('royalhallsApp:contractUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function newEvent() {
            vm.save();
            $state.go('event-new');
        }

        function newPayment() {
            vm.save();
            $state.go('payment-new');
        }

        function findCustomer(search) {
            if (search.length >= 3 || search.length == 0) {
                return Customer.search({
                    page: 0,
                    size: 10,
                    search: search
                }).$promise.then(function (response) {
                    if (response.length < 1) {
                        response.push({'customerName': search});
                    }
                    return response;
                });

            }
        }

        function printPage() {
            $window.print();
        }
    }
})();
