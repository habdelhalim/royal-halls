(function() {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('Payment', Payment);

    Payment.$inject = ['$resource', 'DateUtils'];

    function Payment ($resource, DateUtils) {
        var resourceUrl =  'api/payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.paymentDate = DateUtils.convertDateTimeFromServer(data.paymentDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
