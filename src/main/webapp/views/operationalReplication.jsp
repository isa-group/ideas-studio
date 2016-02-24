<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<span id="inputsTitle"><strong>INPUT</strong></span>
<span id="paramsTitle"><strong></strong></span>

<div id="inputAceEditor">
    <c:out value='${content}' />
</div>
<div id="auxParams">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#auxArg0Tab">Data</a></li>     
        <c:if test="${fn:length(auxArg1) > 0}" ><li><a data-toggle="tab" href="#auxArg1Tab">Arg 1</a></li></c:if>
        <c:if test="${fn:length(auxArg2) > 0}" ><li><a data-toggle="tab" href="#auxArg2Tab">Arg 2</a></li></c:if>
        <c:if test="${fn:length(auxArg3) > 0}" ><li><a data-toggle="tab" href="#auxArg3Tab">Arg 3</a></li></c:if>
        <c:if test="${fn:length(auxArg4) > 0}" ><li><a data-toggle="tab" href="#auxArg4Tab">Arg 4</a></li></c:if>
    </ul>
    <div class="tab-content">
        <div id="auxArg0Tab" class="tab-pane fade in active">
            <textarea id="auxArg0Data" cols="49" rows="10">
                <c:out value='${auxArg0}' />
            </textarea>
        </div>
        <div id="auxArg1Tab" class="tab-pane fade">
            <textarea id="auxArg1Data" cols="49" rows="10">
                <c:out value='${auxArg1}' />
            </textarea>
        </div>
            <div id="auxArg2Tab" class="tab-pane fade">
            <textarea id="auxArg2Data" cols="49" rows="10">
                <c:out value='${auxArg2}' />
            </textarea>
        </div>
            <div id="auxArg3Tab" class="tab-pane fade">
            <textarea id="auxArg3Data" cols="49" rows="10">
                <c:out value='${auxArg3}' />
            </textarea>
        </div>
            <div id="auxArg4Tab" class="tab-pane fade">
            <textarea id="auxArg4Data" cols="49" rows="10">
                <c:out value='${auxArg4}' />
            </textarea>
        </div>
       
             
    </div>
</div>
<div class="btn btn-primary" id="executeTest"> 
    <span class="glyphicon glyphicon-play"></span>
    EXECUTE 
</div>
<span id="resultsTitle"><strong>RESULTS:</strong></span>
<div id="consoleWrapper">
    <div id="gcli-root">				

    </div>
</div>


<script type="text/javascript" charset="utf-8" src="js/operationalReplication.js" ></script>
<script type="text/javascript" charset="utf-8" >
    var editor = ace.edit("inputAceEditor");

//    var languageId = ModeManager
//            .calculateLanguageIdFromExt(ModeManager
//                    .calculateExtFromFileUri(fileUri));
//    var editorThemeId;
//    var formats = ModeManager.languageModeMap[languageId].formats;
//    var currentFormat = EditorManager.sessionsMap[fileUri]
//            .getCurrentFormat();
//    // TODO: Refactor!
//    for (var f in formats) {
//        if (formats[f].format == currentFormat)
//            editorThemeId = formats[f].editorThemeId;
//    }
//    console.log("## editorThemeId: " + editorThemeId);
//    document.editor.setTheme(editorThemeId);
editor.getSession().setMode("ace/mode/SEDL4People");
editor.setTheme("ace/theme/SEDL4People");
initializeCommandLine();
</script>
