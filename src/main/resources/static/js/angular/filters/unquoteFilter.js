angular.module('mainApp')
    .filter('unquote',function(){
        return function(input){
            return angular.isDefined(input) &&
                   angular.isString(input) ?
                        input.replace(/\"/g,'') : input;
        }
    })