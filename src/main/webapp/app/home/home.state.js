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
                        return null
                    }
                }
            })
            .state('home.new', {
                parent: 'app',
                url: '/new',
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
                    entity: ['$stateParams', 'Contract', '$state', function ($stateParams, Contract, $state) {
                        var contract = {
                            contractDate: new Date(),
                            contractStatus: "CREATED",
                            contractNotes: null,
                            totalAmount: null,
                            openAmount: null,
                            creationDate: new Date(),
                            id: null
                        };

                        return Contract.save(contract, function (result) {
                            $state.go('home.edit', {id: result.id});
                        }).$promise;
                    }]
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
            .state('home.edit.event', {
                parent: 'home.edit',
                url: '/event',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/event/event-dialog.html',
                        controller: 'EventDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    contract: {id: $stateParams.id},
                                    eventName: null,
                                    eventStartDate: null,
                                    eventEndDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('home.edit', {id: $stateParams.id}, {reload: 'home.edit'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('home.edit.event-edit', {
                parent: 'home.edit',
                url: '/event/update/{eventId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/event/event-dialog.html',
                        controller: 'EventDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Event', function (Event) {
                                var promise = Event.get({id: $stateParams.eventId}).$promise;

                                return promise.then(function(event){
                                    event.contract = {id: $stateParams.id};
                                    return event;
                                });
                            }]
                        }
                    }).result.then(function () {
                        $state.go('home.edit', {id: $stateParams.id}, {reload: 'home.edit'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('home.edit.event-delete', {
                parent: 'home.edit',
                url: '/event/delete/{eventId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/event/event-delete-dialog.html',
                        controller: 'EventDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Event', function (Event) {
                                return Event.get({id: $stateParams.eventId}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('home.edit', {id: $stateParams.id}, {reload: 'home.edit'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('home.edit.payment-new', {
                parent: 'home.edit',
                url: '/payment',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/payment/payment-dialog.html',
                        controller: 'PaymentDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    contract: {id: $stateParams.id},
                                    paymentType: null,
                                    paymentAmount: null,
                                    paymentDueDate: null,
                                    paymentStatus: null,
                                    chequeNo: null,
                                    bankName: null,
                                    creationDate: new Date(),
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('home.edit', {id: $stateParams.id}, {reload: 'home.edit'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('home.edit.payment-edit', {
                parent: 'home.edit',
                url: '/payment/edit/{paymentId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/payment/payment-dialog.html',
                        controller: 'PaymentDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Payment', function (Payment) {
                                var promise = Payment.get({id: $stateParams.paymentId}).$promise;

                                return promise.then(function (payment) {
                                    payment.contract = {id: $stateParams.id};
                                    return payment;
                                });
                            }]
                        }
                    }).result.then(function () {
                        $state.go('home.edit', {id: $stateParams.id}, {reload: 'home.edit'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('home.edit.payment-delete', {
                parent: 'home.edit',
                url: '/payment/delete/{paymentId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/payment/payment-delete-dialog.html',
                        controller: 'PaymentDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Payment', function (Payment) {
                                return Payment.get({id: $stateParams.paymentId}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('home.edit', {id: $stateParams.id}, {reload: 'home.edit'});
                    }, function () {
                        $state.go('^');
                    });
                }]
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
