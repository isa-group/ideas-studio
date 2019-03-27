//------------------------------------------------------------------------------
//    Menu Items
//------------------------------------------------------------------------------

var newDirItem = {
    elementClass: "createNewDirectory",
    folderDisabled: true,
    titleCode: "editor.actions.modal.create_directory.title",
    buttonCode: "editor.actions.modal.create_directory.button",
    contentUrl: "app/modalWindows/createNewDirectory",
    onClick: function () {
        var estate = $('.createNewDirectory');
        if (!(estate.parent().attr('class') == 'disabled')) {
            genericMenuOption(newDirItem);
        }
    },
    onCreate: function () {
        var folderName = $("#modalCreationField input").val(), nodeUri;
        
        // Create a folder only inside a folder node
        if (!currentSelectedNode.data.isFolder) currentSelectedNode = currentSelectedNode.getParent();
        
        nodeUri = FileApi.calculateNodeUri(currentSelectedNode);
        // Folder already exists
        if (!!getNodeByFileUri(WorkspaceManager.getSelectedWorkspace() + "/" + nodeUri + "/" + folderName)) {
            hideModal();
            showError("There was an error", "Error creating new directory.<br>" +
            "Please, check if a directory with that name already exists in the workspace.", function () {
                hideError();
            });
        } else {
            try {
                if (extractFileExtension(nodeUri)) {
                    // Get node parent
                    nodeUri = nodeUri.substring(0, nodeUri.lastIndexOf("/")).substring(0, nodeUri.lastIndexOf("/"));
                }
            } catch (err) {
                // extractFileExtension fails if nodeUri doesn't include a fileUri
            }
            var folderUri = WorkspaceManager.getSelectedWorkspace() + "/" + nodeUri + "/" + folderName;
            FileApi.createDirectory(folderUri, function (ts) {
                if (ts == true || ts == "true") {
                    console.log("Directory created: " + ts);
                    var keyPath = nodeUri + "/" + folderName;
                    var newChild = buildChild(folderName, true, "folder_icon", keyPath);
                    hideModal();
                    currentSelectedNode.addChild(newChild);
                    currentSelectedNode.sortChildren();
                    //currentSelectedNode.data.keyPath =  nodeUri + "/" + fileName + languageExtension;
                } else {
                    if (!$("#modalCreationField input").val()) {
                        hideModal();
                        showError("Warning", "Please enter a directory name.", function () {
                            hideError();
                            genericMenuOption(newDirItem);
                        });
                    } else {
                        hideModal();
                        showError("There was an error", "Error creating new directory.<br>" +
                                "Please, check if a directory with that name already exists in the workspace.", function () {
                                    hideError();
                                });
                    }
                }
            });
        }
    },
    onCancel: function () {
        hideModal();
    }
};


var newProjectItem = {
    elementClass: "createNewProject",
    folderDisabled: false,
    titleCode: "editor.actions.modal.create_project.title",
    buttonCode: "editor.actions.modal.create_project.button",
    contentUrl: "app/modalWindows/createNewProject",
    onClick: function () {
        genericMenuOption(newProjectItem);
    },
    onCreate: function () {
        var projectName = $("#modalCreationField input").val();
        var projectTemplateName = $("#template-from-module").val();
        var projectUri = WorkspaceManager.getSelectedWorkspace() + "/" + projectName;
        FileApi.createProject(projectUri, function (ts) {
            if (ts == true || ts == "true") {
                if (projectTemplateName != undefined) {
                    var selectedOption = $("#template-from-module").find(":selected");
                    var selectedOptgroup = selectedOption.parent();
                    var languageId = selectedOptgroup[0].id;
                    languageId = languageId.replace("-optgroup", "");
                    instantiateProjectFromTemplate(languageId, projectTemplateName, projectName, projectUri);
                } else {
                    var newChild = buildChild(projectName, true, "project_icon");
                    var parentNode = $("#projectsTree").dynatree("getRoot");
                    parentNode.addChild(newChild);
                }
                hideModal();
             } else {
                if (!$("#modalCreationField input").val()) {
                    hideModal();
                    showError("Warning", "Please enter a project name.", function () {
                        hideError();
                        genericMenuOption(newProjectItem);
                    });
                } else {
                    hideModal();
                    showError("There was an error", "Error creating new project.<br>" +
                            "Please, check if a project with that name already exists in the workspace.", function () {
                                hideError();
                            });
                }
            }

        });
    },
    onCancel: function () {
        hideModal();
    }
};

var uploadFileItem = {
    elementClass: "uploadFile",
    folderDisabled: true,
    titleCode: "editor.actions.modal.upload_file.title",
    buttonCode: "editor.actions.modal.upload_file.button",
    contentUrl: "app/modalWindows/uploadFile",
    onClick: function () {
        var estate = $('.uploadFile');
        if (!(estate.parent().attr('class') == 'disabled')) {
            genericMenuOption(uploadFileItem);
        }
        //genericMenuOption(uploadFileItem);
    },
    onCreate: function () {
        hideModal();
    },
    onCancel: function () {
        hideModal();
    }
};

var downloadFileItem = {
    elementClass: "DownloadFile",
    folderDisabled: true,
    titleCode: "editor.actions.modal.download_file.title",
    buttonCode: "editor.actions.modal.downloadload_file.button",
    contentUrl: "app/modalWindows/uploadFile",
    onClick: function () {
        var estate = $('.uploadFile');
        if (!(estate.parent().attr('class') == 'disabled')) {
            genericMenuOption(uploadFileItem);
        }
        //genericMenuOption(uploadFileItem);
    },
    onCreate: function () {
        hideModal();
    },
    onCancel: function () {
        hideModal();
    }
};

var menuSeparator = 0;

//------------------------------------------------------------------------------
//  Menu Manager Object:
//------------------------------------------------------------------------------
var MenuManager = {
    languageFileItem: "",
    genericMenuItems: [menuSeparator, newDirItem, newProjectItem, menuSeparator, uploadFileItem],
    buildMenu: function (menu, languageFileMessage) {
        menu.empty();
        // Read dinyamically the potential language files
        $.map(ModeManager.getModes(), function(value, index) {return [value];})
                .sort(function (a,b) {return a.name.toLowerCase() > b.name.toLowerCase() ? 1: a.name.toLowerCase() < b.name.toLowerCase() ? -1: 0;}).forEach(function (obj) {
            var languageId = obj.id;
            var languageMode = ModeManager.getMode(languageId);
            var languageName = languageMode.name;
            languageExtension = ModeManager.getExtension(languageId, null);

            console.log(languageName, languageExtension);

            if (languageName == null || languageName == undefined)
                languageName = language.extension;

            var msg = languageFileMessage.replace("$1", languageName);
            var $newLanguageFileElem = $('<li><a class="createNewLanguageFile">' + msg + '</a></li>');
            MenuManager.languageFileItem = $newLanguageFileElem;
            menu.append($newLanguageFileElem);

            setupClickForNewFile($newLanguageFileElem, languageName, languageId, languageExtension);
        });

        var $itemElement = null;
        for (var myItem in this.genericMenuItems) {
            var item = this.genericMenuItems[myItem];
            if (item === menuSeparator) {
                menu.append($('<li class="divider"></li>'));
            } else {
                $itemElement = $('<li><a class="' + item.elementClass + '">' + menuMessages[item.elementClass] + '</a></li>');
                $itemElement.click(item.onClick);
                menu.append($itemElement);
            }
        }
    },
    enableOrDisableOptions: function (menu) {
        if (currentSelectedNode == null) {
            $(".createNewLanguageFile").parent().addClass("disabled");
            for (var myItem in this.genericMenuItems) {
                var item = this.genericMenuItems[myItem];
                if (item !== menuSeparator && item.folderDisabled) {
                    $itemElement = $("." + item.elementClass).parent();
                    $itemElement.addClass("disabled");
                }
            }
        } else {
            $(".createNewLanguageFile").parent().removeClass("disabled");
            for (var myItem in this.genericMenuItems) {
                var item = this.genericMenuItems[myItem];
                if (item !== menuSeparator && item.folderDisabled) {
                    $itemElement = $("." + item.elementClass).parent();
                    $itemElement.removeClass("disabled");
                }
            }
        }
    },
    setupGlobalMenu: function (languageFileMessage) {
        var menu = $("#editorSidePanelHeaderAddProject .dropdown-menu");
        var languageExtension = "";
        this.buildMenu(menu, languageFileMessage);
        this.enableOrDisableOptions(menu);
    }
};


var genericMenuOption = function (menuItem) {
    showContentAsModal(menuItem.contentUrl, menuItem.onCreate, menuItem.onCancel);
};

var setupClickForNewFile = function ($newLanguageFileElem, languageName, languageId, languageExtension) {
    $newLanguageFileElem.click(function () {
        if (!$(".createNewLanguageFile").parent().hasClass("disabled")) {
            showContentAsModal("app/modalWindows/createNewFileOfLanguage?language=" + languageName + "&languageId=" + languageId,
                function () {
                    createNewFile(languageId, languageExtension);
                });
        }
    });
};

var createNewFile = function (languageId, languageExtension,callback) {
    var fileName = $("#modalCreationField input").val(),
        fileExt = $("#modalCreationField .extension").text().split(".")[1];
    console.log("result-->" + nameFileChecker(fileName, fileExt));
    if (nameFileChecker(fileName, fileExt)) {

        var templateName = $("#template-file").val();

        var node = currentSelectedNode;

        tittleAsArray = (currentSelectedNode.data.title).split(".");
        if (tittleAsArray.length > 1) {
            parent = currentSelectedNode.getParent();
            if (parent != null) {
                node = parent;
            }
        }

        var nodeUri = FileApi.calculateNodeUri(node);

        fileUri = WorkspaceManager.getSelectedWorkspace() + "/" + nodeUri + "/" + fileName + languageExtension;
        FileApi.createFile(fileUri, function (ts) {
            if (ts == true || ts == "true") {
                var newChild;
                var keyPath = nodeUri + "/" + fileName + languageExtension;
                if (languageExtension == ".sedl") {
                    newChild = buildChild(fileName + languageExtension, false, "sedl_icon", keyPath);
                } else if (languageExtension == ".iagreetemplate" || languageExtension == ".iagreeoffer" || languageExtension == ".ttl") {
                    newChild = buildChild(fileName + languageExtension, false, "binary_file_icon", keyPath);
                } else {
                    newChild = buildChild(fileName + languageExtension, false, "file_icon", keyPath);
                }

                hideModal();
                node.addChild(newChild);
                node.sortChildren();
            }
            if (templateName && templateName != "")
                instantiateFileContentFromTemplate(languageId, templateName, fileUri);
            else {
                if(callback)
                    callback(fileUri);
                else
                    EditorManager.openFile(fileUri);
                console.log("---->>" + fileUri);
            }
            
        });
    } else {
        alert("Please choose another name, this already exist.");
    }

};

var nameFileChecker = function (newName, newExt) {
    var activeNode = $("#projectsTree").dynatree("getActiveNode"),
        result = true;

    if (activeNode != null) {

        var parentFolder = activeNode.data.isFolder ? activeNode : activeNode.getParent(),
            childlist = parentFolder.childList;

        if (childlist != null) {
            for (var i = 0; i < childlist.length; i++) {
                var child = childlist[0].data.title.split(".");
                if (child[0] == newName && child[1] == newExt) {
                    result = result && false;
                    break;
                }
            }
        }
    }

    return result;
};

function instantiateFileContentFromTemplate(languageId, templateName, fileUri)
{
    $.ajax({"url": ModeManager.idUriMap[languageId] + '/template/document/' + templateName,
        success: function (result, textStatus, request) {
            console.log("Content of the template '" + templateName + "':" + result);
            FileApi.saveFileContents(fileUri, result, function (result) {
                $.ajax({"url": ModeManager.idUriMap[languageId] + '/template/dependences/document/'+ templateName,
                success: function (result, textStatus, request) {
                    console.log("Dependences of the template '" + templateName + "':" + result);
                    var mytemplates;
                    var node = currentSelectedNode;
                    var nodeUri = FileApi.calculateNodeUri(node);
                    var newChild;
                    var keyPath;
                    var fileName=fileUri.substring(fileUri.lastIndexOf('/')+1);
                    var index=0;
                    fileName=fileName.replace(".sedl","");
                    mytemplates=result;
                    if(mytemplates !== null && mytemplates.length != 0){                    
                        for(index=0;index<mytemplates.length;index++){ 
                            (function (j){
                            $.ajax({"url": ModeManager.idUriMap[languageId] + '/template/document/'+ mytemplates[j],
                                success: function (result, textStatus, request) {                                    
                                     var newFileName=mytemplates[j].replace(templateName.replace(".sedl",""),fileName);
                                     var dependenceFileUri = WorkspaceManager.getSelectedWorkspace() + "/" + nodeUri + "/" + newFileName;                                        
                                        (function (fname,furi,processed){
                                        FileApi.createFile(furi, function (ts) {
                                            if(ts){
                                                keyPath = nodeUri + "/" + fname;
                                                newChild = buildChild(fname, false, "file_icon", keyPath);
                                                node.addChild(newChild);
                                                node.sortChildren();
                                                FileApi.saveFileContents(furi, result, function (result) {
                                                    if(processed>=mytemplates.length-1)
                                                        EditorManager.openFile(fileUri);
                                            
                                                });
                                            }
                                        
                                     });})(newFileName,dependenceFileUri,j);
                                     }
                                 });
                        })(index);
                        }
                    }
                }
                });                
            });
            
        }
    });
}

function instantiateProjectFromTemplate(languageId, templateName, projectName, projectUri)
{
    $.ajax({"url": ModeManager.idUriMap[languageId] + '/template/project/' + templateName,
        success: function (result, textStatus, request) {
            console.log("Structure of the template project '" + templateName + "':" + result);
            projectStructure = eval("(" + result + ")");
            projectStructure.title = projectName;
            projectStructure.tooltip = "Project created from the template: '" + languageId + ">" + templateName + "'";
            instantiateProjectElementsFromTemplateStructure(languageId, templateName, projectName, projectUri, projectStructure.children);
            $("#projectsTree").dynatree("getRoot").addChild(projectStructure);
        }
    });
}

function instantiateProjectElementsFromTemplateStructure(languageId, templateName, projectName, projectUri, projectStructure)
{
    var projectItem;
    var name;
    for (var i = 0; i < projectStructure.length; i++)
    {
        projectItem = projectStructure[i];
        name = projectItem.title
        projectItem.keyPath = projectItem.keyPath.replace(templateName, projectName);
        var uri = WorkspaceManager.getSelectedWorkspace() + "/" + projectItem.keyPath;
        if (projectItem.isFolder)
        {
            FileApi.createDirectory(uri,
                (function (projectItem, uri) {
                    return function (result) {
                        if (projectItem.tooltip && projectItem.tooltip != "") {
                            FileApi.createFile(uri + "/.description.txt", function (result) {
                                FileApi.saveFileContents(uri + "/.description.txt", projectItem.tooltip, function (result) {});
                            });

                        }
                        if (projectItem.children && projectItem.children.lenght != 0) {
                            instantiateProjectElementsFromTemplateStructure(languageId, templateName, projectName, projectUri, projectItem.children);
                        }
                    }
                })(projectItem, uri));
        } else {
            FileApi.createFile(uri, (function (projectItem, uri) {
                return function (result) {
                    var templateUri = ModeManager.getBaseUri(languageId) + "/template/project/" + templateName + "/" + projectItem.keyPath.replace(projectName, "");
                    $.ajax({"url": templateUri,
                        "success": function (result2) {
                            console.log(">template file contents: '" + result2 + "'");
                            FileApi.saveFileContents(uri, result2, function (result3) {});
                        },
                        "error": function (result2) {
                            console.log("Error while getting >template file contents: '" + result2 + "'");
                        }
                    });
                }
            })(projectItem, uri));
        }
    }
}

function buildChild(nodeName, isFolder, nodeIcon, keyPath) {
    var str = "{";
    str += "\"title\":\"" + nodeName + "\",";
    str += "\"icon\":" + nodeIcon + ",";
    str += "\"isFolder\":" + isFolder + ",";
    str += "\"keyPath\":\"" + keyPath + "\",";
    str += "\"children\": []";
    str += "}";
    return eval('(' + str + ')');
}

