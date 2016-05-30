(function() {
    'use strict';

    angular
        .module('streamerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('streams', {
            parent: 'entity',
            url: '/streams?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Streams'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/streams/streams.html',
                    controller: 'StreamsController',
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
                }]
            }
        })
        .state('streams-detail', {
            parent: 'entity',
            url: '/streams/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Streams'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/streams/streams-detail.html',
                    controller: 'StreamsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Streams', function($stateParams, Streams) {
                    return Streams.get({id : $stateParams.id});
                }]
            }
        })
        .state('streams.new', {
            parent: 'streams',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/streams/streams-dialog.html',
                    controller: 'StreamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                puburl: null,
                                pubkey: null,
                                privatekey: null,
                                deletekey: null,
                                format: null,
                                example: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('streams', null, { reload: true });
                }, function() {
                    $state.go('streams');
                });
            }]
        })
        .state('streams.edit', {
            parent: 'streams',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/streams/streams-dialog.html',
                    controller: 'StreamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Streams', function(Streams) {
                            return Streams.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('streams', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('streams.delete', {
            parent: 'streams',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/streams/streams-delete-dialog.html',
                    controller: 'StreamsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Streams', function(Streams) {
                            return Streams.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('streams', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
