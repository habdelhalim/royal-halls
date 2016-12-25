(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventTypeDetailController', EventTypeDetailController);

    EventTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EventType'];

    function EventTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, EventType) {
        var vm = this;

        vm.eventType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('royalhallsApp:eventTypeUpdate', function(event, result) {
            vm.eventType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
