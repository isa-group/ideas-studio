<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	jQuery(function() {
		AppPresenter.loadUserData(function() {
		});
	});
</script>

<div id="pagesHeader">
	<div id="pagesHeaderLogo">
		<security:authorize access="isAuthenticated()">
			<div id="pagesHeaderUserTab">
				<div class="userInfo" id="principalUserInfo">
					<span></span> <BR /> <span class="userAuths"
						id="principalUserAuths"></span>
				</div>
				<div class="userAvatar" id="principalUserAvatar"></div>
			</div>
		</security:authorize>
	</div>
</div>