<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script type="text/javascript">
	function redirectToApp(){
		window.location.href = $('base').attr('href');
	}
</script>

<h3>Connected to Google - <i class="fa fa-google-plus"></i> </h3>

    <div class="formInfo">
        <p>${studioConfiguration.workbenchName} is now connected to your Google account.</p>
    </div>
<button id="goToApp" class="btn" onclick="redirectToApp()">Go to ${studioConfiguration.workbenchName}</button>