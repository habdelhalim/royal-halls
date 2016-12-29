(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventExtraOptionDeleteController',EventExtraOptionDeleteController);

    EventExtraOptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'EventExtraOption'];

    function EventExtraOptionDeleteController($uibModalInstance, entity, EventExtraOption) {
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
                    $uibModalInstance.close(true);
                });
        }
    }
})();
