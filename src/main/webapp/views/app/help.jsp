
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>
	jQuery(function() {
	
		AppPresenter.setCurrentSection("help");
	});
</script>
<div id='help'>
    <iframe src="views/help/index.jsp" id='iframeHelp'></iframe>		
</div>