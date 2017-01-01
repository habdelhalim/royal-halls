/**
 * Created by hasan on 1/1/17.
 */
(function () {
    "use strict";

    angular
        .module('royalhallsApp')
        .factory('ContractOption', ContractOption);

    ContractOption.$inject = ['$resource'];

    function ContractOption($resource) {
        var resourceUrl = 'api/events/by-contract/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
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
