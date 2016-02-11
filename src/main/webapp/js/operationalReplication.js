var operationID = $('#helpLink').text();
var data = $('dataTextarea').text();
var fileUri = $('#fileUriText').text();
var content = $('#inputAceEditor').text();
var params = $('paramsTextarea').text();

var OperationalReplication = {
    generateReplicationLink: function (name, extendedData, callback) {
        $.ajax("replications", {
            "type": "POST",
            "data": {
                workspace: WorkspaceManager.getSelectedWorkspace(),
                operation: name,
                file: extendedData.fileUri,
                data: extendedData.content,
                params: extendedData.toString()
            },
            "success": function (result) {
                CommandApi.echo("<input id=\"replicationLink\" disabled size=\"85\" value=\"" + $("base").attr('href').valueOf() + "replications/" + result + "\"></input><a id=\"replicationLinkCopyButton\" class=\"btn btn-default btn-xs\" role=\"button\"><span class=\"glyphicon glyphicon-file\"></span></a>");
                CommandApi.echo("<script>" +
                        "$('#replicationLinkCopyButton').click(function() {" +
                        "alert($('#replicationLink').valueOf());" +
                        "});");
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
                        extendedData.content = content;
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
