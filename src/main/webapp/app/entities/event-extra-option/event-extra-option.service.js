(function() {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('EventExtraOption', EventExtraOption);

    EventExtraOption.$inject = ['$resource'];

    function EventExtraOption ($resource) {
        var resourceUrl =  'api/event-extra-options/:id';

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
