<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="appHeader">
    <div id="appLogo" style="background-image: url('./img/${studioConfiguration.images['logo']}')"> 
    </div>
    <div id="pagesHeaderLogo">
        <br>
        <span>Executing Operation <strong><a id="helpLink">${operation}</a></strong> on file <span id="fileUriText">${fileUri}</span></span>  
    </div>
    <div id="userTab">
        <br>
        <a id="openDemoLink" href="${base}demo/${workspace}" class="btn btn-default btn-xs" role="button">Open in ${studioConfiguration.workbenchName}</a>
    </div>
</div>

