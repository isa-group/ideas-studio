<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>

<h3>Connect to Twitter</h3>

<form action="<c:url value="/connect/twitter" />" method="POST">
    <div class="formInfo">
        <p>
            Click the button to connect ${studioConfiguration.workbenchName} with your Twitter account. 
            (You'll be redirected to Twitter where you'll be asked to authorize
            the connection.)
        </p>
    </div>


    <p>
        <button type="submit" class="btn btn-social btn-twitter">
            <i class="fa fa-twitter"></i> Connect with Twitter                        
        </button>    
    </p>    
</form>
