<%@tag description="Layout for the app" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <title>${studioConfiguration.workbenchName} | app</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <!-- TITLE & ICON -->
        <link rel="shortcut icon" href="favicon.ico"/>
        <!-- Other meta-information -->
        <meta name="description" content="">
        <base	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- STYLES        -->
        <!-- Bootstrap css -->
        <link rel="stylesheet" href="css/bootstrap.css">
        <link rel="stylesheet" href="css/toggle-button.css">
        <!-- Other css styles of our components -->
        <!-- link rel="stylesheet" href="css/jmenu.css" media="screen" -->
        <link rel="stylesheet" href="css/jquery-ui.css">
        <link rel="stylesheet" href="css/jquery.fileupload.css">
        <link rel="stylesheet" href="css/jquery.fileupload-ui.css">        
        <link rel="stylesheet" href="css/passfield.min.css" type="text/css">
        <link rel="stylesheet" href="css/textAngular.css" type="text/css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
        <!-- Custom css styles -->
        <link href="css/jquery.contextMenu.css" rel="stylesheet" type="text/css" >
        <link rel='stylesheet' type='text/css' href='css/dyntree/skin/exemplar-dynatree.css'>
        <link rel="stylesheet" href="css/ideas.css">
        <link rel="stylesheet" href="css/vendor/bootstrap-select.min.css">

        <!-- Initialization scripts -->
        <script src="js/vendor/modernizr-2.6.2.min.js"></script>
        <script type="text/javascript" src="js/vendor/jquery.js"></script>
        <script type="text/javascript" src="js/vendor/bootstrap.js"></script>
        <script type="text/javascript" src="js/vendor/bootstrap-select.min.js"></script>
        <script type="text/javascript" src="js/vendor/jquery-ui.js"></script>
        <script type="text/javascript" src="js/vendor/jquery.ui.resizable.js"></script>
        <script src='js/vendor/jquery.cookie.js' type="text/javascript"></script>
        <script src='js/dyntree/jquery.dynatree.js' type="text/javascript"></script>
        <script src="js/vendor/jquery.ui.widget.js"></script>
        <script src="js/vendor/jquery.iframe-transport.js"></script><!-- Iframe Transport is required for file AJAX upload
                                                             in browsers without support for XHR file uploads -->
        <script src="js/vendor/jquery.fileupload.js"></script>

        <script src="js/vendor/jquery.contextMenu.js" type="text/javascript"></script>

        <script src="js/ace/ace.js" type="text/javascript" charset="utf-8"></script>
        <script src="js/ace-build/ext-language_tools.js" type="text/javascript" charset="utf-8"></script>
        <script src="js/ace-build/ext-searchbox.js" type="text/javascript" charset="utf-8"></script>

        <script type="text/javascript" src="js/md5.js"></script>

        <script type="text/javascript" src="js/requestHelper.js"></script>
        <script src='js/contextAction.js' type="text/javascript"></script>

        <script type="text/javascript" src="js/fileApi.js"></script>
        <script src="js/commandApi.js" type="text/javascript" charset="utf-8"></script>
        <script type="text/javascript" src="js/plugins.js"></script>
        <script type="text/javascript" src="js/main.js"></script>
        <script src='js/workspaceManager.js' type="text/javascript"></script>
        <script src='js/vendor/isotope.pkgd.js' type="text/javascript"></script>
        <script src='js/vendor/html2canvas.js' type="text/javascript"></script>
        <script src='js/vendor/canvas-toBlob.js' type="text/javascript"></script>
        <script src='js/vendor/FileSaver.min.js' type="text/javascript"></script>
        <script type="text/javascript" src="js/appPresenter.js"></script>
        <script type="text/javascript" src="js/app.js"></script>
        <script type="text/javascript" src="js/wizardViewManager.js"></script> <!-- for editor initialization -->
        <script type="text/javascript" src="js/vendor/jsep.min.js"></script>
        <script type="text/javascript" src="js/vendor/mustache.min.js"></script>

        <script type="text/javascript" src="js/vendor/js-yaml.min.js"></script> <!-- yaml parser -->

        <script type="text/javascript" src="js/vendor/angular.min.js"></script>

        <!-- angular-chart -->
        <script src="js/vendor/Chart.min.js"></script>
        <script src="js/vendor/angular-chart.min.js"></script>
        <script src="js/vendor/chartjs-plugin-annotation.min.js"></script>
        <script src="js/vendor/chartjs-plugin-draggable.min.js"></script>
        <script src="js/vendor/gaussian.js"></script>

        <!-- angular dependencies -->
        <script src='js/vendor/angular-ui-router.min.js' type="text/javascript"></script>
        <script type="text/javascript" src="js/vendor/angular-sanitize.min.js"></script>
        <script src='js/vendor/datetimepicker.js' type="text/javascript"></script>
        <script src='js/vendor/datetimepicker.templates.js' type="text/javascript"></script>
        <script src='js/vendor/dateTimeInput.js' type="text/javascript"></script>
        <script src='js/vendor/ng-google-chart.min.js' type="text/javascript"></script>

        <!-- TexAngular editor -->
        <script src='js/vendor/textAngular-rangy.min.js'></script>    
        <script src='js/vendor/textAngular-sanitize.min.js'></script>    
        <script src='js/vendor/textAngular.min.js'></script>

        <!-- angular app -->
        <script type="text/javascript" src="js/angular/app.js"></script>
        <script type="text/javascript" src="js/angular/directives/ppinotDirective.js"></script>
        <script type="text/javascript" src="js/angular/directives/existingfileDirective.js"></script>
        <script type="text/javascript" src="js/angular/directives/gravatarDirective.js"></script>
        <script type="text/javascript" src="js/angular/filters/unquoteFilter.js"></script>

        <!-- HighCharts -->
        <script src="js/vendor/highcharts/highcharts.js"></script>
        <script src="js/vendor/highcharts/highcharts-more.js"></script>
        <script src="js/vendor/highcharts/highcharts-3d.js"></script>
        <script src="js/vendor/highcharts/modules/data.js"></script>
        <script src="js/vendor/highcharts/modules/exporting.js"></script>
        <script src="js/vendor/highcharts/modules/export-data.js"></script>
        <script src="js/vendor/highcharts/modules/funnel.js"></script>
        <script src="js/vendor/highcharts/modules/solid-gauge.js"></script>

    </head>

    <body id="appTemplateFullBody">
        <div id="appLoaderBlocker"></div>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        <div id="appWrapper">
            <security:authorize access="hasAuthority('ADMIN') || hasAuthority('RESEARCHER')">
                <div id="appLeftMenu" class="menuClose">
                    <ideas:app-left-menu />
                </div>
            </security:authorize>
            <div id="appMainContent">
                <div id="appMainContentBlocker" class="hidden"></div>
                <ideas:app-header />
                <div id="appBody" ng-app="mainApp" ng-controller="MainCtrl">
                    <input id="editorContent" style="display:block;" type="text" ng-change="editorContentToModel()" ng-model="modelString" />
                    <input id="compileModel" style="display:none;" type="text" ng-change="compileModel()" ng-model="compilationFlag" />
                    <input id="compileModelFormatView" style="display:none;" type="text" ng-change="compileModelFormatView()" ng-model="compilationFlagFormatView" />
                    <div id="appBodyBlocker"></div>
                    <div id="appBodyLoader">
                        <jsp:doBody/>
                    </div>
                </div>
                <ideas:app-footer/>
            </div>
        </div>

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
    </body>
</html>