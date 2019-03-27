<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
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
        <title>${studioConfiguration.workbenchName} | login</title>
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
        <script type="text/javascript" src="js/wizardViewManager.js"></script>

        <!--script>

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

                                $('#loginContent').addClass("loginError");

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

            jQuery(function () {
                $('.btn-facebook').click(function () {
                    localStorage.setItem('social', 'facebook');
                });
                $('.btn-twitter').click(function () {
                    localStorage.setItem('social', 'twitter');
                });
                $('.btn-google-plus').click(function () {
                    localStorage.setItem('social', 'google');
                });
                $('#loginButton').click(function () {
                    localStorage.setItem('social', '');
                });
            });
        </script -->
    </head>

    <body id="loginView">
        <div id="loginLogo" style="background-image: url('./img/${studioConfiguration.images['logo']}')">            
        </div>
        <div id="lcWrapper">
            <div id="loginContent">            
                    <jsp:doBody/>                                                
            </div>

        </div>
        <div id="loginCopyright">
            <spring:message code="app.footer.copyright" />
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

    </body>
</html>
