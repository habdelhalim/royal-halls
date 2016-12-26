(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('contact');
                    $translatePartialLoader.addPart('customer');
                    $translatePartialLoader.addPart('event');
                    $translatePartialLoader.addPart('payment');
                    $translatePartialLoader.addPart('contract');
                    $translatePartialLoader.addPart('contractStatus');
                    return $translate.refresh();
                }],
                entity: function () {
                    return {
                        contractName: null,
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
