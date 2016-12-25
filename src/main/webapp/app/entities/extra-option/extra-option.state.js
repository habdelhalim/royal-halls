(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('extra-option', {
            parent: 'entity',
            url: '/extra-option?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.extraOption.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-option/extra-options.html',
                    controller: 'ExtraOptionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('extraOption');
                    $translatePartialLoader.addPart('optionType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('extra-option-detail', {
            parent: 'entity',
            url: '/extra-option/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.extraOption.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-option/extra-option-detail.html',
                    controller: 'ExtraOptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('extraOption');
                    $translatePartialLoader.addPart('optionType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExtraOption', function($stateParams, ExtraOption) {
                    return ExtraOption.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'extra-option',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('extra-option-detail.edit', {
            parent: 'extra-option-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option/extra-option-dialog.html',
                    controller: 'ExtraOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtraOption', function(ExtraOption) {
                            return ExtraOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extra-option.new', {
            parent: 'extra-option',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option/extra-option-dialog.html',
                    controller: 'ExtraOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                optionName: null,
                                optionType: null,
                                optionQty: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('extra-option', null, { reload: 'extra-option' });
                }, function() {
                    $state.go('extra-option');
                });
            }]
        })
        .state('extra-option.edit', {
            parent: 'extra-option',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option/extra-option-dialog.html',
                    controller: 'ExtraOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtraOption', function(ExtraOption) {
                            return ExtraOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-option', null, { reload: 'extra-option' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extra-option.delete', {
            parent: 'extra-option',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option/extra-option-delete-dialog.html',
                    controller: 'ExtraOptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExtraOption', function(ExtraOption) {
                            return ExtraOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-option', null, { reload: 'extra-option' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
