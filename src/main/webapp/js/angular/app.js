// AngularJS initialization
angular.module("mainApp", ['puElasticInput'])
    .controller("MainCtrl", ["$scope", "$compile", "$timeout", "$q", function ($scope, $compile, $timeout, $q) {

        $scope.model = {};
    
        // Update editor content from model
        $scope.$watch(
            function () {
                return $scope.model;
            },
            function (newValue, oldValue) {
                
                if ( document.editor && newValue && oldValue && !compareObjects(newValue, oldValue) ) {
                    
                    var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat(),
                        prevPos = document.editor.renderer.scrollBarV.scrollTop, // previous cursos position
                        prevRange = document.editor.selection.getRange();
                        
                    if (currentFormat === "json") {
                        if (newValue !== document.editor.getValue()) {
                            document.editor.setValue(angular.toJson(newValue, 2), -1);
                            // Update editor to previous view
                            document.editor.selection.setRange(prevRange);
                            document.editor.renderer.scrollToY(prevPos);
                        }
                        
                    } else if (currentFormat === "yaml") {
                        var yaml = jsyaml.safeDump(newValue);
                        if (yaml !== document.editor.getValue()) {
                            document.editor.setValue(yaml, -1);
                            // Update editor to previous view
                            document.editor.selection.setRange(prevRange);
                            document.editor.renderer.scrollToY(prevPos);
                        }
                        
                    } else {
                        
                        var baseFormat = EditorManager.sessionsMap[EditorManager.currentUri].getBaseFormat();

                        var currentUri = EditorManager.currentUri,
                            converterUri = ModeManager.getConverter(ModeManager
                                .calculateLanguageIdFromExt(ModeManager
                                    .calculateExtFromFileUri(currentUri)));
                            
                        // Convert json to currentFormat
                        var promise = $q(function (resolve, reject) {
                            CommandApi.callConverter("json", currentFormat, currentUri, angular.toJson(newValue), converterUri,
                                function(result) {
                                    resolve(result.data);
                                }
                            );
                        });
                        promise.then(function (result) {
                            if (result !== document.editor.getValue()) {
                                document.editor.setValue(result, -1);
                                // Update editor to previous view
                                document.editor.selection.setRange(prevRange);
                                document.editor.renderer.scrollToY(prevPos);
                            }
                        });

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
            var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat(),
                editorContent = document.editor.getValue();
            
            // Try-catch block for bad JSON parsing.
            try {
                
                if (editorContent) {

                    //TODO: check if it's a "JSONable" content

                    if (currentFormat === "json") {
                        if ($scope.slaString !== "") {
                            $scope.model = JSON.parse(editorContent);
                        } else {
                            $scope.model = undefined;
                        }

                    } else if (currentFormat === "yaml") {
                        $scope.model = jsyaml.safeLoad(editorContent);

                    } else {
                        // Convert current format to json
                        var converterUri = ModeManager.getConverter(ModeManager
                                .calculateLanguageIdFromExt(ModeManager
                                    .calculateExtFromFileUri(EditorManager.currentUri)));

                        var promise = $q(function (resolve, reject) {
                            CommandApi.callConverter(currentFormat, "json", EditorManager.currentUri, editorContent, converterUri,
                                function(result) {
                                    if (result.data) {
                                        resolve( JSON.parse(result.data) );
                                    }
                                }
                            );
                        });
                        promise.then(function (result) {
                            $scope.model = result;
                        });
                    }
                    
                } else {
                    $scope.model = {};
                }
                
            } catch (err) {
                // nothing
            }
            
            // Show/hide addSlaButton
            $timeout(function () {
                console.log("scope.model : ", $scope.model);
                if ($scope.model && $scope.model.creationConstraints) {
                    if ($("#editorFormats .formatTab.active").text() === "FORM") {
                        $(".addSlaButton").fadeIn();
                    } else {
                        $("#editorInspectorLoader .modelInspectorContent .addSlaButton").fadeIn();
                    }

                } else {
                    $(".addSlaButton").hide();
                } 

            }, 500);
            
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
            
            if ( getCreationConstraints() ) {
                
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
                
            } else {
                console.log("Please, define a 'creationConstraint' in the model");
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
        
        // Remove model element
        $scope.removeModel = function (modelId, $event) {
            
            var element = $event ? $($event.currentTarget) : undefined;
            if (element) {
                var parentSelector = "#editorWrapper",
                    isFormatView = element.closest(parentSelector) ? element.closest(parentSelector).length > 0 : false;
                if (isFormatView) {
                    $(element).closest("[ng-repeat]").animate({width: 'toggle'}); 
                } else {
                    $(element).closest("[ng-repeat]").slideUp();
                }
                
                $timeout(function () {
                if (modelId in getCreationConstraints()) {
                        delete getCreationConstraints()[modelId];
                    }
                }, 500);
                
            } else {
                if (modelId in getCreationConstraints()) {
                    delete getCreationConstraints()[modelId];
                }
            }
            
        };

    }]);