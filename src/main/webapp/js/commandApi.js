var CommandApi = {

	analyzeSEDLDocument : function(callback) {

		// TODO
		alert("Function analyzeSEDLDocument() here");
	},

	checkLanguage : function(content, format, checkLanguageURI, fileUri, callback) {
		$.ajax(checkLanguageURI, {
			"type" : "post",
			"data" : {
				"id" : format,
				"content" : content,
				"fileUri" : fileUri
			},
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
			},
			"async" : true,
		});
	},

	callConverter : function(currentFormat, desiredFormat, fileUri,
			actualContent, converterUri, callback) {
		$.ajax(converterUri, {
			"type" : "post",
			"data" : {
				"currentFormat" : currentFormat,
				"desiredFormat" : desiredFormat,
				"fileUri" : fileUri,
				"content" : actualContent
			},
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
			},
			"async" : true,
		});
	},

	doDocumentOperation : function(operationId, data, fileUri, callback, async) {
		
		if(async == undefined){
			async = true;
		}
		
		var extendedData;

		try {
			extendedData = JSON.parse(JSON.stringify(data));
			extendedData.fileUri = fileUri;
			extendedData.content = EditorManager.getEditorContentByUri(fileUri);
			extendedData.id = operationId;
			var name = '';
			for (var i = 0; i < opMap.length; i++) {
				if(opMap[i].id == operationId){
					name = opMap[i].name;
				}
			}
			
			OperationMetrics.play(name);
			
			// Extensión para coger argumentos adicionales
			for (var i=5; i < arguments.length; i++) {
				extendedData["auxArg"+(i-5)] = arguments[i];
		    }

			var operationUri = ModeManager
					.getBaseUri(ModeManager
							.calculateLanguageIdFromExt(ModeManager.calculateExtFromFileUri(fileUri)))
					+ "/language/operation/" + operationId + "/execute";
			
			RequestHelper.ajax(operationUri, {
				"type" : "post",
				"data" : extendedData,
				"onSuccess" : function(result) {
					console.log("onSuccess");
					console.log(operationUri);
					callback(result);
					OperationMetrics.stop();
				},
				"onProblems" : function(result) {
					console.log("onProblems");
					callback(result);
					OperationMetrics.stop();
				},
				"onError" : function(result) {
					console.log("onError");
					console.log(operationUri);
					callback(result);
					OperationMetrics.stop();
				},
				"onSessionError" : function(result) {
					console.log("onSessionError");
					callback(result);
					OperationMetrics.stop();
				},
				"async" : async,
			});
		} catch (e) {
			console.log("Error executing document operation: \n" + e);
			callback(e);
		}

	},

	openFile : function(fileUri, callback) {
		EditorManager.openFile(fileUri, callback);
	},

	closeFile : function(fileUri) {
		EditorManager.closeFile(fileUri);
	},

	deleteNode : function(fileUri, callback) {
		EditorManager.deleteNode(fileUri, false, callback);
	},

	renameNode : function(fileUri, newName, callback) {
		EditorManager.renameNode(fileUri, newName, callback);
	},

	move : function(originFileUri, targetFileUri) {
		EditorManager.moveNode(originFileUri, targetFileUri, false);
	},

	copy : function(originFileUri, targetFileUri) {
		EditorManager.moveNode(originFileUri, targetFileUri, true);
	},
	
	importDemoWorkspace : function(demoWorkspaceName, targetWorkspaceName) {
		closeAllTabs();
		WorkspaceManager.getWorkspaces(function(workspacesArray) {
			var exists = false;
			for (index in workspacesArray)
				if (workspacesArray[index].name === targetWorkspaceName) {
					exists = true;
					break;
				}
			
			if (exists) {
				var continueHandler = function() {
					switchToDemoWorkspace(demoWorkspaceName, targetWorkspaceName);
					hideModal();
				};
				showModal("Confirm workspace overwriting", "The name for the workspace <b>'" + targetWorkspaceName +"'</b> already exists.<BR/><BR/><b>Do you want to overwrite the existing workspace?</b><BR/>If you want to regenerate without overwriting, try the command <BR/><i>generateDemoWorkspaceWithDestination</i>.",
						"Continue", continueHandler,
						function(){}, function(){});
			} else {
				switchToDemoWorkspace(demoWorkspaceName, targetWorkspaceName);
			}
			
		});
		
	},

	echo : function(msg) {
		CommandsRegistry.echo.exec({
			message : msg
		});
	},
	
	deleteWorkspace : function(fileUri, callback){
		WorkspaceManager.deleteWorkspace(fileUri, callback);
	},
	
	// Helper for executing operations that requires aditional data

	fetchFileContentsBeforeExecutingOperation : function(operation, fileUri, extensionRestriction, operationId,
			titleForModal, descText, primaryActionText, primaryHandler,
			cancelHandler) {

		if (window.File && window.FileReader && window.FileList && window.Blob) { // Check support

			useSelectionList = false;
			
			var fileContent = "";

			var $fileInput = $('<input id="ffcbeoFileUpload" type="file" >');
			var $fileInputWrapper = $(	'<span class="btn btn-success fileinput-button">'+
			        						'<i class="glyphicon glyphicon-plus"></i>'+
			        						'<span> Select file from your local file system</span>'+
			    						'</span>');
			$fileInputWrapper.add($fileInput);
			
			$fileInputWrapper.click(function(){$fileInput.click();});
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
				'<div class="form-group">'+
		            '<select class="form-control" name="category">'+
	                	'<option value=0>-</option>'+
	                '</select>'+
	        	'</div>');
			
			$existingFileSelectionContainer.append($selectableList);
			
			$modalContent.append($existingFileSelectionContainer);

			if (!(window.File && window.FileReader && window.FileList && window.Blob)) {	// Check HTML5 fileApi support
				alert('The File APIs are not fully supported in this browser. This operation can not be used without it.');
				closeModal();
			}

			var fileHandler = function(file) {
				var reader = new FileReader();
				reader.onload = function(event) {
					var content = event.target.result;
					fileContent = content;
					
					$('.modal-footer .btn.btn-primary.continue').removeAttr("disabled");
					useSelectionList = false;
					$('#existingFileSelectionContainer select option').eq(0).prop('selected', true);
					
				};
				reader.readAsText(file[0]);
			};

			try {
				$fileInput.change(function() {
					fileHandler(this.files);
				});
			} catch (e) {
				alert(e);
			}
			

			primaryHandlerWrapper = function() {

				if (useSelectionList) {
					var fUri = $('#existingFileSelectionContainer select, callback').find(":selected").text();
					FileApi.loadFileContents(WorkspaceManager.getSelectedWorkspace() + "/" + fUri, function(result){
						CommandApi.doDocumentOperation(operationId, operation.data, fileUri, function(result){
							hideModal();
							primaryHandler(result);
						}, true, result);
					});
				} else {
					CommandApi.doDocumentOperation(operationId, operation.data, fileUri, function(result){
						hideModal();
						primaryHandler(result);
					}, true, fileContent);
				}
				
				
			};

			showModal(titleForModal, $modalContent, primaryActionText, primaryHandlerWrapper,
					cancelHandler, cancelHandler);
		
			
			// Add options to select once the modal has been rendered
			
			var useExtRestriction = extensionRestriction != null && extensionRestriction != undefined && extensionRestriction != "";
			
			$("#projectsTree").dynatree("getRoot").visit(function(node){
			    if(!node.data.isFolder && (!useExtRestriction || useExtRestriction && node.data.keyPath.indexOf(extensionRestriction, this.length - extensionRestriction.length) !== -1)) {
			    	console.dir(node);
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

var switchToDemoWorkspace = function(demoWorkspaceName, targetWorkspaceName) {
	RequestHelper.ajax("files/importDemo", {
		"type" : "get",
		"data" : {
			"demoWorkspaceName" : demoWorkspaceName,
			"targetWorkspaceName" : targetWorkspaceName
		},
		"onSuccess" : function(result) {
			CommandApi.echo("Swithching to workspace...");
			WorkspaceManager.getWorkspaces(function(wss) {
				$("#projectsTree").dynatree("getTree").reload();
				WorkspaceManager.setSelectedWorkspace(targetWorkspaceName);
				WorkspaceManager.loadWorkspace();
				EditorManager.reset();
			});
			
		},
		"onProblems" : function(result) {
			
		},
		"onError" : function(result) {
		},

		"async" : true,
		});

};

var closeAllTabs = function(){
	CommandApi.echo("Editor preparing...");
	var tabs = $(".glyphicon-remove");
	for(var i=0;i<tabs.length;i++){
		tabs[i].click();
	}
};