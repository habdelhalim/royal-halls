(function() {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('Hall', Hall);

    Hall.$inject = ['$resource'];

    function Hall ($resource) {
        var resourceUrl =  'api/halls/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
