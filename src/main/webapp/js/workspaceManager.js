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
    '': 'binary_file_icon'
};

var selectedWorkspace;
var workspaces;

var WorkspaceManager = {
    setSelectedWorkspace: function (wsName) {
        selectedWorkspace = wsName;
        FileApi.setSelectedWorkspace(wsName, function (ts) {
            console.log("WS setted: " + selectedWorkspace);
        });
    },
    getSelectedWorkspace: function () {
        return selectedWorkspace;
    },
    readSelectedWorkspace: function (callback) {

        FileApi.getSelectedWorkspace(function (ts) {
            selectedWorkspace = ts;
            callback(ts);

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

            callback(wss);
        });
    },
    loadWorkspace: function () {
        var workspaceName = WorkspaceManager.getSelectedWorkspace();
        console.log("Loading WS " + workspaceName + " ...");
        FileApi.loadWorkspace(workspaceName, function (ts) {
            $(".wsactions").remove();
            var wsactions = $('#wsActions');
            var wsLabel = $("#editorSidePanelHeaderWorkspaceInfo");
            wsLabel.empty(WorkspaceManager.getSelectedWorkspace());
            wsLabel.append(WorkspaceManager.getSelectedWorkspace());                        
            if ($("#projectsTree").length > 0) {
                var treeStruct = ts;
                $("#projectsTree").dynatree("getRoot").addChild(treeStruct);
                $(".indented.apl_editor_"
                        + WorkspaceManager.getSelectedWorkspace())
                        .parent().addClass("active");
            
            var wsActions = "";
            
            var workspaceDownload =  "<a style=\"cursor: pointer\" id=\"download-ws\" ";
                workspaceDownload += "data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Download workspace as a single zip file\">";
                workspaceDownload += "<span class=\"glyphicon glyphicon-download\" aria-hidden=\"true\"> </span>";
                workspaceDownload += "</a>";
                
            var workspaceEdit =  "<a style=\"cursor: pointer\" id=\"edit-ws\" ";
                workspaceEdit += "data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Edit workspace\">";
                workspaceEdit += "<span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\"> </span></a>";

            var workspaceToDemo =  "<a style=\"cursor: pointer\" id=\"demo-ws\" ";
                workspaceToDemo += "data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Publish as Demo\">";
                workspaceToDemo += "<span class=\"glyphicon glyphicon-cloud-upload\" aria-hidden=\"true\"> </span></a>";


            var workspaceDelete =  "<a style=\"cursor: pointer\" id=\"delete-ws\"";
                workspaceDelete += "data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Delete workspace\">";
                workspaceDelete += "<span class=\"glyphicon glyphicon-trash\" aria-hidden=\"true\"> </span></a>";
                
//            var workspaceScreenshot =  "<a style=\"cursor: pointer\" id=\"screenshot-ws\"";
//                workspaceScreenshot += "data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Screenshot\">";
//                workspaceScreenshot += "<span class=\"glyphicon glyphicon-camera\" aria-hidden=\"true\"> </span></a>";
                           
                wsActions+="<br>&nbsp;&nbsp;"+workspaceEdit+"";
                wsActions+="&nbsp;&nbsp;"+workspaceDownload+"";
                wsActions+="&nbsp;&nbsp;"+workspaceToDemo+"";
                wsActions+="&nbsp;&nbsp;"+workspaceDelete+"<br>";
//                wsActions+="&nbsp;&nbsp;"+workspaceScreenshot+"<br>";
                wsActions+="";
                
                
                $("#wsactions").append(wsActions);          
            
            }
            $(".dynatree-expander").click();
            
            $("#edit-ws").click(function(e) {
                e.preventDefault();
                var oldName = WorkspaceManager.getSelectedWorkspace();
                $("#modalCreationField input").val(oldName);
		showContentAsModal("app/modalWindows/editWorkspace", function() {
			var workspaceName = $("#modalCreationField input").val();
                        var description = $("#descriptionInput textarea").val();
			$("#workspacesNavContainer li").removeClass("active");
			WorkspaceManager.updateWorkspace(workspaceName,description);
			AppPresenter.loadSection("editor", workspaceName, function() {
				WorkspaceManager.loadWorkspace();
			});
		});
            });
            
            $("#download-ws").click(function(e) {
                    e.preventDefault();
                    var name = WorkspaceManager.getSelectedWorkspace();
                    WorkspaceManager.downloadAsZip(name);
            });
            
            $("#delete-ws").click(function(e) {
                    e.preventDefault();
                    WorkspaceManager.deleteWorkspace(WorkspaceManager.getSelectedWorkspace());
            });
            $("#demo-ws").click(function(e) {
                    e.preventDefault();
                    var name = WorkspaceManager.getSelectedWorkspace();
                    WorkspaceManager.publishWorskspaceAsDemo(name);
                    AppPresenter.loadSection("editor", name, function() {
				WorkspaceManager.loadWorkspace();
			});
            });
            $("#screenshot-ws").click(function(e) {
                    e.preventDefault();
                    
                    var name = WorkspaceManager.getSelectedWorkspace();
                    html2canvas($("#editor"), {
                        onrendered: function(canvas) {
                            theCanvas = canvas;
                            document.body.appendChild(canvas);

                            canvas.toBlob(function(blob) {
                                                    saveAs(blob, "Dashboard.png"); 
                                            });
                        }
                    });
            });
                        
        });
    },
    createWorkspace: function (workspaceName,description,tags) {
        FileApi.createWorkspace(workspaceName,description,tags, function (ts) {                        
            if (ts) {
                createWSLine(workspaceName, function () {
                    WorkspaceManager.setSelectedWorkspace(workspaceName);                    
                    WorkspaceManager.loadWorkspace();
                });
                if($("#zipFile").val()!=="")
                        WorkspaceManager.initializeWithZipFile();
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
        var continueHandler = function() {
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
            showModal("Confirm demos delete", 
                      "Your demos for <b>'" + workspaceName +"'</b> will be erased too.<BR/><BR/>\n\
                      <b>Do you want to delete your existing demos?</b><BR/></i>",
                        "Continue", deleteDemosHandler,
                        function(){}, function(){});
        };
        
        var deleteDemosHandler = function() {
                FileApi.deleteDemoWorkspace(workspaceName, function (ts) {
                    if (ts) {
                    } else {
                        showError("Error", "Error deleting workspace", function () {
                            hideError();
                        });
                    }
                });
            hideModal();
            $(location).attr('href',"app/wsm"); 
        };
        showModal("Confirm workspace delete", "The workspace <b>'" + workspaceName +"'</b> will be erased and all data will be lost.<BR/><BR/><b>Do you want to delete the existing workspace?</b><BR/></i>",
						"Continue", continueHandler,
						function(){}, function(){});
        

    },
    
    updateWorkspace: function (workspaceName,description) {
        FileApi.updateWorkspace(workspaceName,description, function (ts) {                        
            if (ts) {
                if(WorkspaceManager.getSelectedWorkspace()!==workspaceName){
                    deleteWSLine(WorkspaceManager.getSelectedWorkspace());
                }
                createWSLine(workspaceName, function () {
                    WorkspaceManager.setSelectedWorkspace(workspaceName);                    
                    AppPresenter.loadSection("editor", workspaceName, function() {
				WorkspaceManager.loadWorkspace();
			});
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
        var continueHandler = function() {
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
        showModal("Confirm workspace delete", "The workspace <b>'" + workspaceName +"'</b> will be erased and all data will be lost.<BR/><BR/><b>Do you want to delete the existing workspace?</b><BR/></i>",
						"Continue", continueHandler,
						function(){}, function(){});

    },
    downloadAsZip: function (workspaceName,callback) {
        FileApi.downloadAsZip(workspaceName);
    },
    initializeWithZipFile: function(){
        var submitButton=$("#uploadSubmit");
        submitButton.click();
    },
    importDemoWorkspace : function(demoWorkspaceName, targetWorkspaceName) {
        CommandApi.importDemoWorkspace(demoWorkspaceName,targetWorkspaceName);
    },
    loadWorkspaceManagementWindow: function(){
        AppPresenter.loadSection("wsm");
    },
    publishWorskspaceAsDemo: function (workspaceName, callback) {
        var continueHandler = function() {
                FileApi.currentWSAsDemoWS(workspaceName, callback);
                hideModal();
        };
        showModal("Confirm publication as demo", "A demo for the workspace <b>'" + workspaceName +"'</b> will be published. \n\\n\
                    <BR/> All data will be accessible from:\n\
                    <BR/><BR/>https://"+window.location.host+"/IDEAS/demo/"+workspaceName+"\n\
                    <BR/><BR/><b>Do you want to create a demo for the existing workspace?</b><BR/></i>",
						"Continue", continueHandler,
						function(){}, function(){});
    },
    updateDemoWorkspace:function (workspaceName, callback) {
        var continueHandler = function() {
                FileApi.updateDemoWorkspace(workspaceName, callback);
                hideModal();
                $(location).attr('href',"app/wsm");             
        };
        showModal("Confirm demo update", "The demo for the workspace <b>'" + workspaceName +"'</b> will be overwritten and new data will be accessible from:\n\
                    <BR/><BR/>https://"+window.location.host+"/IDEAS/demo/"+workspaceName+"\n\
                    <BR/><BR/><b>Do you want to update the demo for the existing workspace?</b><BR/></i>",
						"Continue", continueHandler,
						function(){}, function(){});
    },
    importNewDemoWorkspace:function (workspaceName, callback) {
        var continueHandler = function() {
                FileApi.importNewDemoWorkspace(workspaceName, callback);
                WorkspaceManager.setSelectedWorkspace(workspaceName);
                hideModal();
                $(location).attr('href',"app/editor"); 
        };
        showModal("Confirm clone", "A clone for the demo workspace <b>'" + workspaceName +"'</b> will be created in your workspaces\n\
                    You will be redirected to the editor after the clone action\n\
                    <BR/><BR/><b>Do you want to clone the public demo ?</b><BR/></i>",
						"Continue", continueHandler,
						function(){}, function(){});
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
        AppPresenter.loadSection("editor", wsName, function () {
            WorkspaceManager.loadWorkspace();
        });
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
                AppPresenter.loadSection("editor", wsName, function () {
                    WorkspaceManager.loadWorkspace();
                });
            } catch (err) {
                console.log(err);
            }
        });
    }

};