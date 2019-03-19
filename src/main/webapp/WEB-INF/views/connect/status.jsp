<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>

<ideas:pages-template>
  <h3>Your Connections</h3>
  <span></span>
  <!-- TODO: Thymeleaf seems to be tacking local/language on the end of properties..."facebook.displayName_en_US"...BECAUSE it's looking up a property and can't find it!!! -->
  <jstl:forEach items="${providerIds}" var="providerId">
      <span>${providerId}</span><br>
  </jstl:forEach>
</ideas:pages-template>