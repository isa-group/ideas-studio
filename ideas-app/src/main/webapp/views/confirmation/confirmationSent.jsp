<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>



<h2><spring:message code="registration.confirmationGreeting"/></h2>
<div class="row-fluid">
    <div class="span3"> 
        <img src="img/email-sent.jpeg" alt="Email sent">
    </div>
    <div class="span9">
        <spring:message code="registration.confirmationSent"/>
    </div>
</div>