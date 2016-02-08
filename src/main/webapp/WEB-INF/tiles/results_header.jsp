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
        <span>Executing Operation <a id="helpLink" href="#">operation name</a> on file ${fileUri}</span>   
    </div>
    <div id="userTab">
        <br>
        <button>Open in ${studioConfiguration.workbenchName}</button>
    </div>
</div>

