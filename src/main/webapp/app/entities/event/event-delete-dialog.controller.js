(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventDeleteController', EventDeleteController);

    EventDeleteController.$inject = ['$uibModalInstance', '$scope', 'entity', 'Event'];

    function EventDeleteController($uibModalInstance, $scope, entity, Event) {
        var vm = this;

        vm.event = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            Event.delete({
                    id: id
                },
                function() {
                    $scope.$emit('royalhallsApp:eventUpdate', {});
                    $scope.$emit('royalhallsApp:contractUpdate', {});
                    $uibModalInstance.close(true);
                });
        }
    }
})();
