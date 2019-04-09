var timeOut;
var displayTime;

var OperationMetrics = {
	init : "",
	end : "",
        timeout: null,
	getInit : function() {
		return OperationMetrics.init;
	},
	getEnd : function() {
		return OperationMetrics.end;
	},
	resetTime : function() {
		OperationMetrics.init = "";
		OperationMetrics.end = "";
	},
	getOperationMilliseconds : function() {
		return OperationMetrics.end - OperationMetrics.init;
	},
	getInitTime : function() {
		return OperationMetrics.init.getHours() + ":"
				+ OperationMetrics.init.getMinutes() + ":"
				+ OperationMetrics.init.getSeconds();
	},
	getEndTime : function() {
		return OperationMetrics.end.getHours() + ":"
				+ OperationMetrics.end.getMinutes() + ":"
				+ OperationMetrics.end.getSeconds();
	},
	getOperationDate : function() {
		return OperationMetrics.init.getDate();
	},

	play : function(nameOp) {
		OperationMetrics.resetTime();
		OperationMetrics.init = new Date();
		var p = "<div style=''>[" + OperationMetrics.getInitTime()
				+ "] Executing <b>" + nameOp + "</b> operation</div>";
		var div = "<div>" + p + "</div>";
		CommandApi.echo(div);

		$(".btn-primary").attr("disabled", "disabled");
                if(OperationMetrics.timeout==null){
                    OperationMetrics.timeout = setTimeout(
				function() {

					if ($("#executingPopup") != undefined) {
						$("#executingPopup").remove();
					}

					var executingPopup = "<div id=\"executingPopup\" class=\"modal fade\" data-backdrop=\"static\" "
							+ "data-keyboard=\"false\" >"
							+ "<div class=\"modal-dialog\">"
							+ "<div class=\"modal-content\">"
							+ "<div class=\"modal-header\">"
							+ "<h4 class=\"modal-title\">Please wait</h4>"
							+ "</div>"
							+ "<div class=\"modal-body\">"
							+ "<p>Executing operation <b>"
							+ nameOp
							+ "</b></p>"
							+ "<p>Elapsed time: <span id=\"elapsedTime\"></span></p>"
							+ "<img id='loadIcon' class='center-block' src='img/ideas/loading1_big_lgbg.gif'"
							+ "width='50'/>"
							+ "</div>"
							+ "<div class=\"modal-footer\">"
							+ "<button type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\" "
							+ "onclick=\"OperationMetrics.abort()\">Cancel</button>"
							+ "</div>"
							+ "</div><!-- /.modal-content -->"
							+ "</div><!-- /.modal-dialog -->" + "</div>";

					$(executingPopup).modal('show');

					displayTime = setInterval(OperationMetrics.updateTime, 1000);

				}, 2000);
                            }
	},
	stop : function() {
                clearTimeout(OperationMetrics.timeout);
                OperationMetrics.timeout=null;
		OperationMetrics.end = new Date();

		$(".btn-primary").removeAttr("disabled");
		var div = "<p>[" + OperationMetrics.getEndTime()
				+ "] Execution finish in "
				+ OperationMetrics.getOperationMilliseconds() + " milsec.</p>";

		CommandApi.echo(div);

		$("#executingPopup").modal('hide');
		OperationMetrics.reset();
	},

	abort : function() {
		RequestHelper.abort();
		OperationMetrics.end = new Date();

		$(".btn-primary").removeAttr("disabled");
		var div = "<p>[" + OperationMetrics.getEndTime()
				+ "] Execution cancelled</p>";

		CommandApi.echo(div);

		$("#executingPopup").modal('hide');
		OperationMetrics.reset();
	},

	reset : function() {
		OperationMetrics.resetTime();
		clearTimeout(OperationMetrics.timeout);
		clearInterval(displayTime);
	},

	updateTime : function() {
		var ms = new Date() - OperationMetrics.init;
		$("#elapsedTime").text(msToTime(ms));
	}
};

function msToTime(duration) {
    var seconds = parseInt((duration/1000)%60);
    var minutes = parseInt((duration/(1000*60))%60);
    return addZero(minutes) + ":" + addZero(seconds);
}

function addZero(i) {
	if (i < 10) {
		i = "0" + i;
	}
	return i;
}
