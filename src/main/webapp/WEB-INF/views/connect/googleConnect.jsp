<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>

<ideas:pages-template>
  <div id="settingsContainer">
    <h3><spring:message code="social.disconnected_from" /> Google</h3>
    <p><spring:message code="social.will_redirect"/>
  </div>

  <script type="text/javascript">
    setTimeout(function() {
      window.location.href = "/app/editor";
    }, 5000);
  </script>
</ideas:pages-template>