
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src='js/dyntree/projectsTree.js' type="text/javascript"></script>
<script src='js/languageModeManager.js' type="text/javascript"></script>
<script src='js/editorManager.js' type="text/javascript"></script>
<script src='js/menuManager.js' type="text/javascript"></script>
<script src='js/editor.js' type="text/javascript"></script>
<script src='js/studioSetup.js' type="text/javascript"></script>
<script src='js/operationsMetrics.js' type="text/javascript"></script>
<script src='js/operationsReport.js' type="text/javascript"></script>
<script src="js/gcli-console/commands.js" type="text/javascript"></script>
<script src="js/gcli-console/gcli-uncompressed.js" type="text/javascript"></script>
<!-- DescriptionInspector -->
<script src='js/vendor/bootstrap.tooltip-popover.min.js' type="text/javascript"></script>
<script src='js/descriptionInspector.js' type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css/descriptionInspector.css" media="screen" />
<!-- End DescriptionInspector -->
<link rel="stylesheet" type="text/css" href="css/gcli-console.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/jquery.contextMenu.css" media="screen" />

<script>
        $(document).ready(initializeEditor);	
</script>

<div id="editorSidePanel" class="ui-widget-content">
	<div id="editorSidePanelHeader">
		<span id="editorSidePanelHeaderWorkspaceInfo"></span> 
		<div id="editorSidePanelHeaderAddProject">
			<div class="btn-group">
			  <div class="btn btn-primary dropdown-toggle" data-toggle="dropdown">+</div>
			  <ul class="dropdown-menu" role="menu">
				<!-- loaded dinamically -->
			  </ul>
			</div>
		</div>
	</div>
	<div id="projectsTreeContainer">
		<ul id="myMenu" class="contextMenu">
			<li class="edit"><a href="#edit">Edit</a></li>
			<li class="cut separator"><a href="#cut">Cut</a></li>
			<li class="copy"><a href="#copy">Copy</a></li>
			<li class="paste"><a href="#paste">Paste</a></li>
			<li class="delete"><a href="#delete">Delete</a></li>
 			<li class="upload separator"><a href="#upload">Upload</a></li>
		</ul>
		<div id="projectsTree"> </div>
                </div>
        <div id="wsactions">

        </div>
	</div>

<div id="editorMainPanel">
	<div id="editorItself">
		<div id="editorHeader">
<!-- 			<div id="editorHeaderCenterInfo">project/dir/dir/file.sedl</div> -->
			<div id="editorTabsContainer">
				<ul id="editorTabs" class="nav nav-tabs">
				</ul>
			</div>
			<div id="editorHeaderRightButtonsPanel">
				<span id="editorToggleInspector"></span>
				<span id="editorMaximize"></span>
				<span id="shareDocument" class="shareDocument"></span>
<!-- 				<span id="editorOutLink"></span> -->
<!-- 				<span id="editorSocial"></span> -->
			</div>
		</div>
		
		<div id="editorWrapper">
			<div id="editorTopShadow"></div>
			<div id="editor"></div>
            <input id="editorContent" type="text" ng-change="slaString2Model()" ng-model="slaString" />
		</div>
		
		<div id="editorFooter">
			<ul id="editorFormats" class="nav nav-tabs">
			</ul>
		</div>

	</div>
	<div id="editorBottomPanel">
		<div id="editorBottomPanelSwitcher">
			<div id="editorActions">
<!-- 				<div class="btn btn-primary" id="analyze"> -->
<!-- 					<span class="glyphicon glyphicon-play"></span> -->
<%-- 					<spring:message code="editor.actions.analyze" /> --%>
<!-- 				</div> -->
			</div>
		</div>
		<div id="editorBottomPanelContentLoader" class="light">
			<div id="consoleWrapper">
				<div id="gcli-root">				
                                    
				</div>
			</div>
		</div>
	</div>
</div>

<inspector-model ng-click="onClicked()">
<div id="editorInspector" class="ui-widget-content hdd">
	<div id="editorInspectorLoader">
		
	</div>
</div>
</inspector-model>
<!-- script require for the internationalizaton of the menus -->
<script>
    var menuMessages={};
    menuMessages["createNewProject"]='Create Project';
    menuMessages["createNewDirectory"]="Create Directory";
    menuMessages["uploadFile"]="Upload File";
</script>

<div id="shareDocumentModal">
	<div class="modal-header">
	    <h4 class="modal-title">Share Document</h4>
	</div>                       
	<div id="modalCreationMail" >
		<div>
			<span class="spn">To:</span>
			<input type="text" class="form-control input-share" id="mailTo">
		</div>
		<div>
			<span class="spn">Mail:</span>
			<textarea class="form-control input-share" id="mailContent"></textarea>
		</div>
	</div>	
	<div id="footer">
	    <a id="shareDocClosed" class="btn btn-primary">Cancel</a>
	    <a id="sendMail" class="btn btn-primary continue">Send</a>
	</div>
</div>