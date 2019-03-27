<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script type="text/javascript">
	function redirectToApp(){
		window.location.href = $('base').attr('href');
	}
</script>

<h3>Connected to <i class="fa fa-github"></i> GitHub</h3>

<form id="disconnect" method="post">
    <div class="formInfo">
        <p>${studioConfiguration.workbenchName} is already connected to your GitHub account.
            Click the button if you wish to disconnect.			
    </div>
<!--     <button type="submit">Disconnect</button>	 -->
    <input type="hidden" name="_method" value="delete" />
</form>
<button id="goToApp" class="btn" onclick="redirectToApp()">Go to APP</button>