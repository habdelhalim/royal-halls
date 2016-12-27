(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('schedule', {
                parent: 'app',
                url: '/schedule',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/schedule/schedule.html',
                        controller: 'HomeController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('home');
                        $translatePartialLoader.addPart('contact');
                        $translatePartialLoader.addPart('customer');
                        $translatePartialLoader.addPart('event');
                        $translatePartialLoader.addPart('payment');
                        $translatePartialLoader.addPart('paymentStatus');
                        $translatePartialLoader.addPart('paymentType');
                        $translatePartialLoader.addPart('contract');
                        $translatePartialLoader.addPart('contractStatus');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {
                            contractDate: null,
                            contractStatus: null,
                            contractNotes: null,
                            totalAmount: null,
                            openAmount: null,
                            creationDate: null,
                            id: null
                        };
                    }
                }
            });
    }
})();
