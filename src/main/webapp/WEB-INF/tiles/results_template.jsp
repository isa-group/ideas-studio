<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<title>${studioConfiguration.workbenchName}</title>
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
<link rel="stylesheet" href="css/jmenu.css" media="screen">
<link rel="stylesheet" href="css/displaytag.css" type="text/css">
<link rel="stylesheet" href="css/passfield.min.css" type="text/css">
<link rel="stylesheet" type="text/css" href="css/gcli-console.css" media="screen" />

<!-- Custom css styles -->
<link rel="stylesheet" href="css/ideas.css">


<!-- Initialization scripts -->
<script src="js/vendor/modernizr-2.6.2.min.js"></script>
<script type="text/javascript" src="js/vendor/jquery.js"></script>
<script type="text/javascript" src="js/vendor/jquery-ui.js"></script>
<script type="text/javascript" src="js/vendor/bootstrap.js"></script>
<script type="text/javascript" src="js/md5.js"></script>
<script type="text/javascript" src="js/vendor/jquery.js"></script>
<script type="text/javascript" src='js/languageModeManager.js' ></script>
<script type="text/javascript" src='js/editorManager.js'></script>
<script type="text/javascript" src='js/editor.js'></script>
<script type="text/javascript" src='js/commandApi.js'></script>
<script type="text/javascript" src='js/studioSetup.js'></script>
<script type="text/javascript" src="js/requestHelper.js"></script>
<!--<script type="text/javascript" src='js/operationsReport.js' ></script>-->
<script type="text/javascript" src="js/gcli-console/commands.js"></script>
<script  type="text/javascript" src="js/gcli-console/gcli-uncompressed.js"></script>
<script type="text/javascript" src="js/appPresenter.js"></script>

<script>
	var fitBottom = function() {
		var pagesContainer = $('#pagesContent');
		var hh = $(window).height() - pagesContainer.position().top - 110;
		$('#pagesContent').css("min-height", hh + "px");
	};

	jQuery(function() {

		fitBottom();

		$(window).resize(function() {
			fitBottom();
		});
	});
</script>

</head>

  <body id="appTemplateFullBody">
    	<div id="appLoaderBlocker"></div>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        <div id="appWrapper">
		
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
