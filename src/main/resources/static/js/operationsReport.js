/**
 * 
 */
(function() {
	with (this[2]) {
		with (this[1]) {
			with (this[0]) {
				return function(event) {
					downloadFile();
				};
			}
		}
	}
});

document.body.addEventListener('dragstart', function(e) {
  var a = e.target;
  if (a.classList.contains('dragout')) {
    e.dataTransfer.setData('DownloadURL', a.dataset.downloadurl);
  }
}, false);

document.body.addEventListener('dragend', function(e) {
  var a = e.target;
  if (a.classList.contains('dragout')) {
    cleanUp(a);
  }
}, false);

var OperationReport = {
		launchedOperations: [],
		resultLaunchedOperations: [],
		timeOfOperations: [],
		createPDFLink:function(fileName){
			var MIME_TYPE = 'application/javascript.';

			var bb = new Blob(OperationReport.reportFileText(), {type: MIME_TYPE});
			var output = document.getElementById("downloadReport");
			
			var a = output;
		    a.download = "IDEAS-Report.txt";
		    a.href = window.URL.createObjectURL(bb);
		    a.textContent = 'Download ready';

		    a.dataset.downloadurl = [MIME_TYPE, a.download, a.href].join(':');
		    a.draggable = true; // Don't really need, but good practice.
		    a.classList.add('dragout');

		    a.onclick = function(e) {
		        if ('disabled' in this.dataset) {
		          return false;
		        }
		        cleanUp(this);
		    };
		},
		reportFileText:function(){
			var result = ["User-------------------------------------------------------\r\n" + AppPresenter.getPrincipalUser(),
			              "\r\n\r\nDate-------------------------------------------------------\r\n"+new Date(),
			              "\r\n\r\nPath-------------------------------------------------------\r\n"+EditorManager.currentUri,
			              "\r\n\r\nDocument content-------------------------------------------\r\n"+EditorManager.getCurrentEditorContent(),
			              ];
			var operations = OperationReport.launchedOperations;
			var resOp = OperationReport.resultLaunchedOperations;
			var times = OperationReport.timeOfOperations;
			for(var i=0;i<OperationReport.launchedOperations.length; i++){
				result.push("\r\n\r\nOperation ["+operations[i]+"]===========================================\r\n");
				result.push("Result-------------------------------------------------------\r\n");
				result.push(resOp[i]+".\r\n");
				result.push("Metrics------------------------------------------------------\r\n");
				result.push("Operation finished in: "+times[i]+" milliseconds");
			}
			return result;
		},
		resetForNewReport:function(){
			OperationReport.launchedOperations = [];
			OperationReport.resultLaunchedOperations = [];
			OperationReport.resultLaunchedOperations = [];
		}
};

var cleanUp = function(a) {
	  a.textContent = 'Downloaded';
	  a.dataset.disabled = true;

	  // Need a small delay for the revokeObjectURL to work properly.
	  setTimeout(function() {
	    window.URL.revokeObjectURL(a.href);
	  }, 1500);
};