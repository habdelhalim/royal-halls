(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('extra-option-variant', {
            parent: 'entity',
            url: '/extra-option-variant?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.extraOptionVariant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-option-variant/extra-option-variants.html',
                    controller: 'ExtraOptionVariantController',
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
                    $translatePartialLoader.addPart('extraOptionVariant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('extra-option-variant-detail', {
            parent: 'entity',
            url: '/extra-option-variant/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.extraOptionVariant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-option-variant/extra-option-variant-detail.html',
                    controller: 'ExtraOptionVariantDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('extraOptionVariant');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExtraOptionVariant', function($stateParams, ExtraOptionVariant) {
                    return ExtraOptionVariant.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'extra-option-variant',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('extra-option-variant-detail.edit', {
            parent: 'extra-option-variant-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-variant/extra-option-variant-dialog.html',
                    controller: 'ExtraOptionVariantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtraOptionVariant', function(ExtraOptionVariant) {
                            return ExtraOptionVariant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extra-option-variant.new', {
            parent: 'extra-option-variant',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-variant/extra-option-variant-dialog.html',
                    controller: 'ExtraOptionVariantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                variantName: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('extra-option-variant', null, { reload: 'extra-option-variant' });
                }, function() {
                    $state.go('extra-option-variant');
                });
            }]
        })
        .state('extra-option-variant.edit', {
            parent: 'extra-option-variant',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-variant/extra-option-variant-dialog.html',
                    controller: 'ExtraOptionVariantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtraOptionVariant', function(ExtraOptionVariant) {
                            return ExtraOptionVariant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-option-variant', null, { reload: 'extra-option-variant' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extra-option-variant.delete', {
            parent: 'extra-option-variant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-variant/extra-option-variant-delete-dialog.html',
                    controller: 'ExtraOptionVariantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExtraOptionVariant', function(ExtraOptionVariant) {
                            return ExtraOptionVariant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-option-variant', null, { reload: 'extra-option-variant' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
