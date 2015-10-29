
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>


<form:form action="j_spring_security_check" modelAttribute="credentials"
	id="loginForm">
	<div class="control-group">
		<%-- 			<form:label path="username" cssClass="control-label span2"> --%>
		<%-- 				<spring:message code="security.username" /> --%>
		<%-- 			</form:label> --%>
		<div class="input-group">
			<span class="input-group-addon"><spring:message
					code="security.username" /></span>
			<form:input path="username" class="form-control" />
		</div>
		<form:errors class="error" path="username" />
	</div>

	<div class="control-group">
		<%-- 			<form:label path="password" cssClass="control-label span2"> --%>
		<%-- 				<spring:message code="security.password" /> --%>
		<%-- 			</form:label> --%>
		<div class="input-group">
			<span class="input-group-addon"><spring:message
					code="security.password" /></span>
			<form:password path="password" class="form-control" />
		</div>
		<form:errors class="formError" path="password" />
	</div>

	<jstl:if test="${showError == true}">
		<div class="alert alert-error" style="visibility: hidden;">
			<button type="button" class="formError" data-dismiss="alert">&times;</button>
			<spring:message code="security.login.failed" />
		</div>
	</jstl:if>

	<!-- 		<div class="form-actions"> -->
	<%-- 			<input type="submit" value="<spring:message code="security.login" />" --%>
	<!-- 				class="btn" /> -->
	<!-- 		</div> -->
	<div class="row-fluid" id="dontRememberLogin">
		<a href="settings/user"> <b><spring:message
				code="security.signup" /></b></a>
		<BR/>
		 <a href="security/useraccount/resetPassword"><spring:message
				code="security.IdontRemember" /></a>
	</div>
</form:form>




