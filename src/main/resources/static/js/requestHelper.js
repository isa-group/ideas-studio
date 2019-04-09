var RequestHelper = {

	currentRequest : undefined,

	ajax : function(url, request, preventDefault) {
		var responseFunctions = {
			onSuccess : request["onSuccess"],
			onProblems : request["onProblems"],
			onSessionError : request["onSessionError"],
			onError : request["onError"]
		};

		var newRequest = {};

		for (key in request) {
			if (key != "onSuccess" && key != "onProblem"
					&& key != "onSessionError" && key != "onError"
					&& key != "success" && key != "error" && key != "complete") {
				newRequest[key] = request[key];
			}
		}

		newRequest["complete"] = function(result, textStatus, exc) {
			handleResponse(responseFunctions, result, textStatus, exc,
					preventDefault);
		};

		currentRequest = auxAjax(url, newRequest);
	},

	sessionAlive : function(result) {
		if (result.status === 901) {
			$("body")
					.append(
							"<div id='lostSessionPanel'>"
									+ "<p>Lost session, please go to login page</p>"
									+ "<button id='goToLogin' class='btn' onclick='window.location.href = window.location.href'>Go to Login</button>"
									+ "</div>");
			// window.location.href = window.location.href;
		}
	},

	abort : function() {
		currentRequest.abort();
	}

};

var auxAjax = function(url, rqst) {
	return $.ajax(url, rqst);
};

var handleResponse = function(responseFunctions, result, textStatus, exc,
		preventDefault) {

	try {
		if (result.statusText != "abort") {
			var resObj = eval('(' + result.responseText + ')');
			var status = resObj["status"];
			if (status == "OK" || status == "OK_PROBLEMS") {
				if (!preventDefault) {

					if (resObj.htmlMessage == null)
						CommandApi.echo(resObj.message);
					else
						CommandApi.echo(resObj.htmlMessage);

					var fileUri = resObj.fileUri;
					var annotations = resObj.annotations;

					if (fileUri == null || fileUri.length == 0)
						fileUri = EditorManager.getCurrentUri();

					if (fileUri != null && fileUri.length > 0
							&& annotations != null && annotations.length > 0
							&& EditorManager.documentIsOpen(fileUri)) {
						EditorManager.setAnnotationsToDocument(fileUri,
								annotations);
					}
				}

				if (status == "OK_PROBLEMS") {
					if (responseFunctions["onProblems"])
						responseFunctions["onProblems"](resObj);
				} else {
					console.log(resObj);
					if (responseFunctions["onSuccess"])
						responseFunctions["onSuccess"](resObj);
				}

			} else if (textStatus == "302" || status == "SESSION_ERROR") {
				if (responseFunctions["onSessionError"])
					responseFunctions["onSessionError"](result);
				location.reload();
			} else { // Errors
				CommandApi.echo("<pre>There is a problem with the document, operation can not be resolved</pre>");
                                if (resObj.htmlMessage == null)
                                    if (resObj.message == null)
                                        CommandApi.echo(resObj.message);
				else 
                                    CommandApi.echo(resObj.htmlMessage);
				if (responseFunctions["onError"])
					responseFunctions["onError"](result, exc);
				console.error("ERROR HANDLING X-RESPONSE!");
			}
			// OperationMetrics.stop();
		} else {
			console.log("Cancelled request");
		}

	} catch (e) {
		console.log(e);
		if (responseFunctions["onError"])
			responseFunctions["onError"](result, exc);
		console.error("Error in response JSON");
	}

};

// Aux functions
var objSize = function(obj) {
	var size = 0, key;
	for (key in obj) {
		if (obj.hasOwnProperty(key))
			size++;
	}
	return size;
};