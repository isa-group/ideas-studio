var operationID = "Small Sampling";//'${id}';
var data = {};//'${data}';
var fileUri = "TFM-SEDL/Howard1999/ExperimentalDescription.sedl";//'${fileUri}';
var content ='import Charts↵/*  #====================================================================================#↵    #    This experiment description is part of the material of the paper on↵    #    EXEMPLAR to be publishen in ESEJ.↵    #====================================================================================#↵    # Experiment of the paper:↵    #   Howard, Geoffry S., et al. ↵    #  "The efficacy of matching information systems development methodologies with ↵    #     application characteristics–an empirical study." ↵    #   Journal of Systems and Software 45.3 (1999): 177-195.↵    #====================================================================================#↵    # Created on 2015/06/25 by D. F. Alonso and J. A. Parejo  ↵    #====================================================================================#↵*/↵EXPERIMENT:Howard1999Experiment version:1.0↵↵Variables:↵Factors:    ↵        methodology enum "data-centered","process-centered","none"↵Outcome:↵    quality ordered enum 1, 2, 3, 4, 5, 6↵Extraneous:↵    expert enum 1,2,3,4,5↵Hypothesis: Differential↵↵Design:↵    Sampling: Random  ↵    Groups: by methodology sizing 10↵    Protocol: Random↵    Analyses:↵    DescriptiveStatistics:↵        Avg(by methodology)↵        StdDev(by methodology)↵        Charts::BoxPlot by methodology↵    NHST-Premises:↵        Shapiro-Wilk(by methodology,0.05)↵        Levene(by methodology, 0.05)↵    NHST:↵        KruskalWalls(of quality by methodology,0.05)↵        Wilcoxon(by methodology,0.05)↵        ';
var params = '${params}';
var modelAttributeValue = '${modelAttribute}';

executeOperation = function(operationUri, extendedData, callback){
    try{
        //OperationMetrics.play(name);

        RequestHelper.ajax(operationUri, {
                "type" : "POST",
                "data" : extendedData,
                "onSuccess" : function(result) {
                        console.log("onSuccess");
                        console.log(operationUri);
                        callback(result);
                        //OperationMetrics.stop();
                },
                "onProblems" : function(result) {
                        console.log("onProblems");
                        callback(result);
                        //OperationMetrics.stop();
                },
                "onError" : function(result) {
                        console.log("onError");
                        console.log(operationUri);
                        callback(result);
                        //OperationMetrics.stop();
                },
                "onSessionError" : function(result) {
                        console.log("onSessionError");
                        callback(result);
                        //OperationMetrics.stop();
                },
                "async" : true,
        });
    }
    catch(e){
            console.log("Error executing operation: \n" + e);
            callback(e);
    }
},
        
launchOperation = function() {
    
    try {
        
        var languageID = ModeManager.calculateLanguageIdFromExt(
                            ModeManager.calculateExtFromFileUri(fileUri));
        
        
        
        var opMap = ModeManager.getOperations(languageID);
        
        for (var i = 0; i < opMap.length; i++) {
            
            if (opMap[i].name == "Small Sampling") {
                
                var _remoteExecution = opMap[i]._remoteExecution;
                var action = eval('(' + opMap[i].action + ')');
                var operation = opMap[i];
                
                if (_remoteExecution != undefined 
                    && _remoteExecution != null
                    && eval('(' + _remoteExecution + ')')) {
                
                    var extendedData = JSON.parse(JSON.stringify(operation.data));
                            extendedData.fileUri = fileUri;
                            extendedData.content = content;
                            extendedData.id = operation.id;
                            
//                            var name = '';
//                            for (var i = 0; i < opMap.length; i++) {
//                                    if(opMap[i].id == operationId){
//                                            name = opMap[i].name;
//                                    }
//                            }
                            
                            for (var i=5; i < arguments.length; i++) {
                                extendedData["auxArg"+(i-5)] = arguments[i];
                            }

                            var operationUri = ModeManager.getBaseUri(languageID)
                        + "/language/operation/" + operation.id + "/execute";

                    executeOperation(operationUri, extendedData,
                        function(result) {
                            if (action !== undefined && action !== null) {
                                action(operation, fileUri, result);
                            }						
                            //OperationReport.launchedOperations.push(operation.name);
                            //OperationReport.resultLaunchedOperations.push(result.message);
                        });
                } 
                else if (action != undefined && action != null){
                    
                    action(operation, fileUriOperation);  
                    
                }
		
                //OperationReport.timeOfOperations.push(OperationMetrics.getOperationMilliseconds());

                break;
                
                
            }
        }
        
        
    }

    catch (err) {
	    //OperationMetrics.stop();
	    //OperationReport.timeOfOperations.push(OperationMetrics.getOperationMilliseconds());
            alert("An error occurred while performing the operation ");
    }
};

$('#executeTest').click(function() {
    alert( "Handler for .click() called." );
    launchOperation();
});