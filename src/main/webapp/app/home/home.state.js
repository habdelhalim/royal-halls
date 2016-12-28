(function () {
    'use strict';

    angular
        .module('royalhallsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('home', {
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
            })
            .state('home.new', {
                parent: 'app',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/home/home.html',
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
            })
            .state('home.edit', {
                parent: 'app',
                url: '/edit/{id}',
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
                    entity: ['$stateParams', 'Contract', function ($stateParams, Contract) {
                        return Contract.get({id: $stateParams.id}).$promise;
                    }]
                }
            })
            .state('home.search', {
                parent: 'app',
                url: '/search',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/contract/contract-search.html',
                        controller: 'ContractSearchController',
                        controllerAs: 'vm',
                        size: 'lg',
                        resolve: {
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
                    }).result.then(function (selected) {
                        $state.go('home.edit', {id: selected});
                    }, function () {
                    });
                }]
            });
    }
})();
