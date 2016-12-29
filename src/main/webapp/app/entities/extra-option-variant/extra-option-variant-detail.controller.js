(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('ExtraOptionVariantDetailController', ExtraOptionVariantDetailController);

    ExtraOptionVariantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExtraOptionVariant', 'ExtraOption'];

    function ExtraOptionVariantDetailController($scope, $rootScope, $stateParams, previousState, entity, ExtraOptionVariant, ExtraOption) {
        var vm = this;

        vm.extraOptionVariant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:extraOptionVariantUpdate', function(event, result) {
            vm.extraOptionVariant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
