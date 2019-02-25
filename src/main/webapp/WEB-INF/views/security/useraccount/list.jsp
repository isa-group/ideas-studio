<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="accounts" id="row"
	requestURI="security/useraccount/list" pagesize="10">
	<spring:message code="row.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />
	<spring:url value="security/useraccount/edit" var="accountURL">
		<spring:param name="accountId" value="${row.id}" />
	</spring:url>
	<display:column>

		<a href="${accountURL}" class="btn"> <spring:message
				code="actions.edit" />
		</a>
	</display:column>
	<display:column>

		<form:form action="${accountURL}">
			<form:hidden path="id" />
			<form:hidden path="version" />
			<input type="submit" name="delete" id="delete">
			<spring:message code="action.delete" />
		</form:form>
	</display:column>


</display:table>