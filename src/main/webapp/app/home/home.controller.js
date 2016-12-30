(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$rootScope', 'Principal', 'LoginService', '$state', 'entity', 'Contract'];

    function HomeController($scope, $rootScope, Principal, LoginService, $state, entity, Contract) {
        var vm = this;

        vm.account = null;
        vm.contract = entity;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.createContract = createContract;
        vm.searchContract = searchContract;
        vm.save = save;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        var updateListener = $rootScope.$on('royalhallsApp:contractUpdate', function() {
            if (vm.contract) {
                Contract.get({
                    id: vm.contract.id
                }, function(entity) {
                    vm.contract = entity;
                });
            }
        });

        $scope.$on('$destroy', function() {
            console.log('calling destroy');
            updateListener();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
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
    }
})();
