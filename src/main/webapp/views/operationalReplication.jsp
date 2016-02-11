<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<span id="inputsTitle"><strong>INPUT</strong></span>
<span id="paramsTitle"><strong></strong></span>

<div id="inputAceEditor">
    <c:out value='${content}' />
</div>
<div id="auxParams">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#home">Data</a></li>
        <li><a data-toggle="tab" href="#menu1">Params</a></li>
        <li><a data-toggle="tab" href="#menu2">...</a></li>
    </ul>
    <div class="tab-content">
        <div id="home" class="tab-pane fade in active">
            <textarea id="dataTextarea" cols="40" rows="10">
                <c:out value='${data}' />
            </textarea>
        </div>
        <div id="menu1" class="tab-pane fade">
            <textarea id="paramsTextarea" cols="40" rows="10">
                <c:out value='${params}' />
            </textarea>
        </div>
        <div id="menu2" class="tab-pane fade">
            <textarea cols="40" rows="10"></textarea>
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
