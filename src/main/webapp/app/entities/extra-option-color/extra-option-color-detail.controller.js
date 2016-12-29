(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionColorDetailController', ExtraOptionColorDetailController);

    ExtraOptionColorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExtraOptionColor', 'ExtraOption'];

    function ExtraOptionColorDetailController($scope, $rootScope, $stateParams, previousState, entity, ExtraOptionColor, ExtraOption) {
        var vm = this;

        vm.extraOptionColor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:extraOptionColorUpdate', function(event, result) {
            vm.extraOptionColor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
