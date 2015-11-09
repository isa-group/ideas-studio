
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>
	jQuery(function() {
	
		AppPresenter.setCurrentSection("help");
	});
</script>
<div id='help'>
<!-- 	<iframe src="https://labs.isa.us.es/IDEAS-help/" scrolling="auto" id='iframeHelp'></iframe>  -->
	
<iframe src="${studioConfiguration.helpURI}" id='iframeHelp'></iframe> 
		
</div>