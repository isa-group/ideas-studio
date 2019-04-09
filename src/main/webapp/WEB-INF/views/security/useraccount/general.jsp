<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>



<div id="generalSettings" class="">
	<form:form id="editGeneralForm" action="security/useraccount/edit"
		modelAttribute="account">
		<h3>
			<spring:message code="userAccount.general.language" />
		</h3>
		<form:hidden path="id" />
		<form:hidden path="version" />

		<div class="control-group">
<%-- 			<form:label name="language" cssClass="control-label span1"> --%>
<%-- 				<spring:message code="userAccount.general.language" /> --%>
<%-- 			</form:label> --%>
			<select name=selectInput >
	        	<option VALUE="en">English</option>  
	        	<option VALUE="es">Español</option>  
     	 	</select>  
			<form:errors class="formError" name="language" />
		</div>

	</form:form>
</div>

