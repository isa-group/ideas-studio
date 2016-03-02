// AngularJS initialization
angular.module("mainApp", ['puElasticInput', 'ngAnimate'])
    .controller("MainCtrl", ["$scope", "$compile", "$timeout", function ($scope, $compile, $timeout) {

        $scope.model = {};

        // Update editor content from model
        $scope.$watch(
            function () {
                // Should use toJson to catch objects declared inside the model.
                return $scope.model;
            },
            function (newValue, oldValue) {
                
                if (document.editor && newValue && oldValue && newValue !== oldValue) {
                    
                    var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat(),
                        prevPos = document.editor.renderer.scrollBarV.scrollTop, // previous cursos position
                        prevRange = document.editor.selection.getRange();
                        
                    if (currentFormat === "json") {
//                        newValue = angular.toJson($scope.model, 4);
                        if (newValue !== document.editor.getValue()) {
                            document.editor.setValue(angular.toJson($scope.model, 4), -1);
                            // Update editor to previous view
                            document.editor.selection.setRange(prevRange);
                            document.editor.renderer.scrollToY(prevPos);
                        }
                        
                    } else if (currentFormat === "yaml") {
//                        var jsonObj = jsyaml.safeLoad(newValue);
                        var yaml = jsyaml.safeDump(newValue);
                        if (yaml !== document.editor.getValue()) {
                            document.editor.setValue(yaml, -1);
                            // Update editor to previous view
                            document.editor.selection.setRange(prevRange);
                            document.editor.renderer.scrollToY(prevPos);
                        }
                        
                    } else {
                        
                        var baseFormat = EditorManager.sessionsMap[EditorManager.currentUri].getBaseFormat();

                        if ( !compareObjects(newValue, oldValue) ) {
                            
                            var currentUri = EditorManager.currentUri,
                                converterUri = ModeManager.getConverter(ModeManager
                                    .calculateLanguageIdFromExt(ModeManager
                                        .calculateExtFromFileUri(currentUri)));

                            // Convert json to currentFormat
                            CommandApi.callConverter("json", currentFormat, currentUri, angular.toJson(newValue), converterUri,
                                function(result) {
                                    
                                    if (result.data) {
                                        document.editor.setValue(result.data, -1);
                                        // Update editor to previous view
                                        document.editor.selection.setRange(prevRange);
                                        document.editor.renderer.scrollToY(prevPos);
                                    }
                                }
                            );
                        }
                    }   
                }
            }, true
        );

        /**
         * Update model from ace editor. 
         * This method is enabled by a hidden input outside of angular environment.
         * @returns {undefined}
         */
        $scope.slaString2Model = function () {
            
            try {
                
                var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat(),
                    currentUri = EditorManager.currentUri,
                    editorContent = document.editor.getValue();
                
                if (currentFormat === "json") {
                    if ($scope.slaString !== "") {
                        $scope.model = JSON.parse(editorContent);
                    } else {
                        $scope.model = undefined;
                    }
                    
                } else if (currentFormat === "yaml") {
                    $scope.model = jsyaml.safeLoad(editorContent);
                    
                } else {
                    //debugger;
                    // Convert current format to json
                    var converterUri = ModeManager.getConverter(ModeManager
                            .calculateLanguageIdFromExt(ModeManager
                                .calculateExtFromFileUri(currentUri)));

                                flag=true;

                    // Convert current editor content to json
                    CommandApi.callConverter(currentFormat, "json", currentUri, editorContent, converterUri,
                        function(result) {
                            if (!compareObjects(JSON.parse(result.data), $scope.model)) {
                                $scope.model = JSON.parse(result.data);
                            }

                        }
                    );
                }
                
            } catch (err) {
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
//                DescriptionInspector.angularFormatView.setDefaultContent();
                $compile(angular.element(selector)[0])($scope);
//                $("#modelBoardContent").attr("background","");
            }
        };
        
        // editorEnable
        $scope.editorEnabled = false;
        $scope.enableEditor = function($event) {
            $scope.editorEnabled = true;
            // Activate element clicked
            $timeout(function () {
                $($event.currentTarget)
                    .prev("span")
                    .find("input")
                    .select();
            }, 150);
        };

        // Disable editor form view
        $scope.disableEditor = function() {
            $timeout(function () {
                var parent = $(":focus").closest("#inspectorModelContent").length > 0 ? 
                    $("#inspectorModelContent") : $("#modelBoardContent");
                var focusElement = parent.find(":focus");
                if (focusElement.length === 0) {
                    $scope.editorEnabled = false;
                }
            }, 150);
        };
        
        /**
         * Creates a new agreement template based on the current model.
         * @returns {undefined}
         */
        $scope.createNewAgTemplate = function () {
            var constraintName = "";
            var i = 1;
            var const_i = "";
            
            while (constraintName === "") {
                const_i = "C"+i;
                if ( !(const_i in getCreationConstraints()) ) {
                    constraintName += const_i;
                    break;
                }
                i++;
            }
            
            if (constraintName !== "") {
                getCreationConstraints()[const_i] = creationConstraintStructureWithId(const_i);
            }
        };
      
        // SLA getters
        var getCreationConstraints = function () {
            return $scope.model.creationConstraints;
        };
        
        // Obtain a creationConstraint for a specific id
        var creationConstraintStructureWithId = function (constraintId) {
            return {
                "slo": {
                    "expression": {
                        "_type": "ParenthesisExpression",
                        "properties": {
                            "exp": {
                                "_type": "LogicalExpression",
                                "properties": {
                                    "exp1": {
                                        "_type": "LogicalExpression",
                                        "properties": {
                                            "exp1": {
                                                "_type": "RelationalExpression",
                                                "properties": {
                                                    "exp1": {
                                                        "_type": "Var",
                                                        "properties": {
                                                            "id": "MaxRequests"
                                                        }
                                                    },
                                                    "exp2": {
                                                        "_type": "Atomic",
                                                        "properties": {
                                                            "value": "1000"
                                                        }
                                                    },
                                                    "operator": "EQ"
                                                }
                                            },
                                            "exp2": {
                                                "_type": "RelationalExpression",
                                                "properties": {
                                                    "exp1": {
                                                        "_type": "Var",
                                                        "properties": {
                                                            "id": "MaxResponseTime"
                                                        }
                                                    },
                                                    "exp2": {
                                                        "_type": "Atomic",
                                                        "properties": {
                                                            "value": "100"
                                                        }
                                                    },
                                                    "operator": "EQ"
                                                }
                                            },
                                            "operator": "AND"
                                        }
                                    },
                                    "exp2": {
                                        "_type": "RelationalExpression",
                                        "properties": {
                                            "exp1": {
                                                "_type": "Var",
                                                "properties": {
                                                    "id": "PlanPrice"
                                                }
                                            },
                                            "exp2": {
                                                "_type": "Atomic",
                                                "properties": {
                                                    "value": "10"
                                                }
                                            },
                                            "operator": "EQ"
                                        }
                                    },
                                    "operator": "AND"
                                }
                            }
                        }
                    }
                },
                "qc": {
                    "condition": {
                        "_type": "RelationalExpression",
                        "properties": {
                            "exp1": {
                                "_type": "Var",
                                "properties": {
                                    "id": "PlanType"
                                }
                            },
                            "exp2": {
                                "_type": "Atomic",
                                "properties": {
                                    "value": "\"pro\""
                                }
                            },
                            "operator": "EQ"
                        }
                    }
                },
                "id": constraintId
            };
        };
        
        // Blur form by key pressing
        $scope.modelKeyPress = function ($event) {
            if ($event.which === 13 || event.which === 27) { // 13,27: ENTER,ESC key
                var element = $event.target;
                element.blur();
            }
        };

    }]);