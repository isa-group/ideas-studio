<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="dataWrapper">
    <div id="inputWrapper">
        INPUTS
        <div id="inputEditor">
            <p><c:out value='${content}' /></p>
        </div>
        PARAMS
        <div id="auxParamsInput">
            <p><c:out value='${params}' /></p>
        </div>
        <div id="dataFileLink">
            <p><c:out value='${data}' /></p>
        </div>
        RESULTS
        <div id="resultsConsole">
            <div class="btn btn-primary" id="executeTest"> 
                    <span class="glyphicon glyphicon-play"></span>
                    EXECUTE 
            </div> 
            <div id="consoleWrapper">
                    <div id="gcli-root">				

                    </div>
            </div>
        </div>
        
        
   
    <script type="text/javascript">
        var modelAttributeValue = '${modelAttribute}';
        //TODO
    </script>
    
    <script src='js/operationalReplication.js' type="text/javascript"></script>
    </div>
</div>




