<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script type="text/javascript">
	function redirecToApp(){
		window.location.href = $('base').attr('href');
	}
</script>

<h3>Connected to Twitter</h3>

<form id="disconnect" method="post">
    <div class="formInfo">
        <p>${studioConfiguration.workbenchName} is already connected to your Twitter account.
            Click the button if you wish to disconnect.			
    </div>
<!--     <button type="submit">Disconnect</button>	 -->
    <input type="hidden" name="_method" value="delete" />
</form>
<button id="goToApp" class="btn" onclick="redirecToApp()">Go to APP</button>
<p></p>
<h3>Unfortunately, Twitter does not provide us with a detailed information about you (such as your e-mail).
    If you have signed up using your twitter account, please, spend a few seconds completing your user profile (specially your contact e-mail), so we can
    send you updated information about ${studioConfiguration.workbenchName}. 
</h3>
<a class="btn" href="settings/user">Complete my profile</a>
