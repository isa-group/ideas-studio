var operationID = $('#helpLink').text();
var fileUri = $('#fileUriText').text();
var editor = ace.edit("inputAceEditor");

var OperationalReplication = {
    generateReplicationLink: function (name, extendedData, callback) {
        var auxParamsArray = [];
        auxParamsArray.push(extendedData.auxArg0,extendedData.auxArg1,extendedData.auxArg2,extendedData.auxArg3,extendedData.auxArg4);
        $.ajax("replications", {
            "type": "POST",
            "data": {
                workspace: WorkspaceManager.getSelectedWorkspace(),
                operation: name,
                file: extendedData.fileUri,
                params: auxParamsArray.toString()
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
                        extendedData.content = editor.getValue();
                        extendedData.id = operation.id;



                        for (var i = 5; i < arguments.length; i++) {
                            extendedData["auxArg" + (i - 5)] = arguments[i];
                        }

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
    data = $('dataTextarea').text();
    fileUri = $('#fileUriText').text();
    content = $('#inputAceEditor').text();
    params = $('paramsTextarea').text();
    console.log("Executing test");
    OperationalReplication.launchOperation(operationID);
});
