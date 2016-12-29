(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'entity', 'Contract'];

    function HomeController ($scope, Principal, LoginService, $state, entity, Contract) {
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

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        function createContract(){
            $state.go('home.new');
        }
        function searchContract(){
            $state.go('home.search');
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
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
