<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"  import="java.net.*, java.io.*,java.util.Properties"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
    <!--<![endif]-->
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <!-- TITLE & ICON -->
        <title><tiles:insertAttribute name="title" ignore="true" /></title>
        <link rel="shortcut icon" href="favicon.ico" />
        <!-- Other meta-information -->
        <meta name="description" content="">
        <base
            href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- STYLES        -->
        <!-- Bootstrap css -->
        <link rel="stylesheet" href="css/bootstrap.css">
        <!-- Other css styles of our components -->
        <link rel="stylesheet" href="css/jquery-ui.css">
        <link rel="stylesheet" href="css/jmenu.css" media="screen">
        <link rel="stylesheet" href="css/passfield.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome/css/font-awesome.css">
        <link rel="stylesheet" href="css/social-buttons.css">
        <!-- Custom css styles -->
        <link rel="stylesheet" href="css/ideas.css">

        <!-- Initialization scripts -->
        <script type="text/javascript" src="js/vendor/jquery.js"></script>
        <script type="text/javascript" src="js/vendor/jquery-ui.js"></script>
        <script src='js/vendor/jquery.cookie.js' type="text/javascript"></script>
        <script type="text/javascript" src="js/vendor/bootstrap.js"></script>

    </head>

    <body id="panicView">
        <div id="loginLogo" style="background-image: url('./img/${studioConfiguration.images['logo']}')">            
        </div>
        <div id="lcWrapper">
            <div id="panicTemplateContent">
                <div id="loginLoader">
                    <tiles:insertAttribute name="body" />
                </div>

            </div>

            <div id="loginCopyright2">
                <spring:message code="app.footer.copyright" />
            </div>
        </div>
    </body>
</html>



