<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>



<form class="form-horizontal span8 offset2 well" action="security/useraccount/resetPassword" method="post">
    <h2><spring:message code="userAccount.editHeader"/></h2>
    <div class="control-group">                
        <label for="email" cssClass="control-label span1">
            <spring:message code="researcher.email"/>
        </label>
        <input id="email" name="email" />                      
    </div>

    <div class="form-actions">
        <input id="submit" type="submit" value="<spring:message code="action.reset" />" class="btn"/>
    </div>
</form>
