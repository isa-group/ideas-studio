<%@tag description="Cabecera de las páginas de IDEAS" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<header id="header">
    <div id="headerDiv" class="row-fluid">
        <!--<h1><a href="index.jsp">STATService</a></h1>-->
        <div id="logo" class="span4">
            <a href="http://moses.us.es/e3"><img src="./img/E3-Logo.png" alt="e3" /></a>
            <div id="moses-logo">
                <spring:message code="master.page.is-part-of" /> 
                <a href="http://moses.us.es"><img src="./img/MOSES-Logo-verySMALL.png" alt="MOSES"/></a>    
            </div>
        </div>        

        <div id="logosRG" class="span4">
            <a href="http://www.us.es"><img src="./img/USLogo.png" alt="University of Sevilla"/></a>
            <!--<a href="http://www.lsi.us.es"><img src="./imgs/LSI.gif" alt="Department of Computing Languages and Systems"/></a>-->
            <a href="http://www.isa.us.es"  target="_blank" ><img src="./img/ISALogo.png" alt="Applied Software Engineering Research Group"/></a>            
        </div>


        <div id="login" class="pull-right span3">

            <security:authorize access="isAnonymous()">
                <a class="btn" href="security/login">
                    <i class="icon-user"></i> 
                    <spring:message code="master.page.login" />
                </a>
<!--                <form id="tw_signin" action="signin/twitter" method="POST">
                    <button type="submit">
                        <img src="img/Facebook.png"/>
                    </button>
                </form>-->
<!--                <form id="fb_signin" action="signin/facebook" method="POST">
                    <button type="submit">
                         <img src="img/sign-in-with-facebook.png" />
                    </button>
                </form>-->
                <a class="btn btn-success" href="researcher/create">
                    <spring:message code="master.page.register" />
                </a>
            </security:authorize>                        
            <security:authorize access="isAuthenticated()">
                <i class="icon-user"></i>
                <a href="researcher/edit"> 
                    <security:authentication property="principal.username" />                                             
                </a>
                <a href="j_spring_security_logout" class="btn-small btn-danger">
                    <i class="icon-remove-circle"></i>
                    <spring:message code="master.page.logout" /> 
                </a>
                <!--<div id="facebook-conectivity">
                    <a href='<c:url value="/connect/facebook" />'>Connect to Facebook</a>
                </div>
                <div id="twitter-contectivity">
                    <a href='<c:url value="/connect/twitter" />'>Connect to Twitter</a>
                </div>                
                <div id="linkedin-conectivity">
                    <a href='<c:url value="/connect/linkedin" />'>Connect to Linkedin</a>
                </div>-->
            </security:authorize>        
        </div>
        <div id="language" class="pull-right span3">
            <a href="?language=en">en</a> | <a href="?language=es">es</a>
        </div>    
    </div>
</header>