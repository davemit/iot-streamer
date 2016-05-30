(function() {
    'use strict';

    angular
        .module('streamerApp')
        .controller('StreamsDetailController', StreamsDetailController);

    StreamsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Streams', 'User'];

    function StreamsDetailController($scope, $rootScope, $stateParams, entity, Streams, User) {
        var vm = this;
        vm.streams = entity;
        
        var unsubscribe = $rootScope.$on('streamerApp:streamsUpdate', function(event, result) {
            vm.streams = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
