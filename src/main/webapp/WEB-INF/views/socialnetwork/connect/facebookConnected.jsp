<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ page session="false" %>

<script type="text/javascript">
	function redirectToApp(){
		window.location.href = $('base').attr('href');
	}
</script>

<h3>Connected to Facebook</h3>

<form id="disconnect" method="post">
    <div class="formInfo">
        <p>
            ${studioConfiguration.workbenchName} is connected to your Facebook account.
            Click the button if you wish to disconnect.
        </p>		
    </div>
<!--     <button type="submit">Disconnect</button>	 -->
    <input type="hidden" name="_method" value="delete" />
</form>
<button id="goToApp" class="btn" onclick="redirectToApp()">Go to APP</button>