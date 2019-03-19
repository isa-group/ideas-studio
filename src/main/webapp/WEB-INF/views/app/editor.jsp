<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<ideas:app-template>
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
    <script src='js/vendor/bootstrap.tooltip-popover.min.js' type="text/javascript"></script>
    <script src='js/descriptionInspector.js' type="text/javascript"></script>
    <script src='js/vendor/moment.js' type="text/javascript"></script>
    <script src='js/vendor/showdown.min.js' type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="css/descriptionInspector.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/vendor/datetimepicker.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/gcli-console.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/jquery.contextMenu.css" media="screen" />
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

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
            <%--
            <!--ul id="myMenu" class="contextMenu">
                <c:if test="${studioConfiguration.advancedMode}">
                    <li class="edit"><a href="#edit">Edit</a></li>
                    <li class="edit"><a href="#editDescription">Edit description</a></li>
                    </c:if>
                <li class="cut separator"><a href="#cut">Cut</a></li>
                <li class="copy"><a href="#copy">Copy</a></li>
                <li class="paste"><a href="#paste">Paste</a></li>
                <li class="delete"><a href="#delete">Delete</a></li>
                    <c:if test="${studioConfiguration.advancedMode}">
                    <li class="upload separator"><a href="#upload">Upload</a></li>
                    </c:if>
                <li class="upload separator"><a href="#download">Download</a></li>
            </ul-->
            --%>
            <div id="projectsTree"> </div>
        </div>
        <div id="wsactions">
            <c:if test="${studioConfiguration.advancedMode}">
                <a id="demo-ws" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="editor.actions.publish_demo" />">
                    <i class="material-icons">cloud_upload</i>
                </a>
            </c:if>
            <a id="download-ws" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="editor.actions.download_zip" />">
                <i class="material-icons">file_download</i>
            </a>
            <c:if test="${studioConfiguration.advancedMode}">
                <a id="edit-ws" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="editor.actions.edit_workspace" />">
                    <i class="material-icons">edit</i>
                </a>
            </c:if>
            <a id="delete-ws" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="editor.actions.delete_workspace" />">
                <i class="material-icons">delete</i>
            </a>
            <label class="switch">
                <input onchange="toggleAdvancedMode()" type="checkbox">
                <div class="slider round"></div>
                <span style="
                      position: absolute;
                      bottom: 0;
                      font-size: 8px;
                      margin-bottom: -20px;
                      right: 6%;
                      text-align: center;
                      color: #777;
                      ">Advanced</span>
            </label>
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
                    <span id="bindingManagerPanelWrapper" ng-show="languageBindingsManifestLen > 0">
                        <select id="bindingManagerPanel" ng-model="currentBinding">
                            <option value="">---</option>
                            <option ng-repeat="(k,v) in languageBindingsManifest" value="{{v}}" ng-show="k != 'default'">
                                {{v.name}}
                            </option>
                        </select>
                    </span>
                </div>
            </div>

            <div id="editorWrapper">
                <div id="editorTopShadow"></div>
                <div id="editor"></div>

                <!-- Icon checker -->
                <div class="ball-clip-rotate">
                    <div class="editor-checker-loading" title="Validating current file content..."></div>
                    <div class="editor-checker-icon editor-checker-icon-ok" title="Current file content is valid">
                        <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                    </div>
                    <div class="editor-checker-icon editor-checker-icon-error" onclick="EditorCheckerIcon.toggleBoard()" title="Current file content is invalid">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </div>
                </div>
                <div class="ball-clip-rotate editor-checker-board"></div>
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

    <div id="editorInspector" class="ui-widget-content hdd">
        <div id="editorInspectorLoader">

        </div>
    </div>
    <!-- script require for the internationalizaton of the menus -->
    <script>
        var menuMessages = {};
        menuMessages["createNewProject"] = 'Create Project';
        menuMessages["createNewDirectory"] = "Create Directory";
        menuMessages["uploadFile"] = "Upload File";
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
</ideas:app-template>