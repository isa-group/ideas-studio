// Manager
var checkerTimer;
var operationArray = [];
var fileUriOperation = "";
var opMap = "";

var saveCurrentSession = function() {
	if (document.editor != null
			&& (EditorManager.currentUri in EditorManager.sessionsMap)) {
		saveSession(EditorManager.currentUri);
	}
};

var saveSession = function(uri, callback) {
	if (document.editor != null) {
		// TODO!!!
		FileApi.saveFileContents(uri, EditorManager.sessionsMap[uri]
				.getBaseSession().getValue(), function() {
			if (callback != null)
				callback();
		});
	}
};

var createNewTabbedInstance = function(fileUri, content) {
	var sessionAg = ModeManager.createSessionAggregationForEditor(fileUri,
			content);
	if (sessionAg == null) {
		var win = window.open("file/get/" + fileUri, "_blank");
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
	newTab.click(function() {
		if (fileUri != EditorManager.currentUri)
			EditorManager.openFile(fileUri);
	});

	// Close tab
	newTabCloseCross.click(function(event) {
		event.stopPropagation();
		EditorManager.closeFile(fileUri);
	});

	newTab.css("bottom", "-27px");
	newTab.css("max-width", "0%");
	setTimeout(function() {
		newTab.css("bottom", "0px");
		newTab.css("max-width", "100%");
	}, 1);

	ContextAction.register(newTab, [ {
		name : "Close",
		action : newTabCloseCross.click
	} ]);
	return newTab;

};

var checkSyntaxFlag = false;

var mayCheckLanguageSyntax = function(fileUri) {
	var agSession = EditorManager.sessionsMap[fileUri];
	if (agSession == undefined || agSession == "undefined") {
		CommandApi
				.echo("Please open the document you want to check before applying the command");
	} else {
		var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri]
				.getCurrentFormat();

		var checkLanguageUri = agSession.getCheckLanguageURI(currentFormat);

		if (checkLanguageUri != undefined && checkLanguageUri != null
				&& checkLanguageUri.length > 0) {

			var content = agSession.getBaseSession().getValue();

			CommandApi.checkLanguage(content, currentFormat, checkLanguageUri,
					fileUri, function(ts) {
						console.log("Checking syntax... " + ts);
						if (ts.status == "OK" || ts == "true") {
							console.log("Syntax is OK.");
							EditorManager
									.setAnnotations(eval('(' + "[]" + ')'));
							
							DescriptionInspector.onEditorCheckedLanguage();
							
							checkSyntaxFlag = true;
						} else {
							console.log(ts);
							EditorManager.setAnnotations(ts.annotations);
							checkSyntaxFlag = false;
						}

					});
		}
	}
};

var initAceEditor = function() {
	var editor = ace.edit("editor");
	document.editor = editor;
	// editor.setTheme("ace/theme/SEDL4People"); //TODO: change theme depending
	// of mode
	editor.setShowPrintMargin(false);

	document.sessionsMap = EditorManager.sessionsMap;
};

var loadExistingTabbedInstance = function(fileUri, content) {

	if (document.editor == null) {
		initAceEditor();

		document.editor
				.on(
						"change",
						function(changesDeltas) {
							if (checkerTimer)
								clearTimeout(checkerTimer);

							checkerTimer = setTimeout(
									function() {
										if (fileUri != "") {

											var content = EditorManager.sessionsMap[EditorManager.currentUri]
													.getBaseSession()
													.getValue();
											EditorManager.saveFile(
													EditorManager.currentUri,
													content);
										}
										mayCheckLanguageSyntax(EditorManager.currentUri);

									}, 1000);
						});
		
		DescriptionInspector.loaders.onInitAceEditor();
		
	}

	$("editor").css("visibility", "visible");

	if (EditorManager.currentUri != null) {
		$(EditorManager.tabsMap[EditorManager.currentUri])
				.removeClass("active");
	}

	EditorManager.currentUri = fileUri;

	$(EditorManager.tabsMap[EditorManager.currentUri]).addClass("active");

	document.editor
			.setSession(EditorManager.sessionsMap[EditorManager.currentUri]
					.getCurrentSession());

	// Formats tabs

	var editorFormatsElement = $("#editorFormats");
	editorFormatsElement.empty();
	var count = 0;
	for ( var key in EditorManager.sessionsMap[EditorManager.currentUri]
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
	var optButton = $('<a class="btn" id="selectOperation" style="display:none;">Analysis report</a>');
	optButton.click(function() {
		loadOperations.onClick();
	});
	///////
	var expandButton=$('<button class="btn" style="{float:left}" id="expandConsole"><span class="glyphicon glyphicon-chevron-up"></span></button>');
	expandButton.click(function(){
		DescriptionInspector.expandConsole('second');
	});
	
	
	var clearButton=$('<button class="btn" style="{float:left}" id="clearConsole"><span class="glyphicon glyphicon-remove-sign"></span></button>');
	clearButton.click(function(){
		DescriptionInspector.clearConsole();
	});
	
	///////
	divContent.append(optButton);
	divContent.append(expandButton);
	divContent.append(clearButton);
	
	var caret = $('<button class="btn btn-primary opButton" data-toggle="dropdown">...</span><span class="sr-only">Toggle Dropdown</span></button>');
	var caretUL = $('<ul id="ulOperationsTypes" class="dropdown-menu scrollable-op-menu" role="menu"></ul>');
	var caretLI = "";
	
	fileUriOperation = EditorManager.getCurrentUri();
	opMap = ModeManager.getOperations(ModeManager
			.calculateLanguageIdFromExt(ModeManager
					.calculateExtFromFileUri(fileUri)));
	
	var ops_name_lenght = 0;
	var use_caret = false;
	var ops = [];
	if (opMap != undefined){
		for (var i = 0; i < opMap.length; i++) {
			ops_name_lenght += opMap[i].name.length;
			if(ops_name_lenght <= 75){
				var op = $('<button class="btn btn-primary opButton" id=' + opMap[i].id + '>'+ opMap[i].name + '</button>');
				operationArray.push(opMap[i]);
				op.click(function() {
					launchOperation($(this).html());
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
				caretLI.click(function() {
					launchOperation($(this).children('a').html());
					operationArray = [];
				});
			}
		}
		
		if(use_caret){
			divContent.append(caret);
			divContent.append(caretUL);
		}
		
		for(var k=0; k<ops.length;k++)
			divContent.append(ops[k]);
	}
	
	buttonPanel.append(divContent);
};

// Operation buttons auxiliar function
var launchOperation = function(name) {
	for (var i = 0; i < opMap.length; i++) {

		try {
			if (opMap[i].name == name) {
				var _remoteExecution = opMap[i]._remoteExecution;
				var action = eval('(' + opMap[i].action + ')');
				var operation = opMap[i];

				// console.log(operation);
				if (_remoteExecution != undefined && _remoteExecution != null
						&& eval('(' + _remoteExecution + ')')) {
					CommandApi.doDocumentOperation(operation.id, operation.data, fileUriOperation, function(result) {
						// console.log(result);
						if (action != undefined	&& action != null) {
							// console.log(result);
							if (result.annotations)
								EditorManager.setAnnotations(result.annotations);
							action(operation, fileUriOperation, result);
						}
						
						OperationReport.launchedOperations.push(operation.name);
						OperationReport.resultLaunchedOperations.push(result.message);
					});
				} else if (action != undefined && action != null) {
					action(operation, fileUriOperation);
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

var drawNewFormatLiElement = function(format, uri, editorFormatsElement) {
	var liElement = $("<li class='formatTab' ><a>" + format.toUpperCase()
			+ "</a></li>");
	liElement.addClass(format);
	editorFormatsElement.append(liElement);

	liElement.click(function() {
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

	sessionsMap : {},
	tabsMap : {},
	currentUri : null,

	getCurrentUri : function() {
		return EditorManager.currentUri;
	},

	showInspector : function() {
		var inspector = $("#editorInspector");
		var inspectorToggle = $("#editorToggleInspector");

		inspectorToggle.addClass("hdd");
		inspector.removeClass("hdd");

		$("#editorInspector").trigger("inspectorChangeState", [true]);

	},

	hideInspector : function() {
		var inspector = $("#editorInspector");
		var inspectorToggle = $("#editorToggleInspector");

		inspectorToggle.removeClass("hdd");
		inspector.addClass("hdd");
		inspector.css("max-width", inspector.width() + "px");
		

		$("#editorInspector").trigger("inspectorChangeState", [false]);

	},

	// FE
	loadInspector : function(ext, format) {
		//		var loader = ModeManager.getInspectorLoader(ModeManager
		//		.calculateLanguageIdFromExt(ext));
		var loader = ModeManager.getInspectorLoader(ext)
			, descriptionLoader = DescriptionInspector.loader
			, inspectorLoader = $("#editorInspectorLoader");
		
		inspectorLoader.empty();
		
		if (loader || descriptionLoader) {
			
			DescriptionInspector.loaders.loadInspectorTab(inspectorLoader);

			if (loader && typeof loader != "undefined") {
				var loadFn = loader(inspectorLoader.find(".moduleInspectorContent"), format);
				
				if (typeof loadFn != "undefined") {
					DescriptionInspector.buildInspectorTabs(inspectorLoader);
					console.log("Inspector module content loaded");
				}
				DescriptionInspector.loader($("#editorInspector .descriptionInspectorContent"));
			}

		} else {
			// Default inspector content (REFACTOR?)
			inspectorLoader
				.append("<span class='emptyMsg'>Nothing to show</span>");
		}
	
	},

	// FM
	openFile : function(fileUri) {

		if (EditorManager.getCurrentUri() != fileUri
				&& getNodeByFileUri(fileUri) != undefined) {

			saveCurrentSession();

			var exists = fileUri in EditorManager.tabsMap;

			FileApi.loadFileContents(fileUri, function(content) {

				if (!exists) {
					var tabbedInstance = createNewTabbedInstance(fileUri,
							content);
					if (tabbedInstance == null) // The editor cannot hande this
						// resource:
						return;
				}

				loadExistingTabbedInstance(fileUri, content);
				mayCheckLanguageSyntax(fileUri);

				var node = getNodeByFileUri(fileUri);
				if (node != undefined)
					node.activate();

				// Change editor theme
				var languageId = ModeManager
						.calculateLanguageIdFromExt(ModeManager
								.calculateExtFromFileUri(fileUri));
				var editorThemeId;
				var formats = ModeManager.languageModeMap[languageId].formats;
				var currentFormat = EditorManager.sessionsMap[fileUri]
						.getCurrentFormat();
				// TODO: Refactor!
				for ( var f in formats) {
					if (formats[f].format == currentFormat)
						editorThemeId = formats[f].editorThemeId;
				}
				console.log("## editorThemeId: " + editorThemeId);
				document.editor.setTheme(editorThemeId);

				// Inspector
				EditorManager.loadInspector(ModeManager
						.calculateLanguageIdFromExt(ModeManager
								.calculateExtFromFileUri(fileUri)),
						EditorManager.sessionsMap[EditorManager.currentUri]
								.getCurrentFormat());
				
				if (DescriptionInspector) {
					if (!DescriptionInspector.existBinding()) 
						document.editor.focus();
						
					DescriptionInspector.loaders.onEditorOpenFile();
				} else {
					document.editor.focus();
				}
			});

		}

	},

	// FM
	closeFile : function(fileUri) {

		saveSession(fileUri, function() {
			var tab = EditorManager.tabsMap[fileUri];

			tab.css("width", tab.width() + "px");

			setTimeout(function() {
				tab.css("bottom", "-27px"); // Magic!!
				tab.css("width", "0px");
				setTimeout(function() {
					tab.remove();
				}, 200);

				var potentialNextTab = Object.keys(EditorManager.tabsMap)[0];
				if (potentialNextTab != undefined && potentialNextTab != null) {
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
			
			if (DescriptionInspector) DescriptionInspector.loaders.onEditorCloseFile();
		});
	},

	reset : function() {
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
			$("#projectsTree").dynatree("getRoot").visit(function(node) {
				node.deactivate();
			});
			$("#editorItself").removeClass("multiformat");
		} catch (Exception) {
			console.log("#projectTree was empty");
		}
	},
	
	saveFile : function(fileUri, content, callback) {
		FileApi.saveFileContents(fileUri, content, function(ts) {
			if (ts == true) {
				console.log("File " + fileUri + " saved correctly.");
				if(callback != undefined)
					callback();
			}
		});
	},

	changeFormat : function(fileUri, desiredFormat) {

		//TODO: comprobar si el cambio de formato se realiza sobre/desde un un DescriptionFullView

		var currentFormat = EditorManager.sessionsMap[EditorManager.currentUri]
				.getCurrentFormat();
		var actualContent = EditorManager.sessionsMap[EditorManager.currentUri]
				.getCurrentSession().getValue();
		var converterUri = ModeManager.getConverter(ModeManager
				.calculateLanguageIdFromExt(ModeManager
						.calculateExtFromFileUri(EditorManager.currentUri)));
		
		if (desiredFormat in EditorManager.sessionsMap[EditorManager.currentUri].getFormatsSessions() &&
			currentFormat !== desiredFormat) {
		
			CommandApi
					.callConverter(
							currentFormat,
							desiredFormat,
							fileUri,
							actualContent,
							converterUri,
							function(result) {
								var appResponse = result;
								console.log("Converter resp:");
								console.log(appResponse);
								if (appResponse.data == "ERROR!")
									CommandApi
											.echo("<p style='color: red'>Can no change to "
													+ desiredFormat
													+ " format, the file contains fails.</p>");
								else {
									EditorManager.sessionsMap[EditorManager.currentUri]
											.setCurrentFormat(desiredFormat);
									document.editor
											.setSession(EditorManager.sessionsMap[EditorManager.currentUri]
													.getCurrentSession());
									EditorManager.sessionsMap[EditorManager.currentUri]
											.getCurrentSession().setValue(
													appResponse.data);
								}

							});

		}

	},

	getCurrentEditorContent : function() {
		return document.editor.getSession().getValue();
	},

	setCurrentEditorContent : function(content) {
		return document.editor.getSession().setValue(content);
	},

	getEditorContentByUri : function(fileUri) {
		return EditorManager.sessionsMap[fileUri].getBaseSession().getValue();
	},

	setAnnotations : function(annotations) {
		document.editor.getSession().setAnnotations(annotations);
	},

	setAnnotationsToDocument : function(fileUri, annotations) {
		EditorManager.sessionsMap[fileUri].getBaseSession().setAnnotations(
				annotations);
	},
	
	// Felipe
	currentDocumentHasErrorAnnotation : function() {
		var ret = false
			, annotations = document.editor.getSession().getAnnotations();
		for (var i=0; i < annotations.length; i++) {
			if (annotations[i].type == "error") {
				ret = true;
				break;
			}
		}
		
		return ret;
	},

	currentDocumentHasProblems : function() {
		return document.editor.getSession().getAnnotations().length > 0 && this.currentDocumentHasErrorAnnotation();
	},

	documentIsOpen : function(fileUri) {
		return fileUri in EditorManager.tabsMap;
	},

	createNode : function(fileUri, fileName, languageExtension, callback) {
		console.log("creating new node...");
		var nodeUri = FileApi.calculateNodeUri(currentSelectedNode.getParent());
		FileApi.createFile(fileUri, function(ts) {
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
	deleteNode : function(fileUri, ignorePersistance, callback) {

		// Close file first
		if (fileUri in EditorManager.tabsMap)
			EditorManager.closeFile(fileUri);

		// Recurs. remove children files
		for (potentialUri in EditorManager.tabsMap) {
			if (potentialUri.indexOf(fileUri) != -1 && potentialUri != fileUri)
				EditorManager.deleteFile(potentialUri, true);
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
					FileApi.deleteProject(fileUri, function(result) {
						callback(result);
					});
				} else {
					FileApi.deleteDirectory(fileUri, function(result) {
						callback(result);
					});
				}
			} else {
				FileApi.deleteFile(fileUri, function(result) {
					callback(result);
				});
			}
		}

	},

	// FM
	renameNode : function(fileUri, newName) {
		var baseUri = trimLastFromNodeUri(fileUri);
		moveNodeAux(fileUri, baseUri + "/" + newName, true, true, false);
	},

	// FM (para commandApi poner 2 métodos: move y copy)
	moveNode : function(originFileUri, targetFileUri, copy) {
		moveNodeAux(originFileUri, targetFileUri, false, true, copy);
	}

};

// File Management auxiliary functions
var calculateNodeNameFromUri = function(uri) {
	var splittedUri = uri.split("/");
	var name = splittedUri[splittedUri.length - 1];
	console.log("name: " + name + "\nUri: " + uri);
	return name;
};

var trimFirstFromNodeUri = function(uri) {
	var splittedUri = uri.split("/");
	console.log("trimFirstFromNodeUri: "
			+ uri.replace(splittedUri[0] + "/", ""));
	return uri.replace(splittedUri[0] + "/", "");
};

var trimLastFromNodeUri = function(uri) {
	var splittedUri = uri.split("/");
	console.log("trimLastFromNodeUri: "
			+ uri.replace("/" + splittedUri[splittedUri.length - 1], ""));
	return uri.replace("/" + splittedUri[splittedUri.length - 1], "");
};

var moveNodeAux = function(originFileUri, targetFileUri, justRename,
		firstRecursion, copy) {

	// Names and uris
	var newName = calculateNodeNameFromUri(targetFileUri);
	var targetFileUriWithoutWS = trimFirstFromNodeUri(targetFileUri);

	// Calculate tree nodes
	var targetDirForNode = trimLastFromNodeUri(targetFileUri);
	var originNode = getNodeByFileUri(originFileUri);

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
		$(tab.children()).click(function() {
			if (targetFileUri != EditorManager.currentUri)
				EditorManager.openFile(targetFileUri);
		});

		// Close tab
		$(tab.children()[1]).off('click');
		$(tab.children()[1]).click(function(event) {
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
				var copyNode = originNode.toDict(true, function(dict) {
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
				FileApi.renameDirectory(originFileUri, newName, function() {
				});
			else
				FileApi.renameFile(originFileUri, newName, function() {
				});
		else if (originNode.data.isFolder)
			FileApi.moveDirectory(originFileUri, targetFileUri, copy,
					function() {
					});
		else
			FileApi.moveFile(originFileUri, targetDirForNode, copy, function() {
			});
	}

};

// Operation languages launcher
var operations = [];
var loadOperations = {
	elementClass : "selectOperationItem",
	folderDisabled : true,
	titleCode : "editor.actions.modal.operation.title",
	buttonCode : "editor.actions.modal.operation.button",
	contentUrl : "app/modalWindows/selectOperationItem",
	onClick : function() {
		genericMenuOption(loadOperations);
	},
	onCreate : function() {
		var checkBoxMap = $(".box-operation");
		for (var i = 0; i < checkBoxMap.length; i++) {
			if (checkBoxMap[i].checked == true) {
				operations.push(checkBoxMap[i].value);
			}
		}
		hideModal();
		resultOperations.onClick();
	},
	onCancel : function() {
		hideModal();
	}
};
var resultOperations = {
	elementClass : "resultOperationItem",
	folderDisabled : true,
	titleCode : "editor.actions.modal.result-operation.title",
	buttonCode : "editor.actions.modal.result-operation.button",
	contentUrl : "app/modalWindows/resultOperationItem",
	onClick : function() {
		genericMenuOption(resultOperations);
	},
	onCreate : function() {
		OperationReport.createPDFLink("Test");
		operations = [];
		OperationReport.resetForNewReport();
		// download report
	},
	onCancel : function() {
		hideModal();
		operations = [];
		OperationReport.resetForNewReport();
	}
};
var userDemoInfo = {
	elementClass : "userDemoInfo",
	folderDisabled : true,
	titleCode : "editor.actions.modal.userDemoInfo.title",
	buttonCode : "editor.actions.modal.userDemoInfo.button",
	contentUrl : "app/modalWindows/userDemoInfo",
	onClick : function() {
		genericMenuOption(userDemoInfo);
		console.log("<<<<<<<onclick()");
	},
	onCreate : function() {
		console.log("<<<<<<<oncreate()");
		hideModal();
	},
	onCancel : function() {
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
}

SessionAggregation.prototype.getFormatsSessions = function() {
	return this.formatsSessions;
};

SessionAggregation.prototype.getSession = function(formatKey) {
	return this.formatsSessions[formatKey];
};

SessionAggregation.prototype.getBaseSession = function() {
	return this.formatsSessions[this.baseFormat];
};

SessionAggregation.prototype.setBaseSession = function(baseFormat) {
	this.baseFormat = baseFormat;
};

SessionAggregation.prototype.setFormatSession = function(formatKey, session) {
	this.formatsSessions[formatKey] = session;
};

SessionAggregation.prototype.getBaseFormat = function() {
	return this.baseFormat;
};

SessionAggregation.prototype.getCurrentFormat = function() {
	return this.current;
};

SessionAggregation.prototype.getCurrentSession = function() {
	return this.getSession(this.getCurrentFormat());
};

SessionAggregation.prototype.setCurrentFormat = function(f) {
	this.current = f;
};

SessionAggregation.prototype.getCheckLanguageURI = function(formatKey) {
	return this.checkLanguageURI[formatKey];
};

SessionAggregation.prototype.setCheckLanguageURI = function(formatKey,
		checkLanguageURI) {
	this.checkLanguageURI[formatKey] = checkLanguageURI;
};
