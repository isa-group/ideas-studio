<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>



<div id="userAccount" class="">    
    <form:form id="editAccountForm" action="security/useraccount/edit" modelAttribute="account">                
        <h3><spring:message code="userAccount.editHeader"/></h3>
        <form:hidden path="id" />
        <form:hidden path="version" />
        <div id="Authorities">
        <jstl:forEach items="${account.authorities}" var="authority" varStatus="status">                                
            <!--<input type="hidden" name="authorities[${status.index}].authority" value="${authority.authority}"/>                    -->
            <form:hidden path="authorities[${status.index}]"/>
        </jstl:forEach>
        </div>

        <div class="control-group">
            <form:label path="username" cssClass="control-label">
                <spring:message code="security.username" />
            </form:label>
            <form:input path="username"/>	
            <form:errors class="alert-error" path="username" />
        </div>

        <div class="control-group">
            <label for="oldPass" class="control-label"><spring:message code="security.oldpassword" /></label>
            <input type="password" id="oldPass" name="oldPass">	            
        </div>
         
        <div class="control-group">
            <form:label cssClass="control-label" path="password">
                <spring:message code="security.newpassword" />
            </form:label>
            <form:password id="mypass" path="password"/>            
            <form:errors class="alert-error" path="password" />
        </div>
        
        <div class="control-group">
            <label class="control-label" for="repeatPass"><spring:message code="security.repeatpassword" /></label>
            <input type="password" id="repeatPass" name="repeatPass">
            <div id="password-repeat-errors" class="hide alert-error">
                <spring:message code="security.differentpasswords.error"/>
            </div>
        </div>

<!--         <div class="form-actions"> -->
<%--             <input id="submit" type="submit" value="<spring:message code="action.save" />" class="btn"/> --%>
<!--         </div>                       -->
    </form:form>
</div>
<script src="js/vendor/passfield.min.js"></script>
<script>        
    $("#mypass").passField(    { /*options*/ }  );
    
    $("#password-repeat-errors").hide();
    
    jQuery(function(){
        $("#submit").click(function(){
            $("#password-repeat-errors").hide();
            var hasError = false;
            var passwordVal = $("#mypass").val();
            var checkVal = $("#repeatPass").val();
            if (passwordVal != checkVal ) {
                $("#password-repeat-errors").show();
                hasError = true;
            }
            if(hasError == true) {return false;}
        });
    });

</script>