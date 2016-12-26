(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'entity'];

    function HomeController ($scope, Principal, LoginService, $state, entity) {
        var vm = this;

        vm.account = null;
        vm.contract = entity;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.createContract = createContract;
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
            console.log('calling create contract');
            $state.go('home.new');
        }
    }
})();
