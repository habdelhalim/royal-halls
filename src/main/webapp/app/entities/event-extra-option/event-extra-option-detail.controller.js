(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventExtraOptionDetailController', EventExtraOptionDetailController);

    EventExtraOptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EventExtraOption', 'Event', 'ExtraOption', 'ExtraOptionVariant', 'ExtraOptionColor'];

    function EventExtraOptionDetailController($scope, $rootScope, $stateParams, previousState, entity, EventExtraOption, Event, ExtraOption, ExtraOptionVariant, ExtraOptionColor) {
        var vm = this;

        vm.eventExtraOption = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:eventExtraOptionUpdate', function(event, result) {
            vm.eventExtraOption = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
