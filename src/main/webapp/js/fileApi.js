var FileApi = {
	loadWorkspace : function(workspaceName, callback) {

		$.ajax("file/getWorkspace?workspaceName=" + workspaceName, {
			"type" : "get",
			"success" : function(result) {
				var treeStruct = eval("(" + result + ")");
				callback(treeStruct);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			// "data": {clave: "Valor a enviar al servidor"},
			"async" : true,
		});

	},

	loadFileContents : function(fileUri, callback) {

		$.ajax("file/getFileContent?fileUri=" + fileUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});

	},
	
	saveFileContents : function(fileUri, fileContent, callback) {

		$.ajax("file/setFileContent", {
			"type" : "post",
			"data" : {
				'fileUri' : fileUri,
				'fileContent' : fileContent
			},
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});

	},
	

	createFile : function(fileUri, callback) {

		$.ajax("file/createFile?fileUri=" + fileUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},

	createDirectory : function(directoryUri, callback) {

		$.ajax("file/createDirectory?directoryUri=" + directoryUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},

	createProject : function(projectUri, callback) {

		$.ajax("file/createProject?projectUri=" + projectUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},

	deleteFile : function(fileUri, callback) {

		$.ajax("file/deleteFile?fileUri=" + fileUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},

	deleteDirectory : function(directoryUri, callback) {

		$.ajax("file/deleteDirectory?directoryUri=" + directoryUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},

	deleteProject : function(projectUri, callback) {

		$.ajax("file/deleteProject?projectUri=" + projectUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},

	createWorkspace : function(workspaceName, callback) {

		$.ajax("file/createWorkspace?workspaceName=" + workspaceName, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
	
	deleteWorkspace : function(workspaceUri, callback) {

		$.ajax("file/deleteWorkspace?workspaceUri=" + workspaceUri, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
	
	moveFile: function(fileUri, destUri, copy, callback) {
		console.log("->>" + fileUri + " " + destUri);
		$.ajax("file/moveFile?fileUri=" + fileUri + "&destUri=" + destUri + "&copy=" + copy, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
	
	moveDirectory: function(directoryUri, destUri, copy, callback) {
		console.log("->>" + directoryUri + " " + destUri);
		$.ajax("file/moveDirectory?directoryUri=" + directoryUri + "&destUri=" + destUri + "&copy=" + copy,  {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
	
	renameFile: function(fileUri, newName, callback) {
		$.ajax("file/renameFile?fileUri=" + fileUri + "&newName=" + newName, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
	
	renameDirectory: function(directoryUri, newName, callback) {
		$.ajax("file/renameDirectory?directoryUri=" + directoryUri + "&newName=" + newName, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},

	
	getWorkspaces : function(callback) {

		$.ajax("file/getWorkspaces", {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
	
	getSelectedWorkspace : function ( callback ) {
		$.ajax("file/getSelectedWorkspace", {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
	
	setSelectedWorkspace : function ( wsName, callback ) {
		$.ajax("file/saveSelectedWorkspace?workspaceName=" + wsName, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true
		});
	},
	
	currnetWSAsDemoWS :function( wsName, callback){
		$.ajax("file/cloneSelectedWorkspaceToDemo?workspaceName=" + wsName, {
			"type" : "get",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true
		});
	},
        
        downloadAsZip: function(uri,callback){
            window.location.href = "file/getAsZip/"+uri;            
        },
	
	// Utils

	calculateNodeUri : function(node) {
		var uri = "";
		var cl = node.getLevel() - 1;
		var currentNode = node.getParent();
		uri += node.data.title;
		while (cl > 0) {
			uri = currentNode.data.title + "/" + uri;
			currentNode = currentNode.getParent();
			cl--;
		}
		return uri;
	}
};