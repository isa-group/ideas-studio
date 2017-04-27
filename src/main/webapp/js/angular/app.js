// AngularJS initialization
var mainApp = angular.module("mainApp", ['ngSanitize', 'ui.router', 'ui.bootstrap.datetimepicker', 'ui.dateTimeInput', 'googlechart', 'chart.js'])
        .controller("MainCtrl", ["$scope", "$compile", "$q", "$http", "$timeout", "$state", "$location", "$stateParams", function ($scope, $compile, $q, $http, $timeout, $state, $location, $stateParams) {

                $scope.model = {};

                $scope.$timeout = $timeout;

                // Update editor content from model
                $scope.$watch(
                        function () {
                            return $scope.model;
                        },
                        function (newValue, oldValue) {
                            var nodeName = '';
                            var tabName = '';
                            try {
                                if (EditorManager.currentUri !== "") {
                                    nodeName = currentSelectedNode.data.title;
                                    tabName = EditorManager.tabsMap[EditorManager.currentUri].text();
                                }
                            } catch (err) {
                            }

                            if (document.editor && (newValue && (oldValue || oldValue === "") || ((newValue || newValue === "") && oldValue)) && !compareObjects(newValue, oldValue) && nodeName === tabName) {

                                var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat(),
                                        formatSessions = EditorManager.sessionsMap[EditorManager.currentUri].getFormatsSessions(),
                                        editorCursorPos = document.editor.renderer.scrollBarV.scrollTop,
                                        editorRange = document.editor.selection.getRange();

                                if (currentFormat === "json" && ("json" in formatSessions || "yaml" in formatSessions)) {
                                    if (newValue !== document.editor.getValue()) {
                                        document.editor.setValue(JSON.stringify(newValue, function (key, value) {
                                            if (key === "$$hashKey") {
                                                return undefined;
                                            }

                                            return value;
                                        }, 2), -1);
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
                                        CommandApi.callConverter(model, "json", currentFormat, currentUri, JSON.stringify(newValue, function (key, value) {
                                            if (key === "$$hashKey") {
                                                return undefined;
                                            }

                                            return value;
                                        }, 2), converterUri,
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

                $scope.WinPrint = null;
                var print = function (styleData) {
                    var prtContent = document.getElementById("modelBoardContent");
                    $scope.WinPrint = 1;

                    showModal("Print view is enabled", "Please, close print window before using " + studioConfiguration.workbenchName + " workbench.",
                            "Ok", closeWinPrint,
                            closeWinPrint, closeWinPrint);

                    $scope.$apply();
                    $scope.WinPrint = window.open('', '', 'left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0');
                    if (styleData)
                        $scope.WinPrint.document.write(styleData + prtContent.innerHTML);
                    else
                        $scope.WinPrint.document.write(prtContent.innerHTML);
                    $scope.WinPrint.document.close();
                    $scope.WinPrint.focus();
                    $scope.WinPrint.print();
                    $scope.WinPrint.close();
                    $scope.WinPrint = null;
                    $scope.$apply();

                    hideModal();
                };

                var closeWinPrint = function () {
                    $scope.WinPrint.close();
                };

                /**
                 * This function prints current Binding view in a new page
                 */
                $scope.print = function () {
                    var style = "";
                    $.when(
                            $.get("css/bootstrap.css", function (data) {
                                style += "<style>" + data + "</style>";
                            }),
                            $.get("css/print.css", function (data) {
                                style += "<style>" + data + "</style>";
                            })
                            ).then(function () {
                        print(style);
                    }, function () {
                        CommandApi.echo("There was an error to download default style. Generating print page(s) without style.");
                        console.warn("There was an error to download default style. Generating print page(s) without style.");
                        print();
                    });
                };

                $scope.clearModel = function () {
                    if (!!$scope.model && typeof $scope.model === "object") {
                        $scope.model = '';
                        $scope.$apply();
                    }
                };

            }])
        .directive("contenteditable", function () {
            return {
                require: "ngModel",
                transclude: true,
                scope: {ngModel: '='},
                link: function (scope, element, attrs, ngModel) {

                    var read = function () {
                        var data = element.text();
                        if (isNumber(data)) { // if is number
                            ngModel.$setViewValue(parseFloat(data));
                        } else {
                            ngModel.$setViewValue(data);
                        }
                    };

                    ngModel.$render = function () {
                        if (ngModel.$viewValue === undefined)
                            element.html("");
                        else
                            element.html(ngModel.$viewValue);
                    };

                    element.bind("blur keyup change", function () {
                        scope.$apply(read);
                    });
                }
            };
        }).filter('capitalize', function () {
    return function (input) {
        return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
    }
});
;

var isNumber = function (data) {
    return !isNaN(Number(data)) && data !== "" && typeof data !== "string";
};