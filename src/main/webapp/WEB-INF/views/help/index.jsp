<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${studioConfiguration.workbenchName} | editor</title>

    <!-- Bootstrap Core CSS -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <style>
	.logo {
		margin: 70px;
	}
	a:hover {
		text-decoration: none;
	}
    </style>

</head>

<body>

    <!-- Page Content -->
    <div class="container">
        <center>
            <img class="logo" src="../../img/${studioConfiguration.images.logo}">
        </center>
        <ul class="list-unstyled">
            <li>
                <h2><a href="https://labs.isa.us.es/IDEAS-help/intro.html">Introduction</a></h2>
            </li>
            <li>
                <h2>Languages</h2>
                <ul class="help-links">

                    <core:forEach var="language" items="${studioConfiguration.modules}">
                        <li>
                            <core:set var="languageName" value="${language.key}"/>
                            <core:set var="languageNameAux" value="${fn:replace(language.key, '-language','')}"/>
                            <core:set var="languageName" value="${fn:replace(languageNameAux, 'ideas-','')}"/>
                            <core:set var="languageNameFinal" value="${fn:toUpperCase(languageName)}" />                      
                            <h3><a href="${language.value}/help/">${languageNameFinal}</a></h3>
                        </li>

                    </core:forEach>  

                </ul>
            </li>       
        </ul>
    </div>
    <!-- /.container -->
</body>
</html>
		
</div>