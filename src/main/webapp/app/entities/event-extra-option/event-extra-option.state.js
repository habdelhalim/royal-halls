(function() {
    'use strict';

    angular
        .module('royalhallsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('event-extra-option', {
            parent: 'entity',
            url: '/event-extra-option?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.eventExtraOption.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event-extra-option/event-extra-options.html',
                    controller: 'EventExtraOptionController',
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
                    $translatePartialLoader.addPart('eventExtraOption');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('event-extra-option-detail', {
            parent: 'entity',
            url: '/event-extra-option/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'royalhallsApp.eventExtraOption.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event-extra-option/event-extra-option-detail.html',
                    controller: 'EventExtraOptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('eventExtraOption');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EventExtraOption', function($stateParams, EventExtraOption) {
                    return EventExtraOption.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'event-extra-option',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('event-extra-option-detail.edit', {
            parent: 'event-extra-option-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-extra-option/event-extra-option-dialog.html',
                    controller: 'EventExtraOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EventExtraOption', function(EventExtraOption) {
                            return EventExtraOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-extra-option.new', {
            parent: 'event-extra-option',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-extra-option/event-extra-option-dialog.html',
                    controller: 'EventExtraOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                optionQty: null,
                                optionNotes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('event-extra-option', null, { reload: 'event-extra-option' });
                }, function() {
                    $state.go('event-extra-option');
                });
            }]
        })
        .state('event-extra-option.edit', {
            parent: 'event-extra-option',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-extra-option/event-extra-option-dialog.html',
                    controller: 'EventExtraOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EventExtraOption', function(EventExtraOption) {
                            return EventExtraOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-extra-option', null, { reload: 'event-extra-option' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-extra-option.delete', {
            parent: 'event-extra-option',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-extra-option/event-extra-option-delete-dialog.html',
                    controller: 'EventExtraOptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EventExtraOption', function(EventExtraOption) {
                            return EventExtraOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-extra-option', null, { reload: 'event-extra-option' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
