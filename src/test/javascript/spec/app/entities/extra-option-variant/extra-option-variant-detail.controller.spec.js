'use strict';

describe('Controller Tests', function() {

    describe('ExtraOptionVariant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExtraOptionVariant, MockExtraOption;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExtraOptionVariant = jasmine.createSpy('MockExtraOptionVariant');
            MockExtraOption = jasmine.createSpy('MockExtraOption');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ExtraOptionVariant': MockExtraOptionVariant,
                'ExtraOption': MockExtraOption
            };
            createController = function() {
                $injector.get('$controller')("ExtraOptionVariantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'royalhallsApp:extraOptionVariantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
