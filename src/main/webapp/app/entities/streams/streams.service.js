(function() {
    'use strict';
    angular
        .module('streamerApp')
        .factory('Streams', Streams);

    Streams.$inject = ['$resource'];

    function Streams ($resource) {
        var resourceUrl =  'api/streams/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
