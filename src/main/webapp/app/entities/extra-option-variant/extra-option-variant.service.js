(function () {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('ExtraOptionVariant', ExtraOptionVariant);

    ExtraOptionVariant.$inject = ['$resource'];

    function ExtraOptionVariant($resource) {
        var resourceUrl = 'api/extra-option-variants/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'queryByOption': {url: 'api/extra-option-variants/by-option/:optionId', method: 'GET', isArray: true},
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
