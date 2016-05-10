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

<script>
    $(document).ready(function () {
        var isChrome = !!window.chrome && !!window.chrome.webstore;
        if (!isChrome) {
            $("#loginButtons").hide();
            $("#loginLoader").hide();
            $("#socialSigninButtons").hide();
            $("#otherBrowserWrapper").show();
            $("#loginContent").css("height", "auto");
        }
    });
</script>

<script>
	var setupLogin = function() {
		
		$('#loginForm').submit(function() {

			$.ajax({
				type : $('#loginForm').attr('method'),
				url : $('#loginForm').attr('action'),
				data : $('#loginForm').serialize(),
				success : function(result, textStatus, request) {

					console.log("-" + result + "-");

					$('#loginLoader').empty();
					$('#loginLoader').css("visibility", "hidden");
					$('#loginLoader').append(result);

					if ($("#loginLoader .formError").length > 0) {

						$('#loginLoader').css("visibility", "visible");

						$('#loginContent').addClass("loginError");
						
						$('#lcWrapper').effect("shake", {
							direction : "left",
							times : 2,
							distance : 30
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
	
	$( document ).ready(function() {		
		//sumit types
		$('body').keypress(function(e) {
		    var code = e.which;
		     if(code == 13) { 
		    	 localStorage.setItem('social', '');
		    	 $('#loginForm').submit();
		     }
		});
		
		$('#loginButton').click(function() {
			$('#loginForm').submit();
		});
		setupLogin();
	});

	jQuery(function() {
		$('.btn-facebook').click(function(){
			localStorage.setItem('social', 'facebook');
		});
		$('.btn-twitter').click(function(){
			localStorage.setItem('social', 'twitter');
		});
		$('.btn-google-plus').click(function(){
			localStorage.setItem('social', 'google');
		});
		$('#loginButton').click(function(){
			localStorage.setItem('social', '');
		});
	});
</script>
</head>

<body id="loginView">
    <div id="loginLogo" style="background-image: url('./img/${studioConfiguration.images['logo']}')">            
        </div>
	<div id="lcWrapper">
		<div id="loginContent">
            <div id="otherBrowserWrapper" style="display:none;">
                <span>Sorry, IDEAS is not supported by your browser.</span>
                <br>
                <span>Please, <a href="https://www.google.es/chrome/browser/desktop/">download Google Chrome</a> latest version or use our <a href="http://labs.isa.us.es/IDEAS/designer-chrome.exe">embedded application</a> for Windows to access IDEAS.</span>
                <br>
                <br>
                <span>Thank you.</span>
            </div>
			<div id="loginLoader">
				<tiles:insertAttribute name="body" />
			</div>
			<div id="loginButtons">
				<div class="btn btn-primary" id="loginButton">
					<spring:message code="security.login" />
					<span class="glyphicon glyphicon-log-in"></span>
				</div>

			</div>
			
			<div id="socialSigninButtons" class="form-inline">
				<span>Or Sign in with:</span>
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
 						<i class="fa fa-twitter"></i> Twitter
					</button>
				</form>
				<form id="go_signin" action="signin/google" method="POST"
					class="form-inline">
					<input type="hidden" name="scope" value="https://www.googleapis.com/auth/userinfo.email">					
					<button type="submit" class="btn btn-social btn-xs btn-google-plus"
						href="/signin/google">
						<i class="fa fa-google-plus"></i> Google
					</button>
				</form>
			</div>
		</div>

		<div id="loginShadow"></div>
	</div>
	<div id="loginCopyright">
		<spring:message code="app.footer.copyright" />
	</div>

	<script>
		var originalRequestUrl = "${originalRequestUrl}";
		<%
		   URL url = application.getResource("/WEB-INF/titles.properties");
		   InputStream in = url.openStream();
		   Properties p = new Properties();  
		   p.load(in);
		   out.println(p.getProperty("login-template.socialSigninButtons"));
		   if(p.getProperty("login-template.socialSigninButtons").equals("false")){%>
		   $("#socialSigninButtons").remove();
		<%   }%>
	</script>
	    <!-- Google Analytics. -->
        <script>
            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

            ga('create', '${studioConfiguration.googleAnalyticsID}', 'auto');
            ga('send', 'pageview');                    
        </script>
        
        
        
        <script>
            //DEMO USERS
            $("#username").ready(function(){
                var isChrome = !!window.chrome && !!window.chrome.webstore;
                if (isChrome) {
                    if ($("#username").val()!="") {
                        $("#password").val($("#username").val());
                        //$("#loginForm").submit();
                        console.log(location.href);
                        localStorage.setItem('demo', 'demo');
                        localStorage.setItem('ws', location.href.split("demo/")[1]);
                        console.log(">>>>>>>>>>>>>>"+location.href.split("demo/")[1]);
                        //$("#loginButton").click();
                        $("#loginForm").submit();
                    } else {
                        localStorage.setItem('demo', '');
                        localStorage.setItem('ws', '');
                    }
                }
            });
		</script>
	
</body>
</html>



