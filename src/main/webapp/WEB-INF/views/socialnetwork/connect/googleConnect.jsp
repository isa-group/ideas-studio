<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>

<h3>Connect to <i class="fa fa-google-plus"></i> Google </h3>

<form action="<c:url value="/connect/google" />" method="POST">
    <div class="formInfo">
        <p>
            Click the button to connect ${studioConfiguration.workbenchName} with your Google account. 
            (You'll be redirected to Twitter where you'll be asked to authorize
            the connection.)
        </p>
    </div>


    <p>
        <input type="hidden" name="scope" value="profile">
        <button type="submit" class="btn btn-social btn-google-plus">
            
            <i class="fa fa-google-plus"></i> Connect with Google                       
        </button>    
    </p>    
</form>
