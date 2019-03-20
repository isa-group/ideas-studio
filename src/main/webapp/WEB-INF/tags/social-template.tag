<%@tag description="Social pages template" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <!-- TITLE & ICON -->
        <title>${studioConfiguration.workbenchName} | app</title>
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
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
        <link rel="stylesheet" href="css/social-buttons.css">
        <!-- Custom css styles -->
        <link rel="stylesheet" href="css/ideas.css">

        <!-- Initialization scripts -->
        <script type="text/javascript" src="js/vendor/jquery.js"></script>
        <script type="text/javascript" src="js/vendor/jquery-ui.js"></script>
        <script src='js/vendor/jquery.cookie.js' type="text/javascript"></script>
        <script type="text/javascript" src="js/vendor/bootstrap.js"></script>

        <script>
            var setupLogin = function () {

                $('#loginForm').submit(function () {

                    $.ajax({
                        type: $('#loginForm').attr('method'),
                        url: $('#loginForm').attr('action'),
                        data: $('#loginForm').serialize(),
                        success: function (result, textStatus, request) {

                            console.log("-" + result + "-");

                            $('#loginLoader').empty();
                            $('#loginLoader').css("visibility", "hidden");
                            $('#loginLoader').append(result);

                            if ($("#loginLoader .formError").length > 0) {

                                $('#loginLoader').css("visibility", "visible");

                                $('#socialTemplateContent').addClass("loginError");

                                $('#lcWrapper').effect("shake", {
                                    direction: "left",
                                    times: 2,
                                    distance: 30
                                });
                                setupLogin();
                            } else {
                                window.location = originalRequestUrl;
                            }

                        }
                    });
                    return false;
                });

            };

            $(document).ready(function () {
                //sumit types
                $('body').keypress(function (e) {
                    var code = e.which;
                    if (code == 13) {
                        localStorage.setItem('social', '');
                        $('#loginForm').submit();
                    }
                });

                $('#loginButton').click(function () {
                    $('#loginForm').submit();
                });
                setupLogin();
            });
        </script>
    </head>

    <body id="loginView">
        <div id="loginLogo" style="background-image: url('./img/${studioConfiguration.images['logo']}')">            
        </div>
        <div id="lcWrapper">
            <div id="socialTemplateContent">
                <div id="loginLoader">
                    <jsp:doBody />
                </div>
                <div id="socialTemplateButtons">
                </div>
            </div>
        </div>
        <div id="loginCopyright">
            <spring:message code="app.footer.copyright" />
        </div>
    </body>
</html>
