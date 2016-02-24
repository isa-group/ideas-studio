var operationID = $('#helpLink').text();
var fileUri = $('#fileUriText').text();
var auxArg0 = $('#auxArg0Data').text();
var auxArg1 = $('#auxArg1Data').text();
var auxArg2 = $('#auxArg2Data').text();
var auxArg3 = $('#auxArg3Data').text();
var auxArg4 = $('#auxArg4Data').text();



var OperationalReplication = {
    generateReplicationLink: function (name, extendedData, callback) {
        
        if(extendedData["auxArg0"]===undefined){
            extendedData["auxArg0"]=null;
        }
        if(extendedData["auxArg1"]===undefined){
            extendedData["auxArg1"]=null;
        }
        if(extendedData["auxArg2"]===undefined){
            extendedData["auxArg2"]=null;
        }
        if(extendedData["auxArg3"]===undefined){
            extendedData["auxArg3"]=null;
        }
        if(extendedData["auxArg4"]===undefined){
            extendedData["auxArg4"]=null;
        }
        
        $.ajax("replications", {
            "type": "POST",
            "data": {
                workspace: WorkspaceManager.getSelectedWorkspace(),
                operation: name,
                file: extendedData.fileUri,
                auxArg0: extendedData.auxArg0,
                auxArg1: extendedData.auxArg1,
                auxArg2: extendedData.auxArg2,
                auxArg3: extendedData.auxArg3,
                auxArg4: extendedData.auxArg4
            },
            "success": function (result) {
                CommandApi.echo("<script type=\"text/javascript\">"+
                                                        "$('#replicationLink').remove();"+
                                                        "$('#replicationLinkCopyButton').remove();"+ 
                                                "</script>"); 
                CommandApi.echo("<input id=\"replicationLink\" +\n\
                                        disabled size=\"85\" \n\
                                        value=\"" + $("base").attr('href').valueOf() + "replications/" + result + "\">\n\
                                </input>\n\
                                <a id=\"replicationLinkCopyButton\"  class=\"btn btn-default btn-xs\" role=\"button\">\n\
                                    <span class=\"glyphicon glyphicon-file\">\n\
                                </span></a>");
                CommandApi.echo("<script>" +
                        "$('#replicationLinkCopyButton').click(function() {" +
                            "OperationalReplication.copyToClipboard('#replicationLink');" +
                        "});</script>");
                callback(result);
            },
            "error": function (result) {
                console.error(result.statusText);
                //RequestHelper.sessionAlive(result);
            },
            "async": true,
        });
    },
    launchOperation: function (operationName) {
        try {

            var languageID = ModeManager.calculateLanguageIdFromExt(
                    ModeManager.calculateExtFromFileUri(fileUri));

            var opMap = ModeManager.getOperations(languageID);

            for (var i = 0; i < opMap.length; i++) {

                if (opMap[i].name == operationName) {

                    var _remoteExecution = opMap[i]._remoteExecution;
                    var action = eval('(' + opMap[i].action + ')');
                    var operation = opMap[i];

                    if (_remoteExecution != undefined
                            && _remoteExecution != null
                            && eval('(' + _remoteExecution + ')')) {

                        var extendedData = JSON.parse(JSON.stringify(operation.data));
                        extendedData.fileUri = fileUri;
                        extendedData.content = ace.edit("inputAceEditor").getValue();
                        extendedData.id = operation.id;
                        extendedData.auxArg0 = auxArg0;
                        extendedData.auxArg1 = auxArg1;
                        extendedData.auxArg2 = auxArg2;
                        extendedData.auxArg3 = auxArg3;
                        extendedData.auxArg4 = auxArg4;

                        var operationUri = ModeManager.getBaseUri(languageID)
                                + "/language/operation/" + operation.id + "/execute";

                        OperationalReplication.executeOperation(operationUri, extendedData,
                                function (result) {
                                    if (action !== undefined && action !== null) {
                                        action(operation, fileUri, result);
                                    }
                                });
                    }
                    else if (action != undefined && action != null) {

                        action(operation, fileUriOperation);

                    }

                    break;


                }
            }


        }

        catch (err) {
            CommandApi.echo("An error occurred while performing the operation ");
        }
    },
    executeOperation: function (operationUri, extendedData, callback) {
        try {
            //OperationMetrics.play(operationID);

            RequestHelper.ajax(operationUri, {
                "type": "POST",
                "data": extendedData,
                "onSuccess": function (result) {
                    console.log("onSuccess");
                    console.log(operationUri);
                    callback(result);
                    //OperationMetrics.stop();
                },
                "onProblems": function (result) {
                    console.log("onProblems");
                    callback(result);
                    //OperationMetrics.stop();
                },
                "onError": function (result) {
                    console.log("onError");
                    console.log(operationUri);
                    callback(result);
                    //OperationMetrics.stop();
                },
                "onSessionError": function (result) {
                    console.log("onSessionError");
                    callback(result);
                    //OperationMetrics.stop();
                },
                "async": true,
            });
        }
        catch (e) {
            console.log("Error executing operation: \n" + e);
            callback(e);
        }

    },
    copyToClipboard: function (inputID) {
            var urlLink = document.querySelector(inputID);  
            var range = document.createRange();  
            range.selectNode(urlLink);  
            window.getSelection().addRange(range);  

            try {  
                // Now that we've selected the anchor text, execute the copy command  
                var successful = document.execCommand('copy');  
                var msg = successful ? 'successful' : 'unsuccessful';  
                console.log('Copy url command was ' + msg);  
            } 
            catch(err) {  
                console.log('Oops, unable to copy');  
            }    
            window.getSelection().removeAllRanges();  
    }
};

$('#helpLink').click(function () {
    var helpUri = ModeManager.getBaseUri(
            ModeManager.calculateLanguageIdFromExt(
                    ModeManager.calculateExtFromFileUri(fileUri)))
            + "/help/";

    window.location.href = helpUri;

});

$('#executeTest').click(function () {
    operationID = $('#helpLink').text();
    fileUri = $('#fileUriText').text();
    var auxArg0 = $('#auxArg0Data').text();
    var auxArg1 = $('#auxArg1Data').text();
    var auxArg2 = $('#auxArg2Data').text();
    var auxArg3 = $('#auxArg3Data').text();
    var auxArg4 = $('#auxArg4Data').text();
    console.log("Executing test");
    OperationalReplication.launchOperation(operationID);
});
