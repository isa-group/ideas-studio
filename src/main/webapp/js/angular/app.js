// AngularJS initialization
angular.module("mainApp", [])
    .controller("MainCtrl", ["$scope", "$compile", function ($scope, $compile) {

      // Update editor content from model
      $scope.$watch(
        function () { return angular.toJson($scope.sla); },
        function (newValue, oldValue) {

          $scope.slaString = newValue;

          if (document.editor && EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat() === "json") {
            newValue = angular.toJson($scope.sla, 4);
            if (newValue !== document.editor.getValue()) {
              document.editor.setValue(newValue, -1);
            }
          }

        }
      );

      // Ordering a model update from ace editor
      $scope.slaString2Model = function () {
        try {
          if ($scope.slaString !== "") {
            $scope.sla = JSON.parse($scope.slaString);
          } else {
            $scope.sla = undefined;
          }
        } catch (err) {
          // empty
        }


      };

      // Ordering a model compilation from DescriptionInspector
      $scope.compilationFlag = 0;
      $scope.compileModel = function () {
        if ($scope.compilationFlag == 1) {
          console.log("compiling angular inspector");
          $compile(document.getElementById("inspectorModelContent"))($scope);
        }
      };

    }]);
