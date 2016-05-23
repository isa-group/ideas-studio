<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet" type="text/css" href="../../css/ideas.css" media="screen" />

<script type="text/javascript">
	function redirecToLogin(){
		window.location.href = $('base').attr('href');
	}
</script>

<%-- <h1><spring:message code="registration.confirmationSucces"/></h1> --%>

<%-- <h2><spring:message code="registration.nextTasks"/></h2> --%>
<%-- <spring:url value="security/useraccount/edit" var="userAccountUrl"> --%>
<%--     <spring:param name="accountId" value="${userAccount.id}"/> --%>
<%-- </spring:url> --%>
<%-- <h3><a href="${userAccountUrl}"><spring:message code="registration.customizeUsernameAndPassword"/></a></h3> --%>
<%-- <h3><a href="socialnetwork/connections/list"><spring:message code="registration.connectYourAccountWithSocialNetworks"/></a></h3> --%>
<%-- <spring:url value="researcher/edit" var="researcherUrl"> --%>
<%--     <spring:param name="researcherId" value="${researcher.id}"/> --%>
<%-- </spring:url> --%>
<%-- <h3><a href="${researcherUrl}"><spring:message code="registration.editYourPersonalData"/></a></h3> --%>
<%-- <h3><a href="experiment/create"><spring:message code="registration.createYourFirstExperiment"/></a></h3> --%>

<img src="../../img/${studioConfiguration.images.logo}">



<div id="message" class="modal show" data-backdrop="static" data-keyboard="false">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">Account validated successfully</h4>
      </div>
      <div class="modal-body">
        <p>Your account has been successfully validated. Please proceed to the login page and provides the credentials we have sent to your email.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick='redirecToLogin()'>Login</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
