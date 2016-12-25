(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('HallTypeDetailController', HallTypeDetailController);

    HallTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HallType'];

    function HallTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, HallType) {
        var vm = this;

        vm.hallType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:hallTypeUpdate', function(event, result) {
            vm.hallType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
