angular.module('mainApp')
        .directive('existingFile', function ($q, $timeout) {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {

                    ctrl.$asyncValidators.existingFile = function (modelValue, viewValue) {

                        if (ctrl.$isEmpty(modelValue)) {
                            return $q.reject();
                        }

                        var def = $q.defer();
                        var uri="files/get/" + selectedWorkspace + "/"+modelValue;
                        $.ajax(uri, {
                            "type": "GET",
                            "success": function (result) {
                                if(!""===result)
                                    def.resolve();
                                else
                                    def.reject();
                            },
                            "error": function (result) {
                                console.error(result);
                                def.reject();
                            },
                            "async": true
                        });

                        return def.promise;
                    };
                }
            };
        });


