// AngularJS initialization
var mainApp = angular.module("mainApp", ['ngSanitize', 'ui.router', 'ui.bootstrap.datetimepicker', 'ui.dateTimeInput'])
        .controller("MainCtrl", ["$scope", "$compile", "$q", "$http", "$timeout", "$state", "$location", "$stateParams", function ($scope, $compile, $q, $http, $timeout, $state, $location, $stateParams) {

        $scope.model = {};

        // Update editor content from model
        $scope.$watch(
                function () {
                    return $scope.model;
                },
                function (newValue, oldValue) {

                    if (document.editor && newValue && oldValue && !compareObjects(newValue, oldValue)) {

                        var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat(),
                                formatSessions = EditorManager.sessionsMap[EditorManager.currentUri].getFormatsSessions(),
                                editorCursorPos = document.editor.renderer.scrollBarV.scrollTop,
                                editorRange = document.editor.selection.getRange();

                        if (currentFormat === "json" && ("json" in formatSessions || "yaml" in formatSessions)) {
                            if (newValue !== document.editor.getValue()) {
                                document.editor.setValue(JSON.stringify(newValue, function( key, value ) {
                                    if( key === "$$hashKey" ) {
                                        return undefined;
                                    }

                                    return value;
                                }), -1);
                                // Update editor to previous view
                                document.editor.selection.setRange(editorRange);
                                document.editor.renderer.scrollToY(editorCursorPos);
                            }

                        } else if (currentFormat === "yaml" && ("json" in formatSessions || "yaml" in formatSessions)) {
                            var yaml = jsyaml.safeDump(newValue);
                            if (yaml !== document.editor.getValue()) {
                                document.editor.setValue(yaml, -1);
                                // Update editor to previous view
                                document.editor.selection.setRange(editorRange);
                                document.editor.renderer.scrollToY(editorCursorPos);
                            }

                        } else if ("json" in formatSessions || "yaml" in formatSessions) {

                            var currentUri = EditorManager.currentUri;
                            var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(currentUri));
                            var model = ModeManager.getMode(modelId);
                            var converterUri = ModeManager.getConverter(modelId);
                            if (model.apiVersion >= 2) {
                                converterUri = converterUri.replace("$srcSyntaxId", "json").replace("$destSyntaxId", currentFormat);
                            }

                            // Convert json to currentFormat
                            var promise = $q(function (resolve, reject) {
                                CommandApi.callConverter(model, "json", currentFormat, currentUri, JSON.stringify(newValue, function( key, value ) {
                                            if( key === "$$hashKey" ) {
                                                return undefined;
                                            }

                                            return value;
                                        }), converterUri,
                                        function (result) {
                                            resolve(result.data);
                                        }
                                );
                            });
                            promise.then(function (result) {
                                if (result !== document.editor.getValue()) {
                                    document.editor.setValue(result, -1);
                                    // Update editor to previous view
                                    document.editor.selection.setRange(editorRange);
                                    document.editor.renderer.scrollToY(editorCursorPos);
                                }
                            });

                        }
                    }
                }, true);

        /**
         * Update model from ace editor. 
         * This method is enabled by a hidden input outside of angular environment.
         * @returns {undefined}
         */
        $scope.editorContentToModel = function () {

            // Try-catch block for bad JSON parsing.
            try {

                var sessionMap = EditorManager.sessionsMap[EditorManager.currentUri],
                        currentFormat = sessionMap.getCurrentFormat(),
                        formatSessions = sessionMap.getFormatsSessions(),
                        editorContent = document.editor.getValue();

                if (editorContent && DescriptionInspector.existCurrentAngularFile() && ("json" in formatSessions || "yaml" in formatSessions)) {

                    //TODO: check if it's a "JSONable" content

                    if (currentFormat === "json") {
                        if ($scope.modelString !== "") {
                            $scope.model = JSON.parse(editorContent);
                        }

                    } else if (currentFormat === "yaml") {
                        $scope.model = jsyaml.safeLoad(editorContent);

                    } else {
                        // Convert current format to json

                        var currentUri = EditorManager.currentUri;
                        var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(currentUri));
                        var model = ModeManager.getMode(modelId);
                        var converterUri = ModeManager.getConverter(modelId);
                        if (model.apiVersion >= 2) {
                            converterUri = converterUri.replace("$srcSyntaxId", currentFormat).replace("$destSyntaxId", "json");
                        }

                        var promise = $q(function (resolve, reject) {
                            CommandApi.callConverter(model, currentFormat, "json", EditorManager.currentUri, editorContent, converterUri,
                                    function (result) {
                                        if (result.data) {
                                            resolve(JSON.parse(result.data));
                                        }
                                    }
                            );
                        });
                        promise.then(function (result) {
                            $scope.model = result;
                        });
                    }

                }

            } catch (err) {
                // nothing
                console.error(err);
            }

        };

        // Compile model from model inspector
        $scope.compilationFlag = 0;
        $scope.compileModel = function () {
            if ($scope.compilationFlag == 1) {
                // Compile model tab content
                $compile(angular.element("#editorInspectorLoader .modelInspectorContent")[0])($scope);
            }
        };

        // Compile model from angular format view
        $scope.compilationFlagFormatView = 0;
        $scope.compileModelFormatView = function () {
            if ($scope.compilationFlagFormatView == 1) {
                var selector = "#editorWrapper";
                $compile(angular.element(selector)[0])($scope);
            }
        };

    }])
        .directive("contenteditable", function () {
            return {
                require: "ngModel",
                transclude: true,
                scope: {ngModel: '='},
                link: function (scope, element, attrs, ngModel) {

                    function read() {
                        var data = element.text();
                        if (!isNaN(parseFloat(data)) && isFinite(data)) { // if is number
                            ngModel.$setViewValue(parseFloat(data));
                        } else {
                            ngModel.$setViewValue(data);
                        } 
                    }

                    ngModel.$render = function () {
                        element.html(ngModel.$viewValue || "");
                    };

                    element.bind("blur keyup change", function () {
                        scope.$apply(read);
                    });
                }
            };
        });