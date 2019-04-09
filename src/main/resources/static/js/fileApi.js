var FileApi = {
	loadWorkspace : function(workspaceName, callback) {

		$.ajax("workspaces/" + workspaceName + "/load", {
			"type" : "GET",
			"success" : function(result) {
                            if(result!==""){
				var treeStruct = eval("(" + result + ")");
				callback(treeStruct);
                            }
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

		$.ajax("files/content?fileUri=" + fileUri, {
			"type" : "GET",
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

		$.ajax("files/content" , {
			"type" : "POST",
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

		$.ajax("files?fileUri=" + fileUri+"&fileType=file", {
			"type" : "POST",
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

		$.ajax("files?fileUri=" + directoryUri+"&fileType=directory", {
			"type" : "POST",
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

		$.ajax("files?fileUri=" + projectUri+"&fileType=project", {
			"type" : "POST",
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

		$.ajax("files?fileUri=" + fileUri +"&fileType=file", {
			"type" : "DELETE",
			"success" : function(result) {
                            if (callback)
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

		$.ajax("files?fileUri=" + directoryUri +"&fileType=directory", {
			"type" : "DELETE",
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

		$.ajax("files?fileUri=" + projectUri+"&fileType=project", {
			"type" : "DELETE",
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

	createWorkspace : function(workspaceName, description, tags, callback) {
            
		$.ajax("files/workspaces?workspaceName=" + workspaceName +"&description="+description+"&tags="+tags, {
			"type" : "POST",
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

		$.ajax("files/workspaces?workspaceName=" + workspaceUri, {
			"type" : "DELETE",
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
        deleteDemoWorkspace : function(workspaceUri, callback) {

		$.ajax("demo/"+ workspaceUri, {
			"type" : "DELETE",
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
		$.ajax("files/move?fileUri=" + fileUri + "&fileType=file&destUri=" + destUri + "&copy=" + copy, {
			"type" : "PUT",
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
		$.ajax("files/move?fileUri=" + directoryUri + "&fileType=directory&destUri=" + destUri + "&copy=" + copy,  {
			"type" : "PUT",
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
		$.ajax("files?fileUri=" + fileUri + "&fileType=file&newName=" + newName, {
			"type" : "PUT",
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
		$.ajax("files?fileUri=" + directoryUri + "&fileType=directory&newName=" + newName, {
			"type" : "PUT",
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
        
        updateWorkspace: function(newName, newDescription, callback) {
                var ws = WorkspaceManager.getSelectedWorkspace();
		$.ajax("files/workspaces?workspaceName=" + ws + "&newName=" + newName+ "&newDescription=" + newDescription, {
			"type" : "PUT",
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

		$.ajax("files/workspaces", {
			"type" : "GET",
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
		$.ajax("files/workspaces/selected", {
			"type" : "GET",
			"success" : function(result) {
				callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
			},
			"async" : true,
		});
	},
	
	setSelectedWorkspace : function ( wsName, callback ) {
		$.ajax("files/workspaces/selected?workspaceName=" + wsName, {
			"type" : "POST",
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
	
	currentWSAsDemoWS :function( wsName, callback){
		$.ajax("workspaces/"+wsName+"/toDemo", {
			"type" : "GET",
			"success" : function(result) {
                            if( callback ) callback(result);
			},
			"error" : function(result) {
				console.error(result.statusText);
				RequestHelper.sessionAlive(result);
			},
			"async" : true,
		});
	},
        
        downloadAsZip: function(wsName){
            window.location.href = "files/getAsZip/"+wsName;   
        },
        
        
        viewDemoWorkspace : function ( wsName, callback ) {
		$.ajax("demo/" + wsName, {
			"type" : "GET",
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
        
        updateDemoWorkspace : function ( wsName, callback ) {
		$.ajax("demo/" + wsName, {
			"type" : "PUT",
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
        importNewDemoWorkspace : function ( wsName, callback ) {
		$.ajax("demo/" + wsName + "/import", {
			"type" : "GET",
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