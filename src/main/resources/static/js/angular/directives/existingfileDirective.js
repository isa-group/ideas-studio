(function(angular) {
  'use strict';
angular.module('mainApp').directive('existingfile', function ($q, $http) {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$asyncValidators.existingfile = function (modelValue, viewValue) {

                if (ctrl.$isEmpty(modelValue)) {
                    // consider empty model invalid
                    return $q.reject();
                } else {
                    return $http.get('files/get/'+selectedWorkspace+'/'+modelValue, {})
                            .then(
                                    function (response) {
                                        if (response.status===404 || response.data === "") {
                                            return $q.reject();
                                            //Server will give me a  notify if it exist or not. I will throw a error If it exist with "reject"
                                        }
                                        return $q.resolve();
                                    }
                            );
                }
            };
        }
    };
});
})(window.angular);

