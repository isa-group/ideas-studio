<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<ideas:social-template>
<p><spring:message code="security.resetPassword"/></p>
<div id="resetLoader">
    <form id="resetPasswordForm" action="security/useraccount/resetPassword" method="post">
        <div class="control-group">
            <div class="input-group">
                <span class="input-group-addon">Email:</span>
                <input id="email" name="email" class="form-control" type="email" value="" autocomplete="on" />
            </div>
            <div id="resetButtons">
                <input id="submit" type="submit" value="<spring:message code="action.reset" />" class="btn ideasButton">
            </div>
        </div>
    </form>
</div>
</ideas:social-template>