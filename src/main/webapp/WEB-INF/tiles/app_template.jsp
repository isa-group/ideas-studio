<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%-- <%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%> --%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>        
        <title>${studioConfiguration.workbenchName} | editor</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <!-- TITLE & ICON -->
        <title><tiles:insertAttribute name="title" ignore="true" /></title>
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
        <link rel="stylesheet" href="css/jquery-ui.css">
        <link rel="stylesheet" href="css/jquery.fileupload.css">
        <link rel="stylesheet" href="css/jquery.fileupload-ui.css">
        <link rel="stylesheet" href="css/displaytag.css" type="text/css">
        <link rel="stylesheet" href="css/passfield.min.css" type="text/css">
        <!-- Custom css styles -->
        <link href="css/jquery.contextMenu.css" rel="stylesheet" type="text/css" >
        <link rel='stylesheet' type='text/css' href='css/dyntree/skin/exemplar-dynatree.css'>
        <link rel="stylesheet" href="css/ideas.css">
        
        <!-- Initialization scripts -->
        <script src="js/vendor/modernizr-2.6.2.min.js"></script>                                    
        <script type="text/javascript" src="js/vendor/jquery.js"></script>
        <script type="text/javascript" src="js/vendor/jquery-ui.js"></script>     
        <script type="text/javascript" src="js/vendor/jquery.ui.resizable.js"></script> 
        <script src='js/vendor/jquery.cookie.js' type="text/javascript"></script>
        <script src='js/dyntree/jquery.dynatree.js' type="text/javascript"></script> 
       <script src="js/vendor/jquery.ui.widget.js"></script>        
        <script src="js/vendor/jquery.iframe-transport.js"></script><!-- Iframe Transport is required for file AJAX upload 
                                                             in browsers without support for XHR file uploads -->
        <script src="js/vendor/jquery.fileupload.js"></script>

        <script src="js/vendor/jquery.contextMenu-custom.js" type="text/javascript"></script>
        
        <script src="js/ace/ace.js" type="text/javascript" charset="utf-8"></script>
      
        <script type="text/javascript" src="js/vendor/bootstrap.js"></script>
        <script type="text/javascript" src="js/md5.js"></script>
        
        <script type="text/javascript" src="js/requestHelper.js"></script>
        <script src='js/contextAction.js' type="text/javascript"></script> 
        
        <script type="text/javascript" src="js/fileApi.js"></script>
        <script src="js/commandApi.js" type="text/javascript" charset="utf-8"></script>
        <script type="text/javascript" src="js/plugins.js"></script>
        <script type="text/javascript" src="js/main.js"></script>
        <script src='js/workspaceManager.js' type="text/javascript"></script>
        <script type="text/javascript" src="js/appPresenter.js"></script>
        <script type="text/javascript" src="js/app.js"></script>
                
    </head>

    <body id="appTemplateFullBody">
    	<div id="appLoaderBlocker"></div>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        <div id="appWrapper">
		<security:authorize access="hasRole('ADMIN')||hasRole('RESEARCHER')">
	        <div id="appLeftMenu" class="menuClose">
	        	<tiles:insertAttribute name="left_menu" />
	        </div>
                </security:authorize>
	        <div id="appMainContent">
	        	<div id="appMainContentBlocker" class="hidden"></div>
		        <tiles:insertAttribute name="header" />
		        <div id="appBody">
		        	<div id="appBodyBlocker"></div>
		        	<div id="appBodyLoader">
		        		<tiles:insertAttribute name="body"/>
		        	</div>
		        </div>
		        <tiles:insertAttribute name="footer" />	
	       	</div>
	    </div>
        
        <!-- Google Analytics. -->
        <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

        ga('create', '${studioConfiguration.googleAnalyticsID}', 'auto');
        ga('send', 'pageview');                    

        </script>
    </body>
</html>