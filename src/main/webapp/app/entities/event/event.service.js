(function () {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('Event', Event);

    Event.$inject = ['$resource', 'DateUtils'];

    function Event($resource, DateUtils) {
        var resourceUrl = 'api/events/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET', isArray: true, transformResponse: function (data) {
                    if (data) {
                        data = JSOG.parse(data);
                    }
                    return data;
                }
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = JSOG.parse(data);
                        data.eventStartDate = DateUtils.convertDateTimeFromServer(data.eventStartDate);
                        data.eventEndDate = DateUtils.convertDateTimeFromServer(data.eventEndDate);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
