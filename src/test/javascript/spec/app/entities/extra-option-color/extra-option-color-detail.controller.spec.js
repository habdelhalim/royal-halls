'use strict';

describe('Controller Tests', function() {

    describe('ExtraOptionColor Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExtraOptionColor, MockExtraOption;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExtraOptionColor = jasmine.createSpy('MockExtraOptionColor');
            MockExtraOption = jasmine.createSpy('MockExtraOption');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ExtraOptionColor': MockExtraOptionColor,
                'ExtraOption': MockExtraOption
            };
            createController = function() {
                $injector.get('$controller')("ExtraOptionColorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'royalhallsApp:extraOptionColorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
