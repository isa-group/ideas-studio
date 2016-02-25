// AngularJS initialization
angular.module("mainApp", ['puElasticInput'])
    .controller("MainCtrl", ["$scope", "$compile", "$timeout", function ($scope, $compile, $timeout) {

        $scope.model = {};

        // Update editor content from model
        $scope.$watch(
            function () {
                // Should use toJson to catch objects declared inside the model.
                return angular.toJson($scope.model);
            },
            function (newValue, oldValue) {
                console.log("newValue === oldValue? ", newValue === oldValue);
                if (newValue && oldValue && newValue !== oldValue && document.editor) {
                    var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();
                    if (currentFormat === "json") {
                        newValue = angular.toJson($scope.model, 4);
                        if (newValue !== document.editor.getValue()) {
                            document.editor.setValue(newValue, -1);
                        }
                    } else if (currentFormat === "yaml") {
                        var jsonObj = jsyaml.safeLoad(newValue);
                        var yaml = jsyaml.safeDump(jsonObj);
                        if (yaml !== document.editor.getValue()) {
                            document.editor.setValue(yaml, -1);
                        }
                    }
                }
            }
        );

        // Ordering a model update from ace editor
        $scope.slaString2Model = function () {
            try {
                var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();
                if (currentFormat === "json") {
                    if ($scope.slaString !== "") {
                        $scope.model = JSON.parse(document.editor.getValue());
                    } else {
                        $scope.model = undefined;
                    }
                } else if (currentFormat === "yaml") {
                    $scope.model = jsyaml.safeLoad(document.editor.getValue());
                }
            } catch (err) {
                console.error(err);
            }
        };

        // Ordering a model compilation from DescriptionInspector
        $scope.compilationFlag = 0;
        $scope.compileModel = function () {
            if ($scope.compilationFlag == 1) {
                // Compile model tab content
                $compile(angular.element("#editorInspectorLoader .modelInspectorContent")[0])($scope);
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

        $scope.disableEditor = function() {
            $timeout(function () {
                var focusElement = $(DescriptionInspector.vars.selectors.inspectorModelContent).find(":focus");
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
                getCreationConstraints()[const_i] = {
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
                                                                "value": "200"
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
                                                        "value": "200"
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
                    "id": const_i
                };
            }
        };
      
        // SLA getters
        var getCreationConstraints = function () {
            return $scope.model.creationConstraints;
        };

    }]);
