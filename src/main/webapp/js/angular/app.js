// AngularJS initialization
angular.module("mainApp", [])
    .controller("MainCtrl", ["$scope", "$compile", function ($scope, $compile) {

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
//                    alert(jsyaml.safeLoad($scope.slaString));
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
        $scope.enableEditor = function() {
            $scope.editorEnabled = true;
        };

        $scope.disableEditor = function($event) {
            $event.preventDefault();
            $scope.editorEnabled = false;
        };

    }]);
