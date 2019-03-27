<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>

<ideas:login-template>                
    <div id="loginLoader">
        <form:form action="/security/login" modelAttribute="credentials" id="loginForm" method="post">
            <div class="control-group">                            
                <div class="input-group">
                    <span class="input-group-addon"><spring:message
                            code="security.username" /></span>
                        <form:input path="username" class="form-control" />
                </div>
                <form:errors class="error" path="username" />
            </div>

            <div class="control-group">                            
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
            <div id="loginButtons">
                <button type="submit" class="btn btn-primary" id="loginButton" form="loginForm"">
                    <spring:message code="security.login" />
                    <span class="glyphicon glyphicon-log-in"></span>
                </button>
            </div>            
        </form:form>

    </div>
    

    <div id="socialSigninButtons" class="form-inline">
        <span><spring:message code="security.login.alternative" />:</span>
        <!--<form id="fa_signin" action="signin/facebook" method="POST"
                class="form-horizontal">
                <input type="hidden" name="scope" value="email">
                <button type="submit" class="btn btn-social btn-xs btn-facebook"> 
                        <i class="fa fa-facebook"></i> Facebook 
                </button> 
        </form>-->
        <form id="tw_signin" action="signin/twitter" method="POST"
              class="form-horizontal">                                        
            <button type="submit" class="btn btn-social btn-xs btn-twitter">
                <i class="fab fa-twitter"></i> Twitter
            </button>
        </form>
        <form id="go_signin" action="signin/google" method="POST"
              class="form-inline">
            <input type="hidden" name="scope" value="https://www.googleapis.com/auth/userinfo.email">					
            <button type="submit" class="btn btn-social btn-xs btn-google-plus"
                    href="/signin/google">
                <i class="fab fa-google"></i> Google
            </button>
        </form>
    </div>                


    <script>
                var originalRequestUrl = "${originalRequestUrl}";
    </script>
    <!-- Google Analytics. -->
    <script>
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r;
            i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
            a = s.createElement(o),
                    m = s.getElementsByTagName(o)[0];
            a.async = 1;
            a.src = g;
            m.parentNode.insertBefore(a, m)
        })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

        ga('create', '${studioConfiguration.googleAnalyticsID}', 'auto');
        ga('send', 'pageview');
    </script>



    <script>

        //DEMO USERS
        $("#username").ready(function () {

            var href = location.href;
            var isWizard = href.match(WizardViewManager.REG_EXP, "$1") !== null;

            if ($("#username").val() != "") { // This is a demo

                var ws = href.split("demo/")[1]
                        .replace(WizardViewManager.REG_EXP, "$1");

                $("#loginForm div.control-group").hide();
                $('#dontRememberLogin').hide();
                $('#loginButtons').hide();
                $('#socialSigninButtons').hide();
                $('#loginContent').height('auto')
                        .append('<p style="text-align:center;">Loading ${studioConfiguration.workbenchName} workbench with demo user...</p>');

                $("#password").val($("#username").val());
                //$("#loginForm").submit();
                console.log(location.href);
                localStorage.setItem('demo', 'demo');
                localStorage.setItem('ws', ws);
                console.log(">>>>>>>>>>>>>>" + ws);
                //$("#loginButton").click();
                $("#loginForm").submit();

            } else {

                localStorage.setItem('demo', '');
                localStorage.setItem('ws', '');

            }

            WizardViewManager.save(isWizard);

        });

    </script>       
</ideas:login-template>