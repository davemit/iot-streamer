(function() {
    'use strict';

    angular
        .module('streamerApp')
        .controller('StreamsDialogController', StreamsDialogController);

    StreamsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Streams', 'User', '$http'];

    function StreamsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Streams, User, $http) {
        var vm = this;
        vm.streams = entity;
        //Test code goes here

        var test_stream =
      "title='itv'%2C&description='this+should+be+deleted+by+the+test'%2C&fields=%5B'test1'%2C+'test2'%5D%2C&tags=%5B'input+test'%5D%2C&hidden=false";


       $http.post("//158.85.106.208:8080/streams.json", test_stream).success(function(data, status) {
           console.log(data);
       })

        //ends here
        console.log(vm.streams);
        vm.users = User.query();
        console.log(vm.users);
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            console.log(vm.streams);
            $scope.$emit('streamerApp:streamsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.streams.id !== null) {
                Streams.update(vm.streams, onSaveSuccess, onSaveError);
            } else {
                Streams.save(vm.streams, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
