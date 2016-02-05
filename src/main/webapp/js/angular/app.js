// AngularJS initialization
angular
    .module("mainApp", [])
    .controller("MainCtrl", ["$scope", "$compile", function ($scope, $compile) {
        
        $scope.slaString = JSON.stringify($scope.sla);

        $scope.$watch(
            function () { return JSON.stringify($scope.sla); },
            function (newValue, oldValue) {
                $scope.slaString = newValue;
                if (document.editor) {
                    document.editor.setValue(newValue);
                }
            }
        );
      
        $scope.slaString2Model = function () {
            $scope.sla = JSON.parse($scope.slaString);
        };

    }]).directive('inspectorModel', function($compile) {
        return {
            
            link : function(scope, element, attr) {
                scope.onClicked = function () {
                    $compile(document.getElementById("inspectorModelContent"))(scope);
                };
            }
            
        };
    });