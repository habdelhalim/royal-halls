/**
 * Created by hasan on 4/8/17.
 */
(function () {
    "use strict";

    angular.module('royalhallsApp').controller('ContractPrintController', ContractPrintController);

    ContractPrintController.$inject = ['$scope', 'entity'];
    function ContractPrintController($scope, entity) {
        var vm = this;
        vm.contract = entity;

    }
})();
