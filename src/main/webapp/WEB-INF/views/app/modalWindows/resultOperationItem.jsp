<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div id="load" >
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×</button>
            <h4 class="modal-title">
                <spring:message code="editor.actions.modal.result-operation.title" />
            </h4>
        </div>
        <div class="modal-body">
            <div id="modalCreationField">
                <!--     	TODO  -->
                <span class="" style="margin-bottom: 2ex;"> <spring:message
                        code="editor.actions.modal.result-operation.msg" />
                </span> <br />
                <div id="operationsResult" class="scrollable-op-result"></div>
            </div>
        </div>
        <div class="modal-footer">
            <a data-dismiss="modal" class="btn dismiss">Close</a> <a
                class="btn btn-primary continue" id="downloadReport"><spring:message
                    code="editor.actions.modal.result-operation.button" /></a>
        </div>
    </div>
    <script>
        $(function () {

            var fileUri = EditorManager.getCurrentUri();
            var opMap = ModeManager.getOperations(ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri)));
            var opIdMap = operations;
            var name = null;

            try {
                for (var i = 0; i < opMap.length; i++) {
                    opIdMap.forEach(function (opid) {
                        if (opMap[i].id == opid) {
                            name = opMap[i].name;
                            OperationMetrics.play(name);
                            var _remoteExecution = opMap[i]._remoteExecution;
                            var action = eval('(' + opMap[i].action + ')');
                            var operation = opMap[i];

                            if (_remoteExecution != undefined && _remoteExecution != null && eval('(' + _remoteExecution + ')')) {

                                CommandApi.doDocumentOperation(operation.id, operation.data, fileUri, function (result) {
                                    console.log("--->>" + operation.id + " " + operation.data + " " + fileUri + " " + operation);
                                    if (action != undefined && action != null) {
                                        console.log(result);
                                        if (result.annotations)
                                            EditorManager.setAnnotations(result.annotations);

                                        action(operation, fileUri, result, function () {

                                        });

                                    }
                                    var line = $('<div class="line"></div>');
                                    var resultLine = $('<span class="op-name">' + operation.name + ' </span>');
                                    var resultOp = $('<span class="op-result">' + result.message + '</span>');
                                    line.append(resultLine);
                                    line.append(resultOp);
                                    $("#operationsResult").append(line);
                                    OperationReport.launchedOperations.push(operation.name);
                                    OperationReport.resultLaunchedOperations.push(result.message);
                                });

                            } else if (action != undefined && action != null) {
                                action(operation, fileUri, function () {});
                            }
                            OperationMetrics.stop();
                            OperationReport.timeOfOperations.push(OperationMetrics.getOperationMilliseconds());
                        }
                    });

                }
            } catch (err) {
                OperationMetrics.stop();
                OperationReport.timeOfOperations.push(OperationMetrics
                        .getOperationMilliseconds());
                CommandApi
                        .echo("An error occurred while performing the operation <b>"
                                + name
                                + "</b>. Make sure you have well defined this operation in the language's manifest.");

                //resultOperations.onCancel();
                var line = $('<div class="line"></div>');
                var resultLine = $('<span class="op-name">' + name + ' </span>');
                var resultOp = $('<span class="op-result">' + "An error occurred. Make sure you have well defined this operation in the language's manifest." + '</span>');
                line.append(resultLine);
                line.append(resultOp);
                $("#operationsResult").append(line);
            }

        });

    </script>
</ideas:app-modal-dialog>