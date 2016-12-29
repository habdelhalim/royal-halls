(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('extra-option-color', {
            parent: 'entity',
            url: '/extra-option-color?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.extraOptionColor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-option-color/extra-option-colors.html',
                    controller: 'ExtraOptionColorController',
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
                    $translatePartialLoader.addPart('extraOptionColor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('extra-option-color-detail', {
            parent: 'entity',
            url: '/extra-option-color/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.extraOptionColor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-option-color/extra-option-color-detail.html',
                    controller: 'ExtraOptionColorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('extraOptionColor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExtraOptionColor', function($stateParams, ExtraOptionColor) {
                    return ExtraOptionColor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'extra-option-color',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('extra-option-color-detail.edit', {
            parent: 'extra-option-color-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-color/extra-option-color-dialog.html',
                    controller: 'ExtraOptionColorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtraOptionColor', function(ExtraOptionColor) {
                            return ExtraOptionColor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extra-option-color.new', {
            parent: 'extra-option-color',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-color/extra-option-color-dialog.html',
                    controller: 'ExtraOptionColorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                colorName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('extra-option-color', null, { reload: 'extra-option-color' });
                }, function() {
                    $state.go('extra-option-color');
                });
            }]
        })
        .state('extra-option-color.edit', {
            parent: 'extra-option-color',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-color/extra-option-color-dialog.html',
                    controller: 'ExtraOptionColorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtraOptionColor', function(ExtraOptionColor) {
                            return ExtraOptionColor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-option-color', null, { reload: 'extra-option-color' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extra-option-color.delete', {
            parent: 'extra-option-color',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-option-color/extra-option-color-delete-dialog.html',
                    controller: 'ExtraOptionColorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExtraOptionColor', function(ExtraOptionColor) {
                            return ExtraOptionColor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-option-color', null, { reload: 'extra-option-color' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
