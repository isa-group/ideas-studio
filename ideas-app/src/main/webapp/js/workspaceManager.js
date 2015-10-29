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
            var wsLabel = $("#editorSidePanelHeaderWorkspaceInfo");
            wsLabel.empty(WorkspaceManager.getSelectedWorkspace());
            wsLabel.append(WorkspaceManager.getSelectedWorkspace());                        
            if ($("#projectsTree").length > 0) {
                var treeStruct = ts;
                $("#projectsTree").dynatree("getRoot").addChild(treeStruct);
                $(".indented.apl_editor_"
                        + WorkspaceManager.getSelectedWorkspace())
                        .parent().addClass("active");
                var workspaceDownload = "<div id='downloadAsZip'>"
                workspaceDownload +=    "   <a href=\"file/getAsZip/"+WorkspaceManager.getSelectedWorkspace()+"\" ";
                workspaceDownload +=    "           data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Download workspace as a single zip file\">";
                workspaceDownload +=    "       Download workspace";
                workspaceDownload +=    "       <span class=\"glyphicon glyphicon-download\" aria-hidden=\"true\"></span>";
                workspaceDownload +=    "   </a>";
                workspaceDownload +=    "</div>";
                $("#projectsTree").append(workspaceDownload);
            }
            EditorManager.reset();
            $(".dynatree-expander").click();
        });
    },
    createWorkspace: function (workspaceName) {
        FileApi.createWorkspace(workspaceName, function (ts) {                        
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
                    WorkspaceManager.loadWorkspace(WorkspaceManager.getSelectedWorkspace());
                });
            }
            hideModal();

        });
    },
    deleteWorkspace: function (workspaceName, callback) {
        FileApi.deleteWorkspace(workspaceName, function (ts) {
            if (ts) {
                deleteWSLine(workspaceName);
            } else {
                showError("Error", "Error deleting workspace", function () {
                    hideError();
                });
            }
            callback(ts);
        });

    },
    downloadAsZip: function (workspaceName) {
        FileApi.downloadAsZip(workspaceName, function (ts) {
        });
    },
    initializeWithZipFile: function(){
        var submitButton=$("#uploadSubmit");
        submitButton.click();
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