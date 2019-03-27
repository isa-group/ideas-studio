var folder_icon = "../../../css/dyntree/skin/eFolder.png";
var project_icon = "../../../css/dyntree/skin/eProject.png";
var sedl_icon = "../../../css/dyntree/skin/eSEDLFile.png";
var file_icon = "../../../css/dyntree/skin/eSimpleFile.png";
var workspace_icon = "../../../css/dyntree/skin/folder_docs.gif";

// TODO: Temporal. Modificar con iconos concretos
var image_icon = "../../../css/dyntree/skin/eImage.png";
var pdf_icon = "../../../css/dyntree/skin/ePDF.png";
var document_icon = "../../../css/dyntree/skin/eDocument.png";
var spreadsheet_icon = "../../../css/dyntree/skin/eSpreadsheet.png";
var slideshow_icon = "../../../css/dyntree/skin/eSlides.png";
var csv_icon = "../../../css/dyntree/skin/eCSV.png";
var r_icon =   "../../../css/dyntree/skin/Rlogo.png";
var latex_icon =   "../../../css/dyntree/skin/LatexFile.png";
var binary_file_icon = "../../../css/dyntree/skin/eBinaryFile.png";

var extensionsIcons = {
    'gif': 'image_icon',
    'bmp': 'image_icon',
    'jpg': 'image_icon',
    'png': 'image_icon',
    'doc': 'document_icon',
    'docx': 'document_icon',
    'rtf': 'document_icon',
    'xls': 'spreadsheet_icon',
    'xlsx': 'spreadsheet_icon',
    'ppt': 'slideshow_icon',
    'pptx': 'slideshow_icon',
    'pdf': 'pdf_icon',
    'csv': 'csv_icon',
    'r': 'r_icon',
    'R': 'r_icon',
    'tex': 'latex_icon',
    '': 'binary_file_icon'
};

var selectedWorkspace;
var workspaces;

var WorkspaceManager = {
    setSelectedWorkspace: function (wsName, callback) {
        selectedWorkspace = wsName;
        FileApi.setSelectedWorkspace(wsName, function (ts) {
            console.log("WS setted: " + selectedWorkspace);
            if (callback) callback(ts);
        });
    },
    getSelectedWorkspace: function () {
        return selectedWorkspace;
    },
    readSelectedWorkspace: function (callback) {
        FileApi.getSelectedWorkspace(function (ts) {
            selectedWorkspace = ts;
            if (callback) callback(ts);
        });
    },
    getWorkspaces: function (callback) {
        console.log("Loading all workspaces.");
        $("#workspacesNavContainer").empty();
        FileApi.getWorkspaces(function (ts) {
            var wss = [];

            try {
                workspaces = eval('(' + ts + ')');

                for (index in workspaces) {
                    if (workspaces[index].name != undefined) {
                        var wsName = workspaces[index].name;
                        createWSLine(wsName);
                        wss.push(workspaces[index]);
                    }

                }
            } catch (err) {

            }

            if (callback) callback(wss);
        });
    },
    loadWorkspace: function (callback) {
        var workspaceName = WorkspaceManager.getSelectedWorkspace();
        console.log("Loading WS " + workspaceName + " ...");
        FileApi.loadWorkspace(workspaceName, function (ts) {
            var wsLabel = $("#editorSidePanelHeaderWorkspaceInfo");
            wsLabel.empty(WorkspaceManager.getSelectedWorkspace());
            wsLabel.append(WorkspaceManager.getSelectedWorkspace());
            if ($("#projectsTree").length > 0) {
                var treeStruct = ts;
                $("#projectsTree").dynatree("getRoot").addChild(treeStruct);
                $(".indented.apl_editor_"
                        + WorkspaceManager.getSelectedWorkspace())
                        .parent().addClass("active");
            }

            if ($("#projectsTree").length === 0) {
                $("#wsactions").css("display", "none");
            }

            // Only for editor page
            if (typeof EditorManager !== "undefined") {
                EditorManager.reset();
                if ($("#projectsTree").length > 0) { // Reorder tree
                    sortProjectsTree();
                    $("#projectsTree").dynatree("getRoot").visit(function(node){ 
                        if (node.getLevel() < 2) node.expand(true);
                    });
                }
            }

            $("#edit-ws").unbind("click").click(function (e) {
                e.preventDefault();
                var oldName = WorkspaceManager.getSelectedWorkspace();
                $("#modalCreationField input").val(oldName);
                showContentAsModal("app/modalWindows/editWorkspace", function () {
                    var workspaceName = $("#modalCreationField input").val();
                    var description = $("#descriptionInput textarea").val();
                    $("#workspacesNavContainer li").removeClass("active");
                    WorkspaceManager.updateWorkspace(workspaceName, description);
                    AppPresenter.loadSection("editor", workspaceName);
                });
            });

            $("#download-ws").unbind("click").click(function (e) {
                e.preventDefault();
                var name = WorkspaceManager.getSelectedWorkspace();
                WorkspaceManager.downloadAsZip(name);
            });

            $("#delete-ws").unbind("click").click(function (e) {
                e.preventDefault();
                WorkspaceManager.deleteWorkspace(WorkspaceManager.getSelectedWorkspace());
            });
            $("#demo-ws").unbind("click").click(function (e) {
                e.preventDefault();
                var name = WorkspaceManager.getSelectedWorkspace();
                WorkspaceManager.publishWorskspaceAsDemo(name);
                // AppPresenter.loadSection("editor", name);
            });
            $("#screenshot-ws").unbind("click").click(function (e) {
                e.preventDefault();

                var name = WorkspaceManager.getSelectedWorkspace();
                html2canvas($("#editor"), {
                    onrendered: function (canvas) {
                        theCanvas = canvas;
                        document.body.appendChild(canvas);

                        canvas.toBlob(function (blob) {
                            saveAs(blob, "Dashboard.png");
                        });
                    }
                });
            });
            
            // Filter extensions
            DEFAULT_FILTER_FILES = true;
            toggleAdvancedMode();
            
            // Check if there is a project called "Demo"
            // Search for Binding file at root 
            $("#projectsTree").dynatree("getRoot").visit(function (node) {
                // If there is a project named Demo
                if (node.data.isFolder && node.getLevel() === 1 && (node.data.keyPath === "Demo" || WizardViewManager.mayApply())) {
                    var found = false;
                    var auxTargetNode = null;
                    CONFIG_EXTENSIONS_PREFERENCES.forEach(function (ext) {
                        if (!found) {
                            node.getChildren().forEach(function (nodeChild) {
                                if (!found) {
                                    var fileUri = getFileUriByNode(nodeChild);
                                    var angFileUri = fileUri.replace(/\.[^/.]+$/, "") + ".ang";
                                    var htmlFileUri = fileUri.replace(/\.[^/.]+$/, "") + ".html";
                                    if (!!getNodeByFileUri(angFileUri) || !!getNodeByFileUri(htmlFileUri)) {
                                        auxTargetNode = nodeChild;
                                        if (ext.toLowerCase() === extractFileExtension(nodeChild.data.title).toLowerCase()) {
                                            EditorManager.openFile(getFileUriByNode(nodeChild), function () {
                                                maximize();
                                            });
                                            found = true;
                                            return false; // break
                                        }
                                    }
                                }
                            });
                        }
                    });
                    // There is a binding but it wasn't considered in extension list
                    if (!found && auxTargetNode != null) {
                        EditorManager.openFile(getFileUriByNode(auxTargetNode), function () {
                            maximize();
                        });
                    }
                }
            });
            if (callback) callback();
        });
    },
    createWorkspace: function (workspaceName, description, tags, callback) {
        FileApi.createWorkspace(workspaceName, description, tags, function (ts) {
            if (ts) {
                createWSLine(workspaceName, function () {
                    WorkspaceManager.setSelectedWorkspace(workspaceName);
                    WorkspaceManager.loadWorkspace();
                });
                if ($("#zipFile").val() !== "")
                    WorkspaceManager.initializeWithZipFile();

                if (callback)
                    callback();
            } else {
                showError("Error", "Error creating new workspace", function () {
                    hideError();
                });
                WorkspaceManager.deleteWorkspace(workspaceName, function () {
                    WorkspaceManager.loadWorkspace();
                });
            }
            hideModal();

        });
    },
    deleteWorkspace: function (workspaceName, callback) {
        var continueHandler = function () {
            FileApi.deleteWorkspace(workspaceName, function (ts) {
                if (ts) {
                    deleteWSLine(workspaceName);
                } else {
                    showError("Error", "Error deleting workspace", function () {
                        hideError();
                    });
                }
            });
            hideModal();
            if (studioConfiguration.advancedMode === true) {
                showContentAsModal("app/modalWindows/confirmDemoDeletion",
                    deleteDemosHandler, function() {}, function() {},
                    {
                        workspaceName: workspaceName
                    }
                );
            }
        };

        var deleteDemosHandler = function () {
            FileApi.deleteDemoWorkspace(workspaceName, function (ts) {
                if (ts) {
                } else {
                    showError("Error", "Error deleting workspace", function () {
                        hideError();
                    });
                }
            });
            hideModal();
            $(location).attr('href', "app/wsm");
        };
        showContentAsModal("app/modalWindows/confirmWorkspaceDeletion", continueHandler,
                function () {}, function () {}, { workspaceName: workspaceName });


    },
    updateWorkspace: function (workspaceName, description) {
        FileApi.updateWorkspace(workspaceName, description, function (ts) {
            if (ts) {
                if (WorkspaceManager.getSelectedWorkspace() !== workspaceName) {
                    deleteWSLine(WorkspaceManager.getSelectedWorkspace());
                }
                createWSLine(workspaceName, function () {
                    WorkspaceManager.setSelectedWorkspace(workspaceName);
                    AppPresenter.loadSection("editor", workspaceName);
                });

            } else {
                showError("Error", "Error updating workspace", function () {
                    hideError();
                });
                WorkspaceManager.loadWorkspace();
            }
            hideModal();
        });
    },
    deleteDemoWorkspace: function (workspaceName, callback) {
        var continueHandler = function () {
            FileApi.deleteDemoWorkspace(workspaceName, function (ts) {
                if (ts) {
                } else {
                    showError("Error", "Error deleting workspace", function () {
                        hideError();
                    });
                }
                if (callback) {
                    callback(ts);
                }
            });
            hideModal();
        };

        showContentAsModal("app/modalWindows/confirmWorkspaceDeletion", continueHandler,
                function () {}, function () {}, { workspaceName: workspaceName });

    },
    downloadAsZip: function (workspaceName, callback) {
        FileApi.downloadAsZip(workspaceName);
    },
    initializeWithZipFile: function () {
        var submitButton = $("#uploadSubmit");
        submitButton.click();
    },
    importDemoWorkspace: function (demoWorkspaceName, targetWorkspaceName) {
        CommandApi.importDemoWorkspace(demoWorkspaceName, targetWorkspaceName);
    },
    loadWorkspaceManagementWindow: function () {
        AppPresenter.loadSection("wsm");
    },
    publishWorskspaceAsDemo: function (workspaceName, callback) {
        var continueHandler = function () {
            FileApi.currentWSAsDemoWS(workspaceName, callback);
            hideModal();
        };
        
        var fullDemoURL = $("base").attr('href').valueOf() + "demo/" + workspaceName;
        
        showContentAsModal("app/modalWindows/confirmPublicationAsDemo", continueHandler,
                function () {}, function () {}, {
                    workspaceName: workspaceName,
                    fullDemoURL: fullDemoURL
                });
    },
    updateDemoWorkspace: function (workspaceName, callback) {
        var continueHandler = function () {
            FileApi.updateDemoWorkspace(workspaceName, callback);
            hideModal();
            $(location).attr('href', "app/wsm");
        };
        showContentAsModal("app/modalWindows/confirmDemoUpdate", continueHandler,
                function () {}, function () {},
                {
                    workspaceName: workspaceName,
                    url: $("base").attr('href').valueOf()
                });
    },
    importNewDemoWorkspace: function (workspaceName, callback) {
        var continueHandler = function () {
            FileApi.importNewDemoWorkspace(workspaceName, callback);
            WorkspaceManager.setSelectedWorkspace(workspaceName);
            hideModal();
            $(location).attr('href', "app/editor");
        };
        showContentAsModal("app/modalWindows/confirmClone", continueHandler,
                function () {}, function () {},
                {
                    workspaceName: workspaceName
                });
    }
};

var createWSLine = function (wsName) {
    var wsLi = $("<li><a class=\"indented apl_editor_"
            + wsName
            + "\">"
            + wsName
            + "</a><span class=\"glyphicon glyphicon-chevron-right\"></span></li>");
    $("#workspacesNavContainer").append(wsLi);
    wsLi.click(function () {
        $("#workspacesNavContainer li").removeClass("active");
        AppPresenter.loadSection("editor", wsName);
    });

};

var deleteWSLine = function (wsName) {
    wsline = $("li:has('a')").filter(function () {
        return $(this).text() === wsName;
    });
    wsline.remove();
    if (wsName == WorkspaceManager.getSelectedWorkspace()) {
        FileApi.getWorkspaces(function (result) {
            try {
                workspaces = eval('(' + result + ')');
                var wsName = workspaces[0].name;
                WorkspaceManager.setSelectedWorkspace(wsName);
                AppPresenter.loadSection("editor", wsName);
            } catch (err) {
                console.log(err);
            }
        });
    }

};