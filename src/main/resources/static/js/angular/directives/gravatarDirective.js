(function (angular) {
    'use strict';
    angular.module('mainApp').directive('gravatar', function ($compile) {

        var defaultGravatarUrl = "";
        var regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

        function getGravatarUrl(email) {
            if (!regex.test(email))
                return defaultGravatarUrl;

            return 'http://www.gravatar.com/avatar/' + $.md5(email);
        }

        function linker(scope, element, attrs) {
            if (scope.email) {
                scope.url = getGravatarUrl(scope.email);
                scope.hash = $.md5(scope.email);
                if (!scope.size)
                    scope.size = 40;
                var template = '<a href="http://www.gravatar.com/{{hash}}" target="_blank"><img ng-src="{{url}}.jpg?s={{size}}"></img></a>';
                $compile(template)(scope, function (cloned, scope) {
                    element.replaceWith(cloned);
                });
                
                scope.$watch('email', function (newVal, oldVal) {
                    if (newVal !== oldVal) {
                        scope.url = getGravatarUrl(scope.email);
                        scope.hash = $.md5(scope.email);
                    }
                });
            }
        }

        return {
            restrict: 'E',            
            transclude: true,
            require: '?email',
            scope: {
                email: '=', size: '='
            },
            link: function (scope, element, attrs) {
                linker(scope, element, attrs);
            }
        };

    });
})(window.angular);

