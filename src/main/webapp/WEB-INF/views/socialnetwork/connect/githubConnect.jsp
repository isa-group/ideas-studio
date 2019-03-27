<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>

<h3>Connect to GitHub <i class="fa fa-github"></i></h3>

<form action="<c:url value="/connect/github" />" method="POST">
    <div class="formInfo">
        <p>
            Click the button to connect ${studioConfiguration.workbenchName} with your GitHub account. 
            (You'll be redirected to GitHub where you'll be asked to authorize
            the connection.)
        </p>
    </div>


    <p>
        <button type="submit" class="btn btn-social btn-github">
            <i class="fa fa-github"></i> Connect with GitHub                        
        </button>    
    </p>    
</form>
