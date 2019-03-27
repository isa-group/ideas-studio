
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>


<!--<div id="Social Data">

<div id="socialneworksconfig">
    <h3><spring:message code="socialnetwork.editHeader"/> </h3
    >
    <jstl:forEach  items="${missingServices}" var="snetwork">
        <div id="${snetwork}-connections">            
                <div id="connect-${snetwork}">
                    <a href='<jstl:url value="connect/${snetwork}" />'><img src="img/${snetwork}-metal-icon.png">Connect to ${snetwork}</a>                    
                </div>
                <hr>
        </div>
    </jstl:forEach>
    <jstl:forEach var="sconfig " items="${servicesConfigs}">            
        <div id="">
                <div id="${sconfig.service}-configuration" class="well">
                    <spring:url value="socialnetwork/edit" var="${serviceConfigurationURL}">
                        <spring:param name="service" value="${serviceConfiguration.service}"/>
                    </spring:url>
                    <a href="${serviceConfigurationURL}"> ${sconfig.service}  - <spring:message code="action.edit"/> Config.</a>                    
                </div>                                                                                           
        </div>
    </jstl:forEach>
</div>

</div>-->
