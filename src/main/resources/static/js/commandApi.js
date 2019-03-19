
var CommandApi = {
    analyzeSEDLDocument: function (callback) {

        // TODO
        alert("Function analyzeSEDLDocument() here");
    },
    checkModel: function (content, format, checkModelURI, fileUri, callbackSuccess, callbackError) {

        var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri));
        var model = ModeManager.modelMap[modelId];
        var data = {
            content: content,
            fileUri: fileUri
        };
        var contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        if (model.apiVersion >= 2) {
            contentType = "application/json; charset=utf-8";
            data = JSON.stringify(data);
        } else {
            data.id = format;
        }
        $.ajax(checkModelURI, {
            type: "POST",
            contentType: contentType,
            data: data,
            success: function (result) {
                callbackSuccess(result);
            },
            error: function (result) {
                console.error(result.statusText);
                callbackError(result.statusText);
            },
            async: true
        });
    },
    callConverter: function (model, currentFormat, desiredFormat, fileUri,
            actualContent, converterUri, callback) {

        var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri));
        var model = ModeManager.modelMap[modelId];

        var fileData = {
            fileUri: fileUri,
            content: actualContent
        };

        var contentType;
        if (model.apiVersion <= 1) {
            fileData.currentFormat = currentFormat;
            fileData.desiredFormat = desiredFormat;
            contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        } else {
            contentType = "application/json; charset=utf-8";
            fileData = JSON.stringify(fileData);
        }

        $.ajax(converterUri, {
            type: "POST",
            contentType: contentType,
            data: fileData,
            success: function (result) {
                callback(result);
            },
            error: function (result) {
                console.error(result.statusText);
            },
            async: true
        });
    },
    doDocumentOperation: function (operationId, data, fileUri, callback, async) {

        if (async === undefined) {
            async = true;
        }

        var data;

        try {

            var operation;
            var name = '';
            for (var i = 0; i < opMap.length; i++) {
                if (opMap[i].id === operationId) {
                    operation = opMap[i];
                    name = opMap[i].name;
                }
            }

            OperationMetrics.play(name);



//            var operationUri = ModeManager
//                    .getBaseUri(ModeManager
//                            .calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri)))
//                    + "/language/operation/" + operationId + "/execute";

            var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri));
            var model = ModeManager.modelMap[modelId];
            var operationUri;
            var headers = {};
            if (model.apiVersion <= 1) {
                data = JSON.parse(JSON.stringify(data));
                data.fileUri = fileUri;
                data.content = EditorManager.getEditorContentByUri(fileUri);
                data.id = operationId;

                // Extensión para coger argumentos adicionales
                for (var i = 5; i < arguments.length; i++) {
                    data["auxArg" + (i - 5)] = arguments[i];
                }

                operationUri = ModeManager.getBaseUri(ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri)))
                        + DEPRECATED_EXEC_OP_URI.replace("$opId", operationId);

            } else if (model.apiVersion >= 2) {
                headers = {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                };
                data = [];
                data[0] = {};
                data[0].fileUri = fileUri;
                data[0].content = EditorManager.getEditorContentByUri(fileUri);
                data[0].parameters = operation.config;

                if (operation.config && operation.config.requireCredential) {
                    data[0].parameters.username = principalUserName;
                }

                // Extensión para coger argumentos adicionales
                for (var i = 5; i < arguments.length; i++) {
                    data[(i - 4)] = arguments[i];
                }

                operationUri = ModeManager.getBaseUri(ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri)))
                        + EXEC_OP_URI.replace("$modelId", modelId).replace("$operationId", operationId);
                data = JSON.stringify(data);
            }

            RequestHelper.ajax(operationUri, {
                headers: headers,
                "type": "POST",
                "data": data,
                "onSuccess": function (result) {
                    console.log("onSuccess");
                    console.log(operationUri);
                    callback(result);
                    OperationMetrics.stop();
                },
                "onProblems": function (result) {
                    console.log("onProblems");
                    callback(result);
                    OperationMetrics.stop();
                },
                "onError": function (result) {
                    console.log("onError");
                    console.log(operationUri);
                    callback(result);
                    OperationMetrics.stop();
                },
                "onSessionError": function (result) {
                    console.log("onSessionError");
                    callback(result);
                    OperationMetrics.stop();
                },
                "async": async
            });
        } catch (e) {
            console.log("Error executing document operation: \n" + e);
            callback(e);
        }

    },
    openFile: function (fileUri, callback) {
        EditorManager.openFile(fileUri, callback);
    },
    closeFile: function (fileUri) {
        EditorManager.closeFile(fileUri);
    },
    deleteNode: function (fileUri, callback) {
        EditorManager.deleteNode(fileUri, false, callback);
    },
    renameNode: function (fileUri, newName, callback) {
        EditorManager.renameNode(fileUri, newName, callback);
    },
    move: function (originFileUri, targetFileUri) {
        EditorManager.moveNode(originFileUri, targetFileUri, false);
    },
    copy: function (originFileUri, targetFileUri) {
        EditorManager.moveNode(originFileUri, targetFileUri, true);
    },
    importDemoWorkspace: function (demoWorkspaceName, targetWorkspaceName, overwrite, callback) {
        closeAllTabs();
        WorkspaceManager.getWorkspaces(function (workspacesArray) {
            var exists = false;
            for (index in workspacesArray)
                if (workspacesArray[index].name === targetWorkspaceName) {
                    exists = true;
                    break;
                }

            if (overwrite) {
                switchToDemoWorkspace(demoWorkspaceName, targetWorkspaceName, callback);
            } else if (exists) {
                if (localStorage.getItem("demo") === "demo") {
                    switchToDemoWorkspace(demoWorkspaceName, targetWorkspaceName, callback);
                } else {
                    var continueHandler = function () {
                        switchToDemoWorkspace(demoWorkspaceName, targetWorkspaceName, callback);
                        hideModal();
                    };

                    // showContentAsModal('app/modalWindows/generalModal', primaryActionText, primaryHandlerWrapper,
                    //                      cancelHandler, cancelHandler, 
                    //                        {'titleForModal': titleForModal, 'modalContent': $modalContent});
                    
                    showContentAsModal('app/modalWindows/importDemoWorkspace', continueHandler,
                            function () {}, function () {});
                }
            } else {
                switchToDemoWorkspace(demoWorkspaceName, targetWorkspaceName, callback);
            }

        });
    },
    echo: function (msg) {
        CommandsRegistry.echo.exec({
            message: msg
        });
    },
    deleteWorkspace: function (fileUri, callback) {
        WorkspaceManager.deleteWorkspace(fileUri, callback);
    },
    // Helper for executing operations that requires aditional data

    fetchFileContentsBeforeExecutingOperation: function (operation, fileUri, extensionRestriction, operationId,
            titleForModal, descText, primaryActionText, primaryHandler,
            cancelHandler) {

        var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri));
        var model = ModeManager.modelMap[modelId];

        if (window.File && window.FileReader && window.FileList && window.Blob) { // Check support

            useSelectionList = false;

            var fileContent = "";

            var $fileInput = $('<input id="ffcbeoFileUpload" type="file" >');
            var $fileInputWrapper = $('<span class="btn btn-success fileinput-button">' +
                    '<i class="glyphicon glyphicon-plus"></i>' +
                    '<span> Select file from your local file system</span>' +
                    '</span>');
            $fileInputWrapper.add($fileInput);

            $fileInputWrapper.click(function () {
                $fileInput.click();
            });
//			var $progressContainer = $(
//					'<div id="progress" class="progress">'
//						+ '<div class="progress-bar progress-bar-success"></div>'
//					+ '</div>');
            var $fileContainer = $('<div id="files" class="files" ></div>');

            var $modalContent = $('<span></span');

            $modalContent.append(descText + "<BR/>");
            $modalContent.append($fileInputWrapper);
            //$modalContent.append($progressContainer);
            $modalContent.append($fileContainer);

            $modalContent.append("<BR/>or select a valid file from your workspace:<BR/><BR/>");

            $existingFileSelectionContainer = $('<div id="existingFileSelectionContainer"></div>');

            $selectableList = $(
                    '<div class="form-group">' +
                    '<select class="form-control" name="category">' +
                    '<option value=0>-</option>' +
                    '</select>' +
                    '</div>');

            $existingFileSelectionContainer.append($selectableList);

            $modalContent.append($existingFileSelectionContainer);

            if (!(window.File && window.FileReader && window.FileList && window.Blob)) {	// Check HTML5 fileApi support
                alert('The File APIs are not fully supported in this browser. This operation can not be used without it.');
                closeModal();
            }

            var fileHandler = function (file) {
                var reader = new FileReader();
                reader.onload = function (event) {
                    var content = event.target.result;
                    fileContent = content;

                    $('.modal-footer .btn.btn-primary.continue').removeAttr("disabled");
                    useSelectionList = false;
                    $('#existingFileSelectionContainer select option').eq(0).prop('selected', true);

                };
                reader.readAsText(file[0]);
            };

            try {
                $fileInput.change(function () {
                    fileHandler(this.files);
                });
            } catch (e) {
                alert(e);
            }


            primaryHandlerWrapper = function () {

                if (useSelectionList) {
                    var fUri = $('#existingFileSelectionContainer select, callback').find(":selected").text();
                    FileApi.loadFileContents(WorkspaceManager.getSelectedWorkspace() + "/" + fUri, function (result) {
                        var data = {};
                        var auxArg = result;
                        if (model.apiVersion <= 1) {
                            data = operation.data;
                        } else if (model.apiVersion >= 2) {
                            auxArg = {};
                            auxArg.fileUri = fUri;
                            auxArg.content = result;
                        }
                        CommandApi.doDocumentOperation(operationId, data, fileUri, function (result) {
                            hideModal();
                            primaryHandler(result);
                        }, true, auxArg);
                    });
                } else {
                    var data = {};
                    var auxArg = fileContent;
                    if (model.apiVersion <= 1) {
                        data = operation.data;
                    } else if (model.apiVersion >= 2) {
                        auxArg = {};
                        auxArg.fileUri = fUri;
                        auxArg.content = fileContent;
                    }
                    CommandApi.doDocumentOperation(operationId, data, fileUri, function (result) {
                        hideModal();
                        primaryHandler(result);
                    }, true, auxArg);

                }


            };

            showModal(titleForModal, $modalContent, primaryActionText, primaryHandlerWrapper,
                    cancelHandler, cancelHandler);


            // Add options to select once the modal has been rendered

            var useExtRestriction = extensionRestriction != null && extensionRestriction != undefined && extensionRestriction != "";

            $("#projectsTree").dynatree("getRoot").visit(function (node) {
                if (!node.data.isFolder && (!useExtRestriction || useExtRestriction && node.data.keyPath.indexOf(extensionRestriction, this.length - extensionRestriction.length) !== -1)) {
                    $('#existingFileSelectionContainer select').append($('<option>', {
                        value: node.data.keyPath,
                        text: node.data.keyPath
                    }));
                }
            });

            // Control logic

            $('.modal-footer .btn.btn-primary.continue').attr("disabled", "disabled");

            $('#existingFileSelectionContainer select').on('change', function (e) {
                var optionSelected = $("option:selected", this);
                //var valueSelected = this.value;
                if (optionSelected == 0) {
                    useSelectionList = false;
                    $('.modal-footer .btn.btn-primary.continue').attr("disabled", "disabled");
                } else {
                    useSelectionList = true;
                    $('.modal-footer .btn.btn-primary.continue').removeAttr("disabled");
                }

            });


        } else {
            alert('Your browser do not support HTML5 FileApi. This operation can not be completed without it.');
        }
    }
};

var switchToDemoWorkspace = function (demoWorkspaceName, targetWorkspaceName, callback) {
    RequestHelper.ajax("files/importDemoWorkspace", {
        "type": "get",
        "data": {
            "demoWorkspaceName": demoWorkspaceName,
            "targetWorkspaceName": targetWorkspaceName
        },
        "onSuccess": function (result) {
            CommandApi.echo("Switching to workspace...");
            WorkspaceManager.getWorkspaces(function (wss) {
                $("#projectsTree").dynatree("getTree").reload();
                WorkspaceManager.setSelectedWorkspace(targetWorkspaceName, function () {
                    WorkspaceManager.loadWorkspace(function () {
                        EditorManager.reset();
                        if (callback) callback();
                    });
                });
            });

        },
        "onProblems": function (result) {

        },
        "onError": function (result) {
        },
        "async": true,
    });

};

var closeAllTabs = function () {
    CommandApi.echo("Editor preparing...");
    var tabs = $(".glyphicon-remove");
    for (var i = 0; i < tabs.length; i++) {
        tabs[i].click();
    }
};