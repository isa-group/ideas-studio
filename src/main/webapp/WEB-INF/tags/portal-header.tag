<%@tag description="put the tag description here" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	jQuery(function() {
		AppPresenter.loadUserData(function() {
		});
	});
</script>


<!-- Fixed navbar -->
<div id="navigation" class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">                    
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>                    
            </button>
            <a class="navbar-brand" href="#" style="padding-top: 0px;">               
                <div id="appLogo" class="navbar-brand" style="background-image: url('./img/${studioConfiguration.images['logo']}');position:inherit;">                                                
                </div>
            </a>
                <security:authorize access="isAuthenticated()">
                <div id="pagesHeaderUserTab" style="width:auto;position:inherit;">
                    <div class="userInfo" id="principalUserInfo">
                        <span></span> <BR /> <span class="userAuths"
                                                   id="principalUserAuths"></span>
                    </div>
                    <div class="userAvatar" id="principalUserAvatar"></div>
                </div>
            </security:authorize>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#home" class="smothscroll">Welcome</a></li>
                <li><a href="#desc" class="smothscroll">Why DESIGNER?</a></li>
                <li><a href="#showcase" class="smothScroll">Showcase</a></li>
                <li><a href="app/editor" class="smothScroll">Use it NOW!</a></li>
                <li><a href="http://www.isa.us.es/publications?lastname=All&title=EXEMPLAR&biblio_year=&tid=All&field_reseracharea_nid=All" class="smothScroll">How to cite</a></li>                
            </ul>            
        </div><!--/.nav-collapse -->                     
    </div>                
</div>