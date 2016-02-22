// AngularJS initialization
angular.module("mainApp", ['puElasticInput'])
    .controller("MainCtrl", ["$scope", "$compile", "$timeout", function ($scope, $compile, $timeout) {

        // Update editor content from model
        $scope.$watch(
            function () {
                return angular.toJson($scope.model);
            },
            function (newValue, oldValue) {

                if (document.editor) {
                    var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();
                    if (currentFormat === "json") {
                        $scope.slaString = newValue;
                        newValue = angular.toJson($scope.model, 4);
                        if (newValue !== document.editor.getValue()) {
                            document.editor.setValue(newValue, -1);
                        }
                    } else if (currentFormat === "yaml") {
                        $scope.slaString = newValue;
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
                        $scope.model = JSON.parse($scope.slaString);
                    } else {
                        $scope.model = undefined;
                    }
                } else if (currentFormat === "yaml") {
                    $scope.model = jsyaml.safeLoad(document.editor.getValue());
                }
            } catch (err) {
                // empty
            }
        };

        // Ordering a model compilation from DescriptionInspector
        $scope.compilationFlag = 0;
        $scope.compileModel = function () {
            if ($scope.compilationFlag == 1) {
                $compile(document.getElementById("inspectorModelContent"))($scope);
            }
        };
        
        // editorEnable
        $scope.editorEnabled = false;
        $scope.enableEditor = function($event) {
            $scope.editorEnabled = true;
            // Activate element clicked
            $timeout(function () {
                $event.currentTarget.focus();
            }, 1000);
        };

        $scope.disableEditor = function() {
            $timeout(function () {
                var focusElement = $(DescriptionInspector.vars.selectors.inspectorModelContent).find(":focus");
                if (focusElement.length === 0) {
                    $scope.editorEnabled = false;
                }
            }, 150);
        };

    }]);
