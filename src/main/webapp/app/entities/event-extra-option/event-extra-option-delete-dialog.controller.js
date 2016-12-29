(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventExtraOptionDeleteController',EventExtraOptionDeleteController);

    EventExtraOptionDeleteController.$inject = ['$uibModalInstance', '$scope', 'entity', 'EventExtraOption'];

    function EventExtraOptionDeleteController($uibModalInstance, $scope, entity, EventExtraOption) {
        var vm = this;

        vm.eventExtraOption = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EventExtraOption.delete({id: id},
                function () {
                    $scope.$emit('royalhallsApp:eventExtraOptionUpdate',{});
                    $uibModalInstance.close(true);
                });
        }
    }
})();
