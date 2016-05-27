/*
 *
 *  
 * 
 */

//------------------------------------------------------------------------------
//      INITIALIZATION FUNCTIONS
//------------------------------------------------------------------------------
// Editor window initialization:
var initializeEditor = function () {
    intializeEditorWindow();
    initializeCommandLine();
    $("#editorSidePanelHeaderAddProject .dropdown-toggle").click(function () {
        MenuManager.setupGlobalMenu("Create $1 file");
    });

    if (localStorage.getItem("demo") == "demo") {
        $("#menuToggler").hide();
        $("#userTab").hide();
        userDemoInfo.onClick();
        if (localStorage.getItem("ws") != "") {
            auxGenerateDemo(localStorage.getItem("ws"));
        }
    }
};

//	location.href='j_spring_security_logout';
//	$(location).attr('href','j_spring_security_logout');


// Initialization of the command line, basically adds the file, project and 
// workspace management commands.
var initializeCommandLine = function () {
    require(['gcli/index', 'demo/index'], function (gcli) {
        gcli.createDisplay();
        gcli.addCommand(CommandsRegistry.generateTemplateWorkspace);
        gcli.addCommand(CommandsRegistry.generateTemplateWorkspaceWithDestination);
        gcli.addCommand(CommandsRegistry.generateDemoWorkspace);
        gcli.addCommand(CommandsRegistry.generateDemoWorkspaceWithDestination);
        gcli.addCommand(CommandsRegistry.checkDocument);
        gcli.addCommand(CommandsRegistry.checkCurrentDocument);
        gcli.addCommand(CommandsRegistry.openFile);
        gcli.addCommand(CommandsRegistry.closeFile);
        gcli.addCommand(CommandsRegistry.deleteNode);
        gcli.addCommand(CommandsRegistry.renameNode);
        gcli.addCommand(CommandsRegistry.move);
        gcli.addCommand(CommandsRegistry.copy);
        gcli.addCommand(CommandsRegistry.echo);
        gcli.addCommand(CommandsRegistry.deleteCurrentWorkspace);
        gcli.addCommand(CommandsRegistry.deleteWorkspace);
        gcli.addCommand(CommandsRegistry.testModule);
        gcli.addCommand(CommandsRegistry.testModules);
        gcli.addCommand(CommandsRegistry.convertCurrentWorkspacetoDemo);
        gcli.addCommand(CommandsRegistry.convertCurrentWorkspacetoTemplate);
        gcli.addCommand(CommandsRegistry.clearConsole);
    });
};

// Important global editor variables:
var currentSelectedNode = null;
var previousSizes = new Object();
var maximizing = false;
var intializeEditorWindow = function () {

    //For demo user, session expired
    window.onload = function () {
        window.onbeforeunload = confirmExit;
    }
    function confirmExit() {
        if (localStorage.getItem("demo") == "demo") {
            $.ajax("j_spring_security_logout", {
                "type": "get",
                "success": function (result) {
                    console.log("logOut");
                },
                "error": function (result) {
                    console.error("logOut fail");
                },
                "async": true,
            });
            return "You tried to leave this page, your changes will be lost. Are you sure want to leave this page? ";
        }
    }

    AppPresenter.setCurrentSection("editor");

    // Hide the editor during loading:
    $("editor").css("visibility", "hidden");

    // Setup editor content loading:
    $("#projectsTree").dynatree({
        onActivate: function (node) {
            console.log("Activated node: " + node);
            currentSelectedNode = node;
            if (!node.data.isFolder) {
                var uri = WorkspaceManager.getSelectedWorkspace() + "/" + FileApi.calculateNodeUri(node);
                EditorManager.openFile(uri);
            }
            ;
        }
    });

    // Setup editor interface and editor window events:
    $("#editorSidePanelHeaderAddProject .dropdown-toggle").click(function () {
        MenuManager.setupGlobalMenu("Create $1 file");
    });
    $("#editorSidePanel").resizable({
        maxWidth: 900,
        minWidth: 150,
        handles: "e"
    });
    $("#editorSidePanel").resize(fitEditorMainPanel);
    $("#editorItself").resizable({
        maxHeight: 800,
        minHeight: 51,
        handles: "s"
    });
    $("#editorItself").resize(fitBottomPanel);
    $("#editorInspector").resizable({
        maxWidth: 900,
        minWidth: 150,
        handles: "w"
    });
    $("#editorInspector").resize(function () {
        $("#editorInspector").css("left", "0");
        fitEditorMainPanel();
    });
    $(window).resize(fitEditor);
    $("#editorMaximize").click(maximize);
    $("#editorToggleInspector").click(toggleInspector);
    //share Document
    $("#shareDocument").click(function () {
        $("#shareDocumentModal").show();
        $("#mailContent").val(EditorManager.getCurrentEditorContent());
    });
    $("#shareDocClosed").click(function () {
        $("#shareDocumentModal").hide();
    });
    $("#sendMail").click(function () {
        var to = $("#mailTo").val();
        var content = $("#mailContent").val();
        share(to, content);
        $("#shareDocumentModal").hide();
    });
    // Finally we show and fit the editor :
    fitEditor();
};

//------------------------------------------------------------------------------
//      FILE MANAGEMENT FUNCTIONS
//------------------------------------------------------------------------------

// File name management:
//share document
function share(dst, mailContent) {
    $.ajax({
        type: "POST",
        url: "app/share",
        data: {to: dst, content: mailContent},
    });
}

//      Normalization:
var special = "%&@#¬/()=?¿¡!|ºª";
function checkerName(e) {
    var disable = true;
    var key = e.keyCode || e.which;
    var letra = String.fromCharCode(key);
    var createBtn = $(".btn-primary");
    var input = $(".form-control");

    for (var i in special) {
        var aux = special[i];
        if (aux == letra) {
            disable = true;
            break;
        }
        if (input.val().indexOf(aux) == -1) {
            disable = false;
        } else {
            disable = true;
            break;
        }
    }
    if (disable) {
        var div = $(".modal-body");
        createBtn.addClass('disabled');
        var label = $('#keyFail');
        if (label[0] == "undefined" || label[0] == undefined)
            div.append("<label id='keyFail' style='color: red'>Please don't use the special character</label>");
    } else {
        var label = $('#keyFail');
        if (label)
            label.remove();

        createBtn.removeClass('disabled');
        disable = false;
    }
}

// Gets the filename from the full path in the three node:
function extractFileName(fullpath) {
    var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
    var filename = fullPath.substring(startIndex);
    if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
        filename = filename.substring(1);
    }
    return filename;
}

// Gets the extension from a filename:
function extractFileExtension(filename) {
    var startIndex = filename.indexOf('.');
    var result = "";
    if (filename[startIndex] === '.')
        result = filename.substring(startIndex + 1);
    return result;
}

// Gets the corresponding icon given a file extension:
function getIconName(fileExtension) {
    var result = extensionsIcons[fileExtension.toLowerCase()];
    if (result == null)
        result = extensionsIcons[""];
    return result;
}

function saveEditorToFile(fileUri, code) {
    FileApi.saveFileContents(fileUri, code, function (ts) {
        if (ts == true) {
            console.log("File " + fileUri + " saved correctly.");
        }
    });
}


// Panels resizing:
var fitEditorMainPanel = function () {
    $("#editorMainPanel").width($(window).width() - $("#editorSidePanel").width() - $("#editorInspector").width());
    $("#editorMainPanel").css("right", $("#editorInspector").width() + "px");
};

var fitBottomPanel = function () {
    $("#editorBottomPanel").height(
            $(window).height() - $("#appHeader").height()
            - $("#appFooter").height()
            - $("#editorItself").height() + "px");

    if (document.editor != null)
        document.editor.resize(true);
};

var fitEditor = function () {
    fitEditorMainPanel();
    fitBottomPanel();
    $("#editorItself").resizable(
            "option",
            "maxHeight",
            $(window).height() - $("#appHeader").height()
            - $("#appFooter").height() - 52);
};


// Maximization

var maximize = function () {
    if (!maximizing) {
        maximizing = true;
        toggleMaximization();
    }
};

var toggleMaximization = function () {
    var appMainContent = $("#appMainContent");
    var editorSidePanel = $("#editorSidePanel");
    var editorMainPanel = $("#editorMainPanel");
    var max_min = $("#editorMaximize");

    // Fit inspector content
    setTimeout(function () {
        DescriptionInspector.inspectorContent.resize();
        // Model tab content
        $("#inspectorModelContent").show().height(
            $("#editorInspectorLoader").height() - $("#appFooter").height() - $("ul#editorTabs.inspectorTabs").height() - 12
        );
    }, 500);

    if (appMainContent.hasClass("maximizedEditor")) {
        appMainContent.removeClass("maximizedEditor");

        editorSidePanel.css("max-width", +"0px");
        editorMainPanel.css("min-width", editorMainPanel.width() + "px");
        editorMainPanel.css("width", previousSizes.editorWidth + "px");
        setTimeout(function () {
            editorSidePanel.css("max-width", previousSizes.sidePanelWidth + 2 + "px");
// 		    		editorMainPanel.css("max-width", previousSizes.editorWidth + "px");
// 		    		editorSidePanel.css("min-width", previousSizes.sidePanelWidth + "px");
            editorMainPanel.css("min-width", previousSizes.editorWidth + "px");
            setTimeout(
                    function () {
                        editorSidePanel.css("max-width", "100%");
                        editorMainPanel.css("max-width", "");
                        editorSidePanel.css("min-width", "");
                        editorMainPanel.css("min-width", "0px");
                        fitEditor();
                    }, 900);
        }, 1);

        max_min.removeClass("minimize");

        setTimeout(function () {
            maximizing = false;
        }, 900);

    } else {
        appMainContent.addClass("maximizedEditor");

        previousSizes.sidePanelWidth = editorSidePanel.width();
        previousSizes.editorWidth = editorMainPanel.width();

        editorSidePanel.css("max-width", editorSidePanel.width());
        editorMainPanel.css("min-width", editorMainPanel.width());
        setTimeout(function () {
            editorSidePanel.css("max-width", "0px");
            editorMainPanel.css("min-width", $(window).width() - $("#editorInspector").width());
        }, 1);

        max_min.addClass("minimize");

        setTimeout(function () {
            maximizing = false;
            editorMainPanel.css("width", $(window).width() - $("#editorInspector").width());
            editorMainPanel.css("min-width", "");
// 	    			fitEditorMainPanel();
        }, 700);
    }
};

var toggleInspector = function () {
    var inspector = $("#editorInspector");
    if (inspector.hasClass("hdd")) {
        EditorManager.showInspector();
    } else {
        EditorManager.hideInspector();
    }

    setTimeout(function () {
        inspector.css("max-width", "");
        fitEditorMainPanel();
    }, 1);
};
