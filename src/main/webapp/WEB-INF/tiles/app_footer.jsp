<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div id="appFooter">
	<span id="version"> v 0.29 f317c12</span>
	<span id="appFooterCopyright">
    	<spring:message code="app.footer.copyright" />
    </span>
    <span id="isaLogoSmall">
    	<a href="http://www.isa.us.es/" target="_blank"><img src="img/ideas/isaLogoSmall.png" alt="ISA"></a>
    </span>
</div>

<div id="versions">
<img id="vClose" alt="" src="img/ideas/basic-icon-x.png">
<div id="vers-info">
	<p>V 0.29 f317c12 release 11/12/2014</p>
	<ul>
	  <li>Demo users service</li>
	  <li>Run sychronous operations</li>
	  <li>Report result fixed to show operation results</li>
	  <li>Bugs fron lost session fixed</li>
	  <li>Project explored fixed</li>
	  <li>Result operation modal fixed</li>
	  <li>Refactor command deleteCurrentWorkspace</li>
	  <li>Bugs deleting workspaces fixed</li>
	  <li>Refactor settings view</li>
	</ul>
	<p>V 0.28 "Heisenberg" aa44911 release 24/10/2014</p>
	<ul>
	  <li>File name repeat bug fixed</li>
	  <li>Analysis report drop button</li>
	  <li>Report result fixed to show operation results</li>
	  <li>Fix logout and user menu settings in new window</li>
	  <li>Fix create files with .fm extension</li>
	  <li>Fix create files with enter key</li>
	  <li>Create csv module</li>
	  <li>Fix context menu z-index</li>
	  <li>Fix console css</li>
	  <li>Fix create and delete workspaces bugs</li>
	  <li>Create IDEAS help</li>
	</ul>
	<p>V 0.27 "Lovelance" cc95441 release 01/09/2014</p>
	<ul>
	  <li>Handler press "intro" to login and modal views</li>
	  <li>Analysis report</li>
	  <li>Download reports</li>
	  <li>Default file name bug repair</li>
	  <li>Create file bugs fixed</li>
	</ul>
	<p>V 0.26 "Dijkstra" f82bc05 release 28/07/2014</p>
	<ul>
	  <li>Code refactoring</li>
	  <li>Contex Menu</li>
	  <li>File upload</li>
	  <li>Default file name bug repair</li>
	  <li>Projects templates</li>
	</ul>
	<p>V 0.25 a50393e release 26/06/2014</p>
	<ul>
	  <li>Lost session fails repair</li>
	  <li>Console text is selectable</li>
	  <li>Create directories bug repair</li>
	  <li>Special characters on files names bug repair</li>
	  <li>Css fails repair</li>
	</ul>
</div>

</div>