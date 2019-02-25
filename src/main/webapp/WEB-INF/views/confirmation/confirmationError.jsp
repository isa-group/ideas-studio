<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<script type="text/javascript">
    function redirectToApp() {
        window.location.href = $('base').attr('href');
    }
</script>

<div>
    <h2><spring:message code="registration.errorTitle"/></h2>
    <spring:message code="registration.confirmationError"/>
</div>
<button class="btn goToApp" onclick="redirectToApp()">Go to ${studioConfiguration.workbenchName}</button>