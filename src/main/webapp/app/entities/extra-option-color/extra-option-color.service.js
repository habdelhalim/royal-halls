(function() {
    'use strict';
    angular
        .module('royalhallsApp')
        .factory('ExtraOptionColor', ExtraOptionColor);

    ExtraOptionColor.$inject = ['$resource'];

    function ExtraOptionColor ($resource) {
        var resourceUrl =  'api/extra-option-colors/:id';

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
