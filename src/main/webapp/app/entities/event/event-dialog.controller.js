(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$timeout', '$scope', '$rootScope', '$filter', '$stateParams',
        '$uibModalInstance', 'entity', 'Event', 'ExtraOption', 'EventExtraOption', 'EventType', 'Hall', 'Contract'];

    function EventDialogController($timeout, $scope, $rootScope, $filter, $stateParams,
                                   $uibModalInstance, entity, Event, ExtraOption, EventExtraOption, EventType, Hall, Contract) {
        var vm = this;

        vm.event = entity;
        vm.datePickerOpenStatus = {};
        vm.eventtypes = EventType.query();
        vm.halls = Hall.query();
        vm.clear = clear;
        vm.save = save;
        vm.setEndDate = setEndDate;
        vm.copyStartDate = copyStartDate;
        vm.addBasicOption = addBasicOption;

        vm.basicOptionFilter = {
            option: {
                optionType: 'BASIC'
            }
        };
        vm.secondaryOptionFilter = {
            option: {
                optionType: 'OPTIONAL'
            }
        };

        ExtraOption.queryByType({type: 'BASIC'}, highlightSelected);

        var unsubscribe = $rootScope.$on('royalhallsApp:eventExtraOptionUpdate', function () {
            if (vm.event.id !== null) {
                var contractId = vm.event.contract.id;
                Event.get({
                    id: vm.event.id
                }, function (entity) {
                    vm.event = entity;
                    vm.event.contract = {
                        id: contractId
                    }
                });
            }
        });

        //cleanup rootScope listener
        $scope.$on('$destroy', function () {
            if (unsubscribe) {
                unsubscribe();
            }
        });

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function addBasicOption() {
            var secondaryOptions = $filter('filter')(vm.event.options, {option: {optionType: 'OPTIONAL'}}) || [];
            var basicOptions = $filter('filter')(vm.event.options, {option: {optionType: 'BASIC'}}) || [];

            var selectedBasicOption = $filter('filter')(vm.basicOptions, {checked: true});

            basicOptions = basicOptions.filter(function (basicOpt) {
                var found = false;
                selectedBasicOption.forEach(function (selectOpt) {
                    if (basicOpt.option.id === selectOpt.id)
                        found = true;

                });

                return found;
            });

            selectedBasicOption.forEach(function (selectOpt) {
                var found = false;
                basicOptions.forEach(function (basicOpt) {
                    if (basicOpt.option.id === selectOpt.id)
                        found = true;
                });

                if (!found) {
                    basicOptions.push({
                        event: 1,
                        option: selectOpt
                    })
                }
            });

            vm.event.options = [].concat([], basicOptions).concat([], secondaryOptions);
        }

        function highlightSelected(result) {
            vm.basicOptions = result.map(function (element) {
                if (vm.event.options !== undefined) {
                    vm.event.options.forEach(function (selected) {
                        if (selected.option.id === element.id) {
                            element.checked = true;
                        }
                    });
                }

                return element;
            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('royalhallsApp:eventUpdate', result);
            $scope.$emit('royalhallsApp:contractUpdate', result);

            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function setEndDate() {
            var hours = 2;
            vm.event.eventEndDate = angular.copy(vm.event.eventStartDate);
            vm.event.eventEndDate.setTime(vm.event.eventEndDate.getTime() + (hours * 60 * 60 * 1000));
        }

        function copyStartDate() {
            vm.event.eventEndDate.setDate(vm.event.eventStartDate.getDate());
            vm.event.eventEndDate.setMonth(vm.event.eventStartDate.getMonth());
            vm.event.eventEndDate.setFullYear(vm.event.eventStartDate.getFullYear());
        }
    }
})();
