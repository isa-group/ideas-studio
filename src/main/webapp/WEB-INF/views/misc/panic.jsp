<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false" %>


<ideas:panic-template>
    <script type="text/javascript">
        function redirectToApp() {
            window.location.href = $('base').attr('href');
        }

        function toggleExMsg() {
            $("#exceptionMsg").toggleClass("hidden");
        }
    </script>

    <h2>An error has occurred</h2>
    <p>
        <spring:message code="panic.text" />
        <code>${name}</code>.
    </p>

    <button class="btn goToApp" id="moreDetails" onclick="toggleExMsg()">Details</button>
    <button class="btn goToApp" onclick="redirectToApp()">Go to ${studioConfiguration.workbenchName}</button>



    <div id="exceptionMsg" class="alert alert-block alert-error hidden">
        <!--<button type="button" class="close" data-dismiss="alert">&times;</button>-->
        <pre>
            <jstl:forEach var="stackTraceElem" items="${exception.stackTrace}">
                <jstl:out value="${stackTraceElem}" />
            </jstl:forEach>
        </pre>
    </div>
</ideas:panic-template>