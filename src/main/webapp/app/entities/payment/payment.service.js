(function() {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('Payment', Payment);

    Payment.$inject = ['$resource'];

    function Payment ($resource) {
        var resourceUrl =  'api/payments/:id';

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
