(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionDetailController', ExtraOptionDetailController);

    ExtraOptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExtraOption'];

    function ExtraOptionDetailController($scope, $rootScope, $stateParams, previousState, entity, ExtraOption) {
        var vm = this;

        vm.extraOption = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:extraOptionUpdate', function(event, result) {
            vm.extraOption = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
