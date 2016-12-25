(function() {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('HallType', HallType);

    HallType.$inject = ['$resource'];

    function HallType ($resource) {
        var resourceUrl =  'api/hall-types/:id';

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
