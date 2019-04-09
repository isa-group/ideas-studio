<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script type="text/javascript">
	function redirectToApp(){
		window.location.href = $('base').attr('href');
	}
</script>

<h3>Connected to Twitter - <i class="fa fa-twitter"></i> </h3>

    <div class="formInfo">
        <p>${studioConfiguration.workbenchName} is now connected to your Twitter account.</p>
        <p>Unfortunately, Twitter does not provide us with a detailed information about you (such as your e-mail).</p>
        <p>If you have signed up using your Twitter account, please, spend a few seconds <b>completing your user profile</b> (specially your contact e-mail), so we can send you updated information about ${studioConfiguration.workbenchName}.</p>
            
    </div>
<a id="completeYourProfile" class="btn" href="settings/user">Complete my profile</a>