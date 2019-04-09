// AngularJS initialization
var mainApp = angular.module("mainApp", ['ngSanitize', 'ui.router', 'ui.bootstrap.datetimepicker', 'ui.dateTimeInput', 'googlechart', 'chart.js','textAngular'])
    .controller("MainCtrl", ["$scope", "$compile", "$q", "$http", "$timeout", "$state", "$location", "$stateParams", function ($scope, $compile, $q, $http, $timeout, $state, $location, $stateParams) {
                    
        $scope.model = {};
        $scope.config = {
            autoremoveModel: false
        };
        

        $scope.currentBinding = "";
        
        $scope.$watch(function () {
            return $scope.currentBinding;
        }, function (newValue, oldValue) {
            if (newValue !== oldValue) {
                $scope.applyBinding(newValue);
            }
        });
        
        /**
         * Update current Binding visualization.
         * @param {Binding} currentBinding
         */
        $scope.applyBinding = function (currentBinding) {
            console.log("apply binding", currentBinding);

            try {
                currentBinding = JSON.parse(currentBinding);
            } catch (err) {
                DescriptionInspector.angularFormatView.destroy();
                return;
            }
            
            // Get Binding content
            $scope.getBindingContent(
                currentBinding.templateURL,
                currentBinding.controllerURL
            ).then(function (data) {
                LanguageBindingsManifestManager.apply({
                    template: data.template,
                    controller: data.controller,
                    selectors: ["modelBoardContent", "inspectorModelContent"]
                });
            }).catch(function (err) {
                console.error(err);
            });
        };
        
        /**
         * Get remote Binding content
         * @param {type} angUrl
         * @param {type} ctlUrl
         * @returns {Promise}
         */
        $scope.getBindingContent = function (angUrl, ctlUrl) {
            
            var blockerView = $("#appLoaderBlocker");
            
            blockerView.css({
                opacity: "0.8",
                visibility: "visible"
            });
            
            return new Promise(function (resolve, reject) {
                $http({
                    method: 'GET',
                    url: angUrl
                }).then(function (resp1) {
                    $http({
                        method: 'GET',
                        url: ctlUrl
                    }).then(function (resp2) {
                        resolve({
                            template: resp1.data, 
                            controller: resp2.data
                        });
                        blockerView.css({
                            opacity: "0",
                            visibility: "hidden"
                        });
                    }, function () {
                        blockerView.css({
                            opacity: "0",
                            visibility: "hidden"
                        });
                        reject();
                    });
                }, function () {
                    blockerView.css({
                        opacity: "0",
                        visibility: "hidden"
                    });
                    reject();
                });
            });
        };
        
        /**
         * Transform serializable object based on editor selected format.
         * @param {type} value
         * @returns {Array|Object}
         */
        $scope.transformSerializableToObject = function (value) {
            
            var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat().toLowerCase();
            var ret = undefined;
            
            if (currentFormat === "yaml") {
                ret = jsyaml.safeLoad(value);
            } else if (currentFormat === "json") {
                ret = JSON.parse(value);
            }
            
            return ret;
        };
        
        $scope.updateModel = function (value) {
            
            var ret = $scope.transformSerializableToObject(value);
            if (!!ret) {
                $scope.model = ret;
            } else {
                console.warn("Cannot update Binding model");
            }

        };

        $scope.languageBindingsManifest = {};
        
        $scope.$watch(function () {
            return $scope.languageBindingsManifest;
        }, function (newValue, oldValue) {
            $scope.languageBindingsManifestLen = (!!newValue) ? Object.keys(newValue).length: 0;
        });
        $scope.$timeout = $timeout;

        $scope.regenerateModelWacthers=function(){
         if($scope.clearModelWhatchers)
             $scope.clearModelWhatchers();
        // Update editor content from model
        $scope.clearModelWhatchers=$scope.$watch(
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
                } catch (err) {}

                if (document.editor && (
                        newValue && (oldValue || !oldValue || oldValue === "") || ((newValue || !newValue || newValue === "") && oldValue)
                    ) && !compareObjects(newValue, oldValue) && nodeName === tabName) {

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
                                    if(result.status!="ERROR")
                                        resolve(result.data);
                                    else
                                        console.log("ERROR while transforming from "+currentFormat+" to JSON!. "+result.message);
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
                    
                    // Update Binding panel
                    if (LanguageBindingsManifestManager.mayApply()) {
                        var model = $scope.transformSerializableToObject(document.editor.getValue());
                        if (!!model && "bindings" in model) {
                            $timeout(function () {
                                $scope.languageBindingsManifest = model.bindings;
                                if ( $("#bindingManagerPanel").val() === "" ) {
                                    $("#bindingManagerPanel").trigger("change");                                
                                }
                            }, 150)
                        }
                    }
                }
            }, true);
        };
        
        //$scope.regenerateModelWacthers();
        
        

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

                if (editorContent && (DescriptionInspector.existCurrentAngularFile() || LanguageBindingsManifestManager.mayApply()) && ("json" in formatSessions || "yaml" in formatSessions)) {

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
                                    if (result && result.data) {
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
        
        $scope.$compile = $compile;

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

            showContentAsModal('app/modalWindows/printMessage', closeWinPrint,
                closeWinPrint, closeWinPrint, { 'workbenchName': studioConfiguration.workbenchName });

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
            $scope.regenerateModelWacthers();
            if (!!$scope.model && typeof $scope.model === "object" && !!$scope.config.autoremoveModel) {                                
                $scope.model = undefined;
                $scope.$apply();                
            }                        
        };

    }]).directive("contenteditable", ['$timeout', function ($timeout) {
        return {
            restrict: "A",
            require: "ngModel",
            transclude: true,
            scope: {
                ngModel: '=',
                format: '=',
                timeout: '='
            },
            link: function (scope, element, attrs, ngModel) {

                var timer = undefined;

                var read = function (attrs, fileUri) {

                    var data = element.text();
                    var format = attrs.format;

                    if (!!timer) {
                        $timeout.cancel(timer);
                    }

                    timer = $timeout(function () {

                        if (fileUri === EditorManager.currentUri) {

                            if (!!format && !!data) {
                                if (format === "number" || format === "double") {
                                    data = parseFloat(data);
                                } else if (format === "string") {
                                    data = String(data);
                                } else if (format === "int" || format === "integer") {
                                    data = parseInt(data, 10);
                                }
                            } else if (!!data) {
                                if (!isNaN(data)) {
                                    data = parseFloat(data);
                                }
                            }

                            ngModel.$setViewValue(data);

                        }

                    }, !isNaN(attrs.timeout) ? Number(attrs.timeout) : 0);
                };

                ngModel.$render = function () {
                    var value = ngModel.$viewValue;
                    element.html((value === undefined) ? "" : value);
                };

                element.bind("keyup change", function () {
                    scope.$apply(read(attrs, EditorManager.currentUri));
                });
            }
        };
    }]).directive('dynamic', function ($compile) {
        return {
            restrict: 'A',
            replace: true,
            link: function (scope, ele, attrs) {
                scope.$watch(attrs.dynamic, function (html) {
                    ele.html(html);
                    $compile(ele.contents())(scope);
                });
            }
        };
    })
    .filter('capitalize', function () {
        return function (input) {
            return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
        }
    })
    .filter('numkeys', function() {
        return function(object) {
            return Object.keys(object).length;
        }
    });