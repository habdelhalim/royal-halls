(function () {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('ExtraOption', ExtraOption);

    ExtraOption.$inject = ['$resource'];

    function ExtraOption($resource) {
        var resourceUrl = 'api/extra-options/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'queryByType': {url: 'api/extra-options/by-type/:type', method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
