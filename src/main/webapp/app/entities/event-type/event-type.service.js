(function() {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('EventType', EventType);

    EventType.$inject = ['$resource'];

    function EventType ($resource) {
        var resourceUrl =  'api/event-types/:id';

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
