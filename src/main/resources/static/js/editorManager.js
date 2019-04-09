// Manager
var checkerTimer;
var operationArray = [];
var fileUriOperation = "";
var opMap = "";
var advModeOperationOriginalIndexes = [];

var saveCurrentSession = function (callback) {
    if (document.editor != null
            && (EditorManager.currentUri in EditorManager.sessionsMap)) {
        saveSession(EditorManager.currentUri, callback);
    } else {
        if (callback) callback();
    }
};

var saveSession = function (uri, callback) {
    if (document.editor != null) {
        // TODO!!!
        FileApi.saveFileContents(uri, 
            EditorManager.sessionsMap[uri].getBaseSession().getValue(),
            callback
        );
    }
};

var createNewTabbedInstance = function (fileUri, content) {
    var sessionAg = ModeManager.createSessionAggregationForEditor(fileUri,
            content);
    if (sessionAg == null) {
        var win = window.open("files/get/" + fileUri, "_blank");
        if (win) {
            // Browser has allowed it to be opened
            win.focus();
        } else {
            // Broswer has blocked it
            alert('Please allow popups for this site');
        }
        return null;
    }

    var id = "et$" + fileUri.replace(/ /g, "_").replace(/\//g, "$");
    var auxArr = fileUri.split("/");
    var name = auxArr[auxArr.length - 1];
    var newTab = $("<li><a id=\"" + id + "\" class=\"editorTab\">" + name
            + "</a></li>");
    var newTabCloseCross = $("<span class=\"glyphicon glyphicon-remove\"></span>");

    newTab.attr('title', fileUri);

    $("#editorTabs").prepend(newTab);
    newTab.append(newTabCloseCross);

    EditorManager.tabsMap[fileUri] = newTab;

    EditorManager.sessionsMap[fileUri] = sessionAg;

    // Select tab
    newTab.click(function () {
        if (fileUri != EditorManager.currentUri)
            EditorManager.openFile(fileUri);
    });

    // Close tab
    newTabCloseCross.click(function (event) {
        event.stopPropagation();
        EditorManager.closeFile(fileUri);
    });

    newTab.css("bottom", "-27px");
    newTab.css("max-width", "0%");
    setTimeout(function () {
        newTab.css("bottom", "0px");
        newTab.css("max-width", "100%");
    }, 1);

    ContextAction.register(newTab, [{
            name: "Close",
            action: newTabCloseCross.click
        }]);
    return newTab;

};

var checkSyntaxFlag = false;

var mayCheckLanguageSyntax = function (fileUri, callback) {
    var agSession = EditorManager.sessionsMap[fileUri];
    if (agSession == undefined || agSession == "undefined") {
        CommandApi.echo("Please open the document you want to check before applying the command");
    } else {
        EditorCheckerIcon.loading();
        
        var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();

        var checkLanguageUri = agSession.getCheckLanguageURI(currentFormat);

        if (checkLanguageUri !== undefined && checkLanguageUri !== null && checkLanguageUri.length > 0) {

            var content = agSession.getBaseSession().getValue();

            content = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentSession().getValue();

            CommandApi.checkModel(content, currentFormat, checkLanguageUri, fileUri, function (ts) {
                console.log("Checking syntax... " + ts);
                if (ts.status === "OK" || ts === "true") {
                    console.log("Syntax is OK.");
                    
                    EditorManager.setAnnotations(eval('(' + "[]" + ')'));
                    DescriptionInspector.onEditorCheckedLanguage();
                    checkSyntaxFlag = true;
                    
                    if (callback) callback();
                } else {
                    console.log(ts);
                    EditorManager.setAnnotations(ts.annotations);
                    EditorCheckerIcon.error(({type: "syntax", annotations: ts.annotations}));
                    checkSyntaxFlag = false;
                }

            });
        } else {
            
            if (callback) callback();
            
        }
    }
};
var mayCheckLanguageConsistency = function (fileUri, callback) {
    var agSession = EditorManager.sessionsMap[fileUri];
    if (agSession == undefined || agSession == "undefined") {
        CommandApi.echo("Please open the document you want to check consistency before applying the command");
    } else {
        var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();
        var consistencyCheckLanguageUri = agSession.getCheckConsistencyLanguageURI(currentFormat);

        if (consistencyCheckLanguageUri !== undefined && consistencyCheckLanguageUri !== null && consistencyCheckLanguageUri.length > 0) {

            var content = agSession.getBaseSession().getValue();

            content = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentSession().getValue();

            CommandApi.checkModel(content, currentFormat, consistencyCheckLanguageUri, fileUri, function (ts) {
                console.log("Checking consistency... " + ts);
                if (ts.status === "OK" || ts === "true") {
                    console.log("Consistency is OK.");
                    EditorManager.setAnnotations(eval('(' + "[]" + ')'));
                    DescriptionInspector.onEditorCheckedLanguage();
                    EditorCheckerIcon.success();
                    checkSyntaxFlag = true;
                } else {
                    console.log(ts);
                    EditorManager.setAnnotations(ts.annotations);
                    EditorCheckerIcon.error({type: "consistency", annotations: ts.annotations});
                    checkSyntaxFlag = false;
                }
                
                if (callback) callback();

            }, function (err) {
                EditorCheckerIcon.error({type: "consistency", annotations: [{type: "error", row: "1", column: "1", text: err}]});
            });
        } else {
            EditorCheckerIcon.success();
        }
    }
};

var initAceEditor = function () {
    ace.require("ace/ext/language_tools");
    var editor = ace.edit("editor");
    editor.setOptions({
        enableBasicAutocompletion: true
    });
    document.editor = editor;
    // editor.setTheme("ace/theme/SEDL4People"); //TODO: change theme depending
    // of mode
    editor.setShowPrintMargin(false);

    document.sessionsMap = EditorManager.sessionsMap;
};

var loadExistingTabbedInstance = function (fileUri, content) {
    
    if( ($scope && !!$scope.model && typeof $scope.model === "object") || DescriptionInspector.existCurrentAngularFile())
        Binding.clearModel();

    if (document.editor == null) {
        
        initAceEditor();

        document.editor
                .on(
                        "change",
                        function (changesDeltas) {                            
                            if (checkerTimer)
                                clearTimeout(checkerTimer);                                                            
                            checkerTimer = setTimeout(
                                    function () {
                                        if (fileUri != "") {                                            
                                            var content = EditorManager.sessionsMap[EditorManager.currentUri]
                                                    .getBaseSession()
                                                    .getValue();
                                            if(content!="")
                                                EditorManager.saveFile(
                                                    EditorManager.currentUri,
                                                    content);
                                        }
                                        
                                        mayCheckLanguageSyntax(EditorManager.currentUri, function () {
                                            DescriptionInspector.editorContentToModel();
                                            mayCheckLanguageConsistency(EditorManager.currentUri);
                                        });
                                        
                                        
                                        

                                    }, 3000);

                        });

        DescriptionInspector.loaders.onInitAceEditor();

    }

    $("editor").css("visibility", "visible");

    if (EditorManager.currentUri != null) {
        $(EditorManager.tabsMap[EditorManager.currentUri])
                .removeClass("active");
    }

    oldUri = EditorManager.currentUri;

    EditorManager.currentUri = fileUri;

    $(EditorManager.tabsMap[EditorManager.currentUri]).addClass("active");

    document.editor
            .setSession(EditorManager.sessionsMap[EditorManager.currentUri]
                    .getCurrentSession());

    // Formats tabs

    var editorFormatsElement = $("#editorFormats");
    editorFormatsElement.empty();
    var count = 0;
    for (var key in EditorManager.sessionsMap[EditorManager.currentUri]
            .getFormatsSessions()) {
        drawNewFormatLiElement(key, EditorManager.currentUri,
                editorFormatsElement);
        count++;
    }

    if (count <= 1 &&
            (DescriptionInspector.isCurrentDescriptionFile() ||
                    !DescriptionInspector.existCurrentDescriptionFile())) {
        $("#editorItself").removeClass("multiformat");
    } else {
        $("#editorItself").addClass("multiformat");
    }

    currentFormat = EditorManager.sessionsMap[EditorManager.currentUri]
            .getCurrentFormat();
    $("#editorFormats ." + currentFormat).addClass("active");

    // Operation buttons

    var buttonPanel = $('#editorActions');
    buttonPanel.empty();

    var divContent = $('<div id="selectOperationDiv" class="btn-group"></div>');
    var divContentNonLanguageOpts = $('<div id="nonLanguageButtons" class="btn-group">');
    var optButton = $('<a class="btn" id="selectOperation" style="display:none;">Analysis report</a>');
    optButton.click(function () {
        loadOperations.onClick();
    });
//    var expandButton = $('<button class="btn btn-link" style="{float:left}" id="expandConsole"><span class="glyphicon glyphicon-chevron-up"></span></button>');
//    expandButton.click(function () {
//        DescriptionInspector.expandConsole('second');
//    });
    var clearButton = $('<button class="btn btn-link" style="{float:left}" id="clearConsole"><span class="glyphicon glyphicon-remove-sign"></span></button>');
    clearButton.click(function () {
        DescriptionInspector.clearConsole();
    });
    divContentNonLanguageOpts.append(optButton);
//    divContentNonLanguageOpts.append(expandButton);
    divContentNonLanguageOpts.append(clearButton);
    divContent.append(divContentNonLanguageOpts);

    var caret = $('<button class="btn btn-primary opButton" data-toggle="dropdown">...</span><span class="sr-only">Toggle Dropdown</span></button>');
    var caretUL = $('<ul id="ulOperationsTypes" class="dropdown-menu scrollable-op-menu" role="menu"></ul>');
    var caretLI = "";

    fileUriOperation = EditorManager.getCurrentUri();
    var opId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri));
    opMap = ModeManager.getOperations(opId);
    var model = ModeManager.getMode(opId);

    var ops_name_length = 0;
    var use_caret = false;
    var ops = [];
    if (opMap && typeof opMap != "undefined") {
        
        // Declare a collapse element to set all secondary operation
        var DEFAULT_COLLAPSED_OPERATION_VALUE = "Other operations...";
        var opVisibility = $(
            '<div id="opGrouperContainer" class="form-group opButton">'+
                '<select class="selectpicker" style="cursor:pointer;" data-style="btn-secondary">'+
                '<option>' + DEFAULT_COLLAPSED_OPERATION_VALUE + '</option>'+
                '</select>'+
            '</div>');

        advModeOperationOriginalIndexes = [];
        var auxAdvMode = []; // each element contains: "name", "index"
        
        // Iterating language operations and creating buttons for each operation
        for (var i = 0; i < opMap.length; i++) {
            
            // Manage selection picker for operations
            if ("visibility" in opMap[i] && opMap[i].visibility === "secondary") {
                
                // Manage advanced mode operations
                var advancedModeDataAttrStr = "";
                if ("advanced" in opMap[i]) {
                    // With advanced mode declared on manifest
                    advancedModeDataAttrStr += 'data-advanced-mode="' + opMap[i].advanced + '"';
                    if (opMap[i].advanced === true) {
                        // i+1 since first element of picker is "Other operations..."
                        auxAdvMode.push(opMap[i].name);
                    }
                }
                
                // Group operation for selection picker
                if ("group" in opMap[i]) {
                    var opGrouped = opVisibility.find("select optgroup[label='"+ opMap[i].group +"']");
                    if (opGrouped.length > 0) {
                        opGrouped.append(
                            $('<option id="' + opMap[i].id + '_collapsed" value="' + opMap[i].id + '" ' + advancedModeDataAttrStr + ' style="cursor:pointer;">' + opMap[i].name + '</option>')
                        );
                    } else {
                        opVisibility.find("select").append(
                            $('<optgroup id="' + opMap[i].id + '_collapsed_group" label="'+ opMap[i].group +'">'+
                                '<option id="' + opMap[i].id + '_collapsed" value="' + opMap[i].id + '" ' + advancedModeDataAttrStr + ' style="cursor:pointer;">' + opMap[i].name + '</option>'+
                            '</optgroup>')
                        );
                    }
                } else {
                    opVisibility.find("select").append(
                        $('<option id="' + opMap[i].id + '_collapsed" value="' + opMap[i].id + '" ' + advancedModeDataAttrStr + ' style="cursor:pointer;">' + opMap[i].name + '</option>')
                    );
                }
                operationArray.push(opMap[i]);

            } else {
            
                ops_name_length += opMap[i].name.length;

                if (typeof opMap[i].icon != "undefined") {
                    if (typeof opMap[i].iconOnly != "undefined" && opMap[i].iconOnly) {
                        var op = $('<button class="btn btn-primary opButton" title="' + opMap[i].name + '" id="' + opMap[i].id + '" data="' + opMap[i].advancedMode + '"><span class="' + opMap[i].icon + '"></span></button>');
                        operationArray.push(opMap[i]);
                        op.click(function () {
                            launchOperation(model, $(this).attr('id'), $(this).attr('title'));
                            operationArray = [];
                        });
                        ops.push(op);
                    } else {
                        var op = $('<button class="btn btn-primary opButton" name="' + opMap[i].name + '" id="' + opMap[i].id + '" data="' + opMap[i].advancedMode + '"><span class="' + opMap[i].icon + '"></span> ' + opMap[i].name + '</button>');
                        operationArray.push(opMap[i]);
                        op.click(function () {
                            launchOperation(model, $(this).attr('id'), $(this).attr('name'));
                            operationArray = [];

                        });
                        ops.push(op);
                    }

                } else {

                    if (ops_name_length <= 75) {

                        var op = $('<button class="btn btn-primary opButton" id=' + opMap[i].id + '>' + opMap[i].name + '</button>');
                        operationArray.push(opMap[i]);
                        op.click(function () {
                            launchOperation(model, $(this).attr('id'), $(this).text());
                            operationArray = [];
                        });
                        ops.push(op);


                    } else {
                        use_caret = true;
                        caretLI = $('<li><a id="' + opMap[i].id
                                + '" class="continue onlyOne">' + opMap[i].name
                                + '</a></li>');
                        caretUL.append(caretLI);
                        operationArray.push(opMap[i]);
                        caretLI.click(function () {
                            launchOperation(model, $(this).children('a').attr('id'), $(this).children('a').html());
                            operationArray = [];
                        });
                    }
                }
            }
        }
        
        // Add collapsed operations
        if (opVisibility.find("select option").length > 1) {
            // Added "opButton" class to fit with operation buttons bar
            opVisibility.addClass("opButton").find("select").change(function () {
                if ($(this).val() !== "") {
                    launchOperation(model, $(this).val(), $(this).val());
                    operationArray = [];
                    $(this).val(DEFAULT_COLLAPSED_OPERATION_VALUE);
                }
            });
            opVisibility.find("select").attr("data-advanced-mode-options", advModeOperationOriginalIndexes.toString());
            ops.push(opVisibility);
        }
        
        if (use_caret) {
            divContent.append(caret);
            divContent.append(caretUL);
        }

        for (var k = 0; k < ops.length; k++)
            divContent.append(ops[k]);
        
        // Load select picker style
        if (divContent.find('.selectpicker').length > 0 && !divContent.find('.selectpicker').is(":visible")) {
            // This will always show picker
            divContent.find('.selectpicker').selectpicker();
            // Hide first elements and fit picker
            divContent.find("#opGrouperContainer > div > div > ul > li:nth-child(1)").hide();
            divContent.find("#opGrouperContainer > div > div > ul > li:nth-child(2)").hide();
            divContent.find("#opGrouperContainer > div").css("width", "auto");
            divContent.find("#opGrouperContainer > div > div > ul > li").each(function () {
                var index = $(this).attr("data-original-index");
                if (index && auxAdvMode.indexOf($(this).text()) !== -1) {
                    advModeOperationOriginalIndexes.push(Number(index));
                }
            });
            
        }
    }

    comMap = ModeManager.getCommands(ModeManager
            .calculateModelIdFromExt(ModeManager
                    .calculateExtFromFileUri(fileUri)));

    oldCommands = ModeManager.getCommands(ModeManager
            .calculateModelIdFromExt(ModeManager
                    .calculateExtFromFileUri(oldUri)));
    require(['gcli/index', 'demo/index'], function (gcli) {
        if (typeof oldCommands != 'undefined') {
            for (var i = 0; i < oldCommands.length; i++) {
                gcli.removeCommand(oldCommands[i]);
            }
        }
        if (typeof comMap != 'undefined') {
            for (var i = 0; i < comMap.length; i++) {
                var command = comMap[i];

                var action = eval('(' + command.action + ')');
                gcli.addCommand({
                    name: command.id,
                    description: command.name,
                    params: command.params,
                    returnType: 'string',
                    exec: action,
                    defaultValue: command.defaultValue
                });

            }
        }
    });

    buttonPanel.append(divContent);
};

// Operation buttons auxiliar function
var launchOperation = function (model, id, name,callback) {
    var operationsMap=ModeManager.getOperations(model);
    if(!operationsMap)
        operationsMap=opMap;
    for (var i = 0; i < operationsMap.length; i++) {
        try {
            if (operationsMap[i].id === id) {
                var operation = operationsMap[i];
                if (model.apiVersion >= 2.0) {
                    switch (operation.type) {
                        case 'simple':
                            CommandApi.doDocumentOperation(operation.id, {}, fileUriOperation, function (result) {
                                if (result.annotations)
                                    EditorManager.setAnnotations(result.annotations);
                                OperationReport.launchedOperations.push(operation.name);
                                OperationReport.resultLaunchedOperations.push(result.message);
                            });
                            break;
                        case 'requireFile':
                            CommandApi.fetchFileContentsBeforeExecutingOperation(operation, fileUriOperation, operation.config.filter,
                                    operation.id, operation.config.modalTitle, operation.config.modalDescription, operation.name, function (result) {}, function (result) {})
                            break;
                        case 'createFile':
                            CommandApi.doDocumentOperation(operation.id, {}, fileUriOperation, function (result) {
                                if (result.annotations)
                                    EditorManager.setAnnotations(result.annotations);

                                if (result.status === 'OK') {
                                    
                                    var newUri = fileUriOperation.replace(/\.[^/.]+$/, "") + '.' + operation.config.ext;
                                    var fileName = fileUriOperation.substring(fileUriOperation.lastIndexOf('/') + 1, fileUriOperation.lastIndexOf('.'));
                                    if (operation.config.fileName) {
                                        newUri = fileUriOperation.replace(/[^\/]*$/, operation.config.fileName + '.' + operation.config.ext);
                                        fileName = operation.config.fileName;
                                    }
                                    var ext = '.' + operation.config.ext;
                                    var content = result.data;
                                    EditorManager.createNode(newUri, fileName, ext, function () {
                                        EditorManager.saveFile(newUri, content, function () {
                                            EditorManager.openFile(newUri);
                                            OperationReport.launchedOperations.push(operation.name);
                                            OperationReport.resultLaunchedOperations.push(result.message);
                                        });
                                    });
                                } else {
                                    OperationReport.launchedOperations.push(operation.name);
                                    OperationReport.resultLaunchedOperations.push(result.message);
                                }
                            });
                            break;
                    }
                } else {
                    var _remoteExecution = operation._remoteExecution;
                    var action = eval('(' + operation.action + ')');

                    // console.log(operation);
                    if (_remoteExecution !== undefined && _remoteExecution !== null
                            && eval('(' + _remoteExecution + ')')) {
                        CommandApi.doDocumentOperation(operation.id, operation.data, fileUriOperation, function (result) {
                            // console.log(result);
                            if (action !== undefined && action !== null) {
                                // console.log(result);
                                if (result.annotations)
                                    EditorManager.setAnnotations(result.annotations);
                                action(operation, fileUriOperation, result);
                                if(callback)
                                    callback(result);
                            }

                            OperationReport.launchedOperations.push(operation.name);
                            OperationReport.resultLaunchedOperations.push(result.message);
                        });
                    } else if (action !== undefined && action !== null) {
                        
                        if(callback)
                            result = action(operation, fileUriOperation,callback);
                        else
                            result = action(operation, fileUriOperation);
                    }
                }
                OperationReport.timeOfOperations.push(OperationMetrics.getOperationMilliseconds());
                break;
            }
        } catch (err) {
            OperationMetrics.stop();
            OperationReport.timeOfOperations.push(OperationMetrics.getOperationMilliseconds());
            CommandApi.echo("An error occurred while performing the operation <b>"
                    + name
                    + "</b>. Make sure you have well defined this operation in the language's manifest.");
            break;
        }
    }

};

var drawNewFormatLiElement = function (format, uri, editorFormatsElement) {
    var liElement = $("<li class='formatTab' ><a>" + format.toUpperCase()
            + "</a></li>");
    liElement.addClass(format);
    editorFormatsElement.append(liElement);

    liElement.click(function () {
        if (!EditorManager.currentDocumentHasProblems()) {
            if (!$(".formatTab." + format.toLowerCase()).hasClass("active")) {
                console.log("-> " + format);
                $("#editorFormats li").removeClass("active");
                EditorManager.changeFormat(uri, format);
                liElement.addClass("active");
            }
        } else {
            CommandApi.echo("<p style='color: red'>Can not convert to "
                    + format + " while errors exist.</p>");
        }
    });
};

// FE
var updateChildrenKeyPath = function (node, newName, prevName) {
    node.visit(function (childNode) {
        //TODO: replace only once
        var originFileUri = WorkspaceManager.getSelectedWorkspace() + "/" + childNode.data.keyPath,
                targetFileUri = WorkspaceManager.getSelectedWorkspace() + "/" + childNode.data.keyPath.replace(prevName, newName);
        // Update child tabsMap
        if (originFileUri in EditorManager.tabsMap) {
            EditorManager.tabsMap[targetFileUri] = EditorManager.tabsMap[originFileUri];
            EditorManager.tabsMap[originFileUri] = null;
            delete EditorManager.tabsMap[originFileUri];

            EditorManager.sessionsMap[targetFileUri] = EditorManager.sessionsMap[originFileUri];
            EditorManager.sessionsMap[originFileUri] = null;
            delete EditorManager.sessionsMap[originFileUri];

            if (originFileUri == EditorManager.currentUri)
                EditorManager.currentUri = targetFileUri;
        }
        // Update child keyPath
        childNode.data.keyPath = trimFirstFromNodeUri(targetFileUri);
    });
};

var EditorManager = {
    sessionsMap: {},
    tabsMap: {},
    currentUri: null,
    getCurrentUri: function () {
        return EditorManager.currentUri;
    },
    showInspector: function () {
        var inspector = $("#editorInspector");
        var inspectorToggle = $("#editorToggleInspector");

        inspectorToggle.addClass("hdd");
        inspector.removeClass("hdd");

        $("#editorInspector").trigger("inspectorChangeState", [true]);

    },
    hideInspector: function () {
        var inspector = $("#editorInspector");
        var inspectorToggle = $("#editorToggleInspector");

        inspectorToggle.removeClass("hdd");
        inspector.addClass("hdd");
        inspector.css("max-width", inspector.width() + "px");


        $("#editorInspector").trigger("inspectorChangeState", [false]);

    },
    // FE
    loadInspector: function (ext, format) {
        //		var loader = ModeManager.getInspectorLoader(ModeManager
        //		.calculateLanguageIdFromExt(ext));
        var loader = ModeManager.getInspectorLoader(ext)
                , descriptionLoader = DescriptionInspector.loader
                , editorInspector = $("#editorInspectorLoader");

        editorInspector.empty();

        if (loader || descriptionLoader) {

            DescriptionInspector.loaders.buildInspectorTabs();

            if (loader) {
                var loadFn = loader(editorInspector.find(".moduleInspectorContent"), format);
                if (loadFn) {
                    DescriptionInspector.tabs.loadInspectorModuleTab(editorInspector);
                    console.log("Inspector module content loaded");
                }
                DescriptionInspector.loader($("#editorInspector .descriptionInspectorContent"));
            }

        } else {
            // Default inspector content (REFACTOR?)
            editorInspector
                    .append("<span class='emptyMsg'>Nothing to show</span>");
        }

    },
    // FM
    openFile: function (fileUri, callback) {
        
        if (EditorManager.getCurrentUri() !== fileUri
                && getNodeByFileUri(fileUri) !== undefined) {
            
            WizardViewManager.loading(true);

            saveCurrentSession(function () {
                
                var exists = fileUri in EditorManager.tabsMap;

                FileApi.loadFileContents(fileUri, function (content) {

                    if (!exists) {
                        var tabbedInstance = createNewTabbedInstance(fileUri,
                                content);
                        if (tabbedInstance === null) // The editor cannot hande this
                            // resource:
                            return;
                    }

                    loadExistingTabbedInstance(fileUri, content);

                    mayCheckLanguageSyntax(fileUri, function () {
                        mayCheckLanguageConsistency(fileUri);
                    });

                    var node = getNodeByFileUri(fileUri);
                    if (node !== undefined)
                        node.activate();

                    // Change editor theme
                    var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri));
                    var editorThemeId;
                    var model = ModeManager.modelMap[modelId];

                    if (model.apiVersion >= 2) {
                        var formats = ModeManager.modelMap[modelId].syntaxes;
                        var currentFormat = EditorManager.sessionsMap[fileUri].getCurrentFormat();
                        // TODO: Refactor!
                        for (var f in formats) {
                            if (formats[f].id === currentFormat)
                                editorThemeId = formats[f].editorThemeId;
                        }
                    } else {
                        var formats = ModeManager.modelMap[modelId].formats;
                        var currentFormat = EditorManager.sessionsMap[fileUri].getCurrentFormat();
                        // TODO: Refactor!
                        for (var f in formats) {
                            if (formats[f].format === currentFormat)
                                editorThemeId = formats[f].editorThemeId;
                        }
                    }

                    console.log("## editorThemeId: " + editorThemeId);
                    document.editor.setTheme(editorThemeId);

                    // Inspector
                    EditorManager.loadInspector(ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri)),
                            EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat());
                    
                    // Load Binding content
                    if (DescriptionInspector) {
                        if (!DescriptionInspector.existBinding())
                            document.editor.focus();

                        DescriptionInspector.loaders.onEditorOpenFile();
                    } else {
                        document.editor.focus();
                    }

                    if (!DEFAULT_FILTER_FILES) {
                        DEFAULT_FILTER_FILES = true;
                        toggleAdvancedMode();
                    } else {
                        $("#editorFooter").show();
                    }
                    
                    LanguageBindingsManifestManager.load();

                    if (callback)
                        callback(content);
                    
                    setTimeout(function () {
                        WizardViewManager.loading(false);
                    }, 1000);
                    
                });
            });

        }

    },
    // FM
    closeFile: function (fileUri) {

        saveSession(fileUri, function () {
            var tab = EditorManager.tabsMap[fileUri];

            tab.css("width", tab.width() + "px");

            setTimeout(function () {
                tab.css("bottom", "-27px"); // Magic!!
                tab.css("width", "0px");
                setTimeout(function () {
                    tab.remove();
                }, 200);

                var potentialNextTab = Object.keys(EditorManager.tabsMap)[0];
                if (potentialNextTab != undefined && potentialNextTab != null) {
                    if (!(EditorManager.currentUri in EditorManager.tabsMap))
                        EditorManager.openFile("" + potentialNextTab);
                } else {
                    EditorManager.reset();
                    $("#selectOperationDiv").remove();
                    // EditorManager.currentUri = "";
                    // document.editor.destroy();
                    // document.editor = null;
                    //
                    // $("#editor").empty();
                    // $('#editor').remove();
                    // $('#editorWrapper').append('<div id="editor"></div>');
                    //
                    // $("#projectsTree").dynatree("getRoot").visit(function(node){
                    // node.deactivate();
                    // });
                    //
                    // $("#editorItself").removeClass("multiformat");
                }

            }, 1);

            EditorManager.sessionsMap[fileUri] = null;

            delete EditorManager.sessionsMap[fileUri];
            delete EditorManager.tabsMap[fileUri];

            if (DescriptionInspector)
                DescriptionInspector.loaders.onEditorCloseFile();
            
            // There is no file to show with editor maximized
            if (Object.keys(EditorManager.tabsMap).length === 0 && $("#appMainContent").hasClass("maximizedEditor") && !maximizing) {
                toggleMaximization();
            }
            
            LanguageBindingsManifestManager.clear();
            
        });
    },
    reset: function () {
        EditorCheckerIcon.stop();
        EditorManager.currentUri = "";
        // document.editor.destroy();
        document.editor = null;
        try {
            $("#editor").empty();
            $("#editor").remove();
        } catch (Exception) {
            console.log("#editor was empty");
        }
        $("#editorWrapper").append('<div id="editor"></div>');
        try {
            $("#projectsTree").dynatree("getRoot").visit(function (node) {
                node.deactivate();
            });
            $("#editorItself").removeClass("multiformat");
        } catch (Exception) {
            console.log("#projectTree was empty");
        }
    },
    saveFile: function (fileUri, content, callback) {
        FileApi.saveFileContents(fileUri, content, function (ts) {
            if (ts == true) {
                console.log("File " + fileUri + " saved correctly.");
                if (callback != undefined)
                    callback();
            }
        });
    },
    changeFormat: function (fileUri, desiredFormat) {

        //TODO: comprobar si el cambio de formato se realiza sobre/desde un un DescriptionFullView

        var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();
        var actualContent = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentSession().getValue();
        var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(EditorManager.currentUri));
        var model = ModeManager.getMode(modelId);
        var converterUri = ModeManager.getConverter(modelId);
        if (model.apiVersion >= 2) {
            converterUri = converterUri.replace("$srcSyntaxId", currentFormat).replace("$destSyntaxId", desiredFormat);
        }

        if (desiredFormat in EditorManager.sessionsMap[EditorManager.currentUri].getFormatsSessions() && currentFormat !== desiredFormat) {

            CommandApi.callConverter(model, currentFormat, desiredFormat, fileUri, actualContent, converterUri, function (result) {
                var appResponse = result;
                console.log("Converter resp:");
                console.log(appResponse);
                if (appResponse.data === "ERROR!")
                    CommandApi.echo("<p style='color: red'>" + "Cannot change to " + desiredFormat + " format, the file contains fails." + "</p>");
                else {
                    EditorManager.sessionsMap[EditorManager.currentUri].setCurrentFormat(desiredFormat);
                    document.editor.setSession(EditorManager.sessionsMap[EditorManager.currentUri].getCurrentSession());
                    EditorManager.sessionsMap[EditorManager.currentUri].getCurrentSession().setValue(appResponse.data);
                }
            });
        }

    },
    getCurrentEditorContent: function () {
        return document.editor.getSession().getValue();
    },
    setCurrentEditorContent: function (content) {
        return document.editor.getSession().setValue(content);
    },
    getEditorContentByUri: function (fileUri) {
        var result=null;
        if(EditorManager.sessionsMap[fileUri])
            result=EditorManager.sessionsMap[fileUri].getBaseSession().getValue();
        else{
            result=$.ajax({
                type: "GET",
                async: false,
                url: "files/get/"+fileUri 
             }).responseText;
        }
        return result;
    },
    setAnnotations: function (annotations) {
        document.editor.getSession().setAnnotations(annotations);
    },
    setAnnotationsToDocument: function (fileUri, annotations) {
        EditorManager.sessionsMap[fileUri].getBaseSession().setAnnotations(
                annotations);
    },
    // Felipe
    currentDocumentHasErrorAnnotation: function () {
        var ret = false
                , annotations = document.editor.getSession().getAnnotations();
        for (var i = 0; i < annotations.length; i++) {
            if (annotations[i].type == "error") {
                ret = true;
                break;
            }
        }

        return ret;
    },
    currentDocumentHasProblems: function () {
        return document.editor.getSession().getAnnotations().length > 0 && this.currentDocumentHasErrorAnnotation();
    },
    documentIsOpen: function (fileUri) {
        return fileUri in EditorManager.tabsMap;
    },
    createNode: function (fileUri, fileName, languageExtension, callback) {
        console.log("creating new node...");
        var nodeUri = FileApi.calculateNodeUri(currentSelectedNode.getParent());
        FileApi.createFile(fileUri, function (ts) {
            if (ts == true || ts == "true") {
                var newChild;
                var keyPath = nodeUri + "/" + fileName + languageExtension;
                if (languageExtension == ".sedl") {
                    newChild = buildChild(fileName + languageExtension, false,
                            "sedl_icon", keyPath);
                } else if (languageExtension == ".iagreetemplate"
                        || languageExtension == ".iagreeoffer"
                        || languageExtension == ".ttl") {
                    newChild = buildChild(fileName + languageExtension, false,
                            "binary_file_icon", keyPath);
                } else {
                    newChild = buildChild(fileName + languageExtension, false,
                            "file_icon", keyPath);
                }

                currentSelectedNode.getParent().addChild(newChild);
                currentSelectedNode.getParent().sortChildren();
            }
            console.log("---->>" + fileUri);
            callback();
        });

    },
    // FM (para commandApi no incluir "ignorePersistance" y poner por defecto a
    // false)
    deleteNode: function (fileUri, ignorePersistance, callback) {

        // Close file first
        if (fileUri in EditorManager.tabsMap)
            EditorManager.closeFile(fileUri);

        // Recurs. remove children files
        for (potentialUri in EditorManager.tabsMap) {
            if (potentialUri.indexOf(fileUri) != -1 && potentialUri != fileUri) {
                EditorManager.closeFile(potentialUri);
                FileApi.deleteFile(potentialUri);
            }
        }

        // Remove from tree
        var node = getNodeByFileUri(fileUri);
        if (node != undefined)
            node.remove();

        // TODO show confirmation with callbacks
        // Call backend for deletion
        if (!ignorePersistance || ignorePersistance == undefined) {

            if (node.data.isFolder) {
                if (node.getParent() == "DynaTreeNode<_1>: 'null'") {
                    FileApi.deleteProject(fileUri, function (result) {
                        callback(result);
                    });
                } else {
                    FileApi.deleteDirectory(fileUri, function (result) {
                        callback(result);
                    });
                }
            } else {
                FileApi.deleteFile(fileUri, function (result) {
                    callback(result);
                });
            }
        }

    },
    // FM
    renameNode: function (fileUri, newName) {
        var baseUri = trimLastFromNodeUri(fileUri);
        moveNodeAux(fileUri, baseUri + "/" + newName, true, true, false);
    },
    // FM (para commandApi poner 2 métodos: move y copy)
    moveNode: function (originFileUri, targetFileUri, copy) {
        moveNodeAux(originFileUri, targetFileUri, false, true, copy);
    }

};

// File Management auxiliary functions
var calculateNodeNameFromUri = function (uri) {
    var splittedUri = uri.split("/");
    var name = splittedUri[splittedUri.length - 1];
    console.log("name: " + name + "\nUri: " + uri);
    return name;
};

var trimFirstFromNodeUri = function (uri) {
    var splittedUri = uri.split("/");
    console.log("trimFirstFromNodeUri: "
            + uri.replace(splittedUri[0] + "/", ""));
    return uri.replace(splittedUri[0] + "/", "");
};

var trimLastFromNodeUri = function (uri) {
    var splittedUri = uri.split("/");
    console.log("trimLastFromNodeUri: "
            + uri.replace("/" + splittedUri[splittedUri.length - 1], ""));
    return uri.replace("/" + splittedUri[splittedUri.length - 1], "");
};

var moveNodeAux = function (originFileUri, targetFileUri, justRename, firstRecursion, copy) {

    // Names and uris
    var newName = calculateNodeNameFromUri(targetFileUri);
    var targetFileUriWithoutWS = trimFirstFromNodeUri(targetFileUri);

    // Calculate tree nodes
    var targetDirForNode = trimLastFromNodeUri(targetFileUri);
    var originNode = getNodeByFileUri(originFileUri);

    if (getNodeByFileUri(targetFileUri)) {
        if (!justRename)
            CommandApi.echo("<p style='color:red'>Project Tree: Element already exists.</p>");
        return false;
    }

    // Recurs. "move" childrens
    for (potentialUri in EditorManager.tabsMap) {
        if (potentialUri.indexOf(originFileUri) != -1
                && potentialUri != originFileUri) {
            var childName = calculateNodeNameFromUri(potentialUri);
            moveNodeAux(potentialUri, targetFileUri + "/" + childName,
                    justRename, false, copy);
        }
    }

    // Replace currentUri if necessary
    if (originNode && EditorManager.currentUri == originFileUri)
        EditorManager.currentUri = targetFileUri;

    // Update references in maps (only for terminal nodes)
    if (originNode && originFileUri != targetFileUri && originFileUri in EditorManager.tabsMap && !originNode.data.isFolder) {
        EditorManager.sessionsMap[targetFileUri] = EditorManager.sessionsMap[originFileUri];
        EditorManager.sessionsMap[originFileUri] = null;
        delete EditorManager.sessionsMap[originFileUri];

        EditorManager.tabsMap[targetFileUri] = EditorManager.tabsMap[originFileUri];
        EditorManager.tabsMap[originFileUri] = null;
        delete EditorManager.tabsMap[originFileUri];
    }

    // Handle tab if open
    var tab = EditorManager.tabsMap[targetFileUri];
    if (tab != undefined) {
        $(tab.children()[0]).empty();
        $(tab.children()[0]).append(newName);
        tab.attr('title', targetFileUri);

        // Select tab
        $(tab.children()).parent().off('click');
        $(tab.children()).click(function () {
            if (targetFileUri != EditorManager.currentUri)
                EditorManager.openFile(targetFileUri);
        });

        // Close tab
        $(tab.children()[1]).off('click');
        $(tab.children()[1]).click(function (event) {
            // alert(targetFileUri);
            event.stopPropagation();
            EditorManager.closeFile(targetFileUri);
        });
    }

    // Handle tree
    var targetNode = getNodeByFileUri(targetDirForNode);
    var prevName = originNode.data.title;
    if (originNode != undefined && targetNode != undefined) {
        originNode.data.keyPath = targetFileUriWithoutWS;
        originNode.render(); // For dynatree bug! Node needs to be rendered
        // before moving!
        targetNode.render(); // For dynatree bug!
        if (firstRecursion) {
            if (copy) {
                var copyNode = originNode.toDict(true, function (dict) {
                    // dict.title = "Copy of " + dict.title;
                    delete dict.key;
                });
                targetNode.addChild(copyNode);
            } else {
                originNode.move(targetNode);
                originNode.setTitle(newName);

                // Update children keypath
                if (originNode.data.isFolder)
                    updateChildrenKeyPath(originNode, newName, prevName);

                targetNode.sortChildren(); // TODO: Add depth limit for
                // performance

            }
        }
    } else if (originNode.getLevel() <= 1) { // it's  a project
        originNode.data.keyPath = targetFileUriWithoutWS
        originNode.render();
        updateChildrenKeyPath(originNode, newName, prevName);
    }

    // TODO show confirmation with callbacks
    // Call backend
    if (firstRecursion != undefined && firstRecursion) {
        if (justRename)
            if (originNode.data.isFolder)
                FileApi.renameDirectory(originFileUri, newName, function () {
                });
            else
                FileApi.renameFile(originFileUri, newName, function () {
                });
        else if (originNode.data.isFolder)
            FileApi.moveDirectory(originFileUri, targetFileUri, copy,
                    function () {
                    });
        else
            FileApi.moveFile(originFileUri, targetDirForNode, copy, function () {
            });
    }

};

// Operation languages launcher
var operations = [];
var loadOperations = {
    elementClass: "selectOperationItem",
    folderDisabled: true,
    titleCode: "editor.actions.modal.operation.title",
    buttonCode: "editor.actions.modal.operation.button",
    contentUrl: "app/modalWindows/selectOperationItem",
    onClick: function () {
        genericMenuOption(loadOperations);
    },
    onCreate: function () {
        var checkBoxMap = $(".box-operation");
        for (var i = 0; i < checkBoxMap.length; i++) {
            if (checkBoxMap[i].checked == true) {
                operations.push(checkBoxMap[i].value);
            }
        }
        hideModal();
        resultOperations.onClick();
    },
    onCancel: function () {
        hideModal();
    }
};
var resultOperations = {
    elementClass: "resultOperationItem",
    folderDisabled: true,
    titleCode: "editor.actions.modal.result-operation.title",
    buttonCode: "editor.actions.modal.result-operation.button",
    contentUrl: "app/modalWindows/resultOperationItem",
    onClick: function () {
        genericMenuOption(resultOperations);
    },
    onCreate: function () {
        OperationReport.createPDFLink("Test");
        operations = [];
        OperationReport.resetForNewReport();
        // download report
    },
    onCancel: function () {
        hideModal();
        operations = [];
        OperationReport.resetForNewReport();
    }
};
var userDemoInfo = {
    elementClass: "userDemoInfo",
    folderDisabled: true,
    titleCode: "editor.actions.modal.userDemoInfo.title",
    buttonCode: "editor.actions.modal.userDemoInfo.button",
    contentUrl: "app/modalWindows/userDemoInfo",
    onClick: function () {
        genericMenuOption(userDemoInfo);
        console.log("<<<<<<<onclick()");
    },
    onCreate: function () {
        console.log("<<<<<<<oncreate()");
        hideModal();
    },
    onCancel: function () {
        hideModal();
    }
};

// ------------
// Structs
// ------------

// SessionAggregation

function SessionAggregation() {
    this.current = null;
    this.formatsSessions = {};
    this.checkLanguageURI = {};
    this.checkConsistencyLanguageURI = {};
}

SessionAggregation.prototype.getFormatsSessions = function () {
    return this.formatsSessions;
};

SessionAggregation.prototype.getSession = function (formatKey) {
    return this.formatsSessions[formatKey];
};

SessionAggregation.prototype.getBaseSession = function () {
    return this.formatsSessions[this.baseFormat];
};

SessionAggregation.prototype.setBaseSession = function (baseFormat) {
    this.baseFormat = baseFormat;
};

SessionAggregation.prototype.setFormatSession = function (formatKey, session) {
    this.formatsSessions[formatKey] = session;
};

SessionAggregation.prototype.getBaseFormat = function () {
    return this.baseFormat;
};

SessionAggregation.prototype.getCurrentFormat = function () {
    return this.current;
};

SessionAggregation.prototype.getCurrentSession = function () {
    return this.getSession(this.getCurrentFormat());
};

SessionAggregation.prototype.setCurrentFormat = function (f) {
    this.current = f;
};

SessionAggregation.prototype.getCheckLanguageURI = function (formatKey) {
    return this.checkLanguageURI[formatKey];
};

SessionAggregation.prototype.setCheckLanguageURI = function (formatKey,
        checkLanguageURI) {
    this.checkLanguageURI[formatKey] = checkLanguageURI;
};

SessionAggregation.prototype.getCheckConsistencyLanguageURI = function (formatKey) {
    return this.checkConsistencyLanguageURI[formatKey];
};

SessionAggregation.prototype.setCheckConsistencyLanguageURI = function (formatKey,
        checkConsistencyLanguageURI) {
    this.checkConsistencyLanguageURI[formatKey] = checkConsistencyLanguageURI;
};
