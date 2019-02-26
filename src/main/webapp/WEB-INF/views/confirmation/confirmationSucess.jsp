<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<ideas:social-template>
    <script type="text/javascript">
        function redirectToApp() {
            window.location.href = $('base').attr('href');
        }
    </script>

    <div>
        <h2><spring:message code="registration.checkYourEmail"/></h2>
        <spring:message code="registration.confirmationSent"/>
    </div>
    <button class="btn goToApp" onclick="redirectToApp()">Go to ${studioConfiguration.workbenchName}</button>
</ideas:social-template>