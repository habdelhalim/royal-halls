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
                entity: ['Contract', function(Contract) {
                    return Contract.get({id : 1}).$promise;
                }]
            }
        });
    }
})();
