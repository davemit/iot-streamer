(function() {
    'use strict';

    angular
        .module('streamerApp')
        .controller('StreamsDeleteController',StreamsDeleteController);

    StreamsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Streams'];

    function StreamsDeleteController($uibModalInstance, entity, Streams) {
        var vm = this;
        vm.streams = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Streams.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
