<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<script type="text/javascript">
	function redirecToLogin(){
		window.location.href = $('base').attr('href');
	}
</script>

<div id="message" class="modal show" data-backdrop="static" data-keyboard="false">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">Error validating account</h4>
      </div>
      <div class="modal-body">
        <p><spring:message code="registration.confirmationError"/></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick='redirecToLogin()'>Login</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->