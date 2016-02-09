<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

            <span id="inputsTitle"><strong>INPUTS</strong></span>
            <span id="paramsTitle"><strong>Params</strong></span>
            
            <div id="inputAceEditor">
                <c:out value='${content}' />
            </div>
            <div id="auxParams">
                <textarea id="paramsArea" >
                    <c:out value='${params}' />
                    <c:out value='${data}' />
                </textarea>
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
        
            
        

<script>
                var editor = ace.edit("inputAceEditor");
                editor.setTheme("ace/theme/SEDL4People");
                editor.getSession().setMode("ace/mode/SEDL4People");
            </script>

<script src='js/operationalReplication.js' type="text/javascript"></script>

    <script type="text/javascript">
        var modelAttributeValue = '${modelAttribute}';
        //TODO
    </script>

