<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<%@attribute name="changelog"%>
<div id="appFooter">
	<div id="version">v${project.version}</div>
	<span id="appFooterCopyright"> 
            <spring:message code="app.footer.copyright" />
	</span> <span id="isaLogoSmall"> <a href="http://www.isa.us.es/"
		target="_blank"> <img src="img/ideas/isaLogoSmall.png" alt="ISA">
	</a>
	</span>
</div>

<div id="versions" style="display: none;">
	<img id="vClose" alt="" src="img/ideas/basic-icon-x.png">
	<div id="vers-info">
		${changelog}
	</div>

</div>