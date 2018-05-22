(function(angular) {
  'use strict';
angular.module('mainApp').directive('gravatar', function () {
  
  var defaultGravatarUrl = "";
  var regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  
  function getGravatarUrl(email,s) {
    if (!regex.test(email))
      return defaultGravatarUrl;    
    
    return 'http://www.gravatar.com/avatar/' + $.md5(email);
  }
  
  function linker(scope) {
    scope.url = getGravatarUrl(scope.email,scope.s);
    scope.hash= $.md5(scope.email);
    if(!scope.s)
      scope.s=40;
    scope.$watch('email', function (newVal, oldVal) {
      if (newVal !== oldVal) {
        scope.url = getGravatarUrl(scope.email);
      }
    });
  }
  
  return {
    template: '<a href="http://www.gravatar.com/{{hash}}" target="_blank"><img ng-src="{{url}}.jpg?s={{s}}"></img></a>',
    restrict: 'EA',
    replace: true,
    scope: {
      email: '='
    },
    link: linker
  };
  
});
})(window.angular);

