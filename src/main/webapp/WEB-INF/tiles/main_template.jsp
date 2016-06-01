<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>        
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <!-- TITLE & ICON -->
        <title>${studioConfiguration.workbenchName} | app</title>
        <link rel="shortcut icon" href="favicon.ico"/> 
        <!-- Other meta-information -->
        <meta name="description" content="">
        <base	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">        
        <!-- STYLES        -->
        <!-- Bootstrap css -->
        <link rel="stylesheet" href="css/bootstrap.css">
        <!-- Other css styles of our components -->
        <link rel="stylesheet" href="css/jmenu.css" media="screen">
        <link rel="stylesheet" href="css/passfield.min.css" type="text/css">        
        <link rel="stylesheet" href="css/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="css/social-buttons.css">
        <!-- Custom css styles -->
        <link rel="stylesheet" href="css/e3.css">
        
        
        <!-- Initialization scripts -->
        <script src="js/vendor/modernizr-2.6.2.min.js"></script>                                    
        <script type="text/javascript" src="js/vendor/jquery.js"></script>
        <script type="text/javascript" src="js/vendor/jquery-ui.js"></script>        
        <script type="text/javascript" src="js/vendor/bootstrap.js"></script>
    </head>

    <body id="top">
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        
        
        <div id="mainContainer" class="container">
            <tiles:insertAttribute name="header" />
            <tiles:insertAttribute name="menu" />    
            <div id="content" class="row-fluid">		
                <tiles:insertAttribute name="body" />	
                <jstl:if test="${message != null || messageCode != null}">			
                    <span class='message 
                          <jstl:if test="${message_type!=null}">
                            <spring:message code="${message_type}"/>  
                          </jstl:if>
                          '
                          >
                        <jstl:if test="${messageCode!=null}">
                            <spring:message code="${messageCode}" />
                        </jstl:if>
                        <jstl:if test="${message!=null}">
                             ${message}
                        </jstl:if>
                    </span>
                        
                </jstl:if>	
            </div>	
            <tiles:insertAttribute name="footer" />	
        </div>
        
        <!-- We load the rest  of the scripts at the end to improve the page load -->
        
        
                        
        <!--<script type="text/javascript" src="js/vendor/jmenu.js"></script>  -->      
        <!-- Custom --->
        <script type="text/javascript" src="js/plugins.js"></script>
        <script type="text/javascript" src="js/main.js"></script>
        
        <!-- Google Analytics. -->
        <script>
            var _gaq=[['_setAccount','UA-XXXXX-X'],['_trackPageview']];
            (function(d,t){var g=d.createElement(t),s=d.getElementsByTagName(t)[0];
            g.src=('https:'==location.protocol?'//ssl':'//www')+'.google-analytics.com/ga.js';
            s.parentNode.insertBefore(g,s)}(document,'script'));
        </script>
    </body>
</html>