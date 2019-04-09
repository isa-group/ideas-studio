<%@tag description="Left menu for the ideas app" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>

    jQuery(function () {

        $('.apl_social').click(function () {
            AppPresenter.loadSection("social");
        });
        $('.apl_settings').click(function () {
            AppPresenter.loadSection("settings");
        });
        $('.apl_administration').click(function () {
            AppPresenter.loadSection("administration");
        });
        $('.apl_help').click(function () {
            AppPresenter.loadSection("help");
        });

        // Workspaces on scroll:

        var changeShadowsOnScroll = function (scroll) {
            var first = $('#workspacesNavContainerWrapper').first();
            var limit = 10;

            if (first != null) {
                var scrollTopVal = first.scrollTop();
                var hdif = $('#workspacesNavContainer').height() - $('#workspacesNavContainerWrapper').height();

// 				console.log("> " + hdif+ " , " + scrollTopVal + " -> " + (hdif-scrollTopVal));


                var opacity1 = Math.max(0, Math.min(1, (scrollTopVal / limit)));
                var opacity2 = Math.max(0, Math.min(1, ((hdif - scrollTopVal) / limit)));
                $('#appNavigatorInnerWrapper .shadowCurvedBottom1').fadeTo(0, opacity1);
                $('#appNavigatorInnerWrapper .shadowCurvedTop1').fadeTo(0, opacity2);
            }
        };

        $('#workspacesNavContainerWrapper').on('scroll', function (scroll) {
            changeShadowsOnScroll(scroll);
        });

        $('#workspacesNavContainer').bind("DOMSubtreeModified", function (scroll) {
            changeShadowsOnScroll(scroll);
        });

        $(window).on("resize", function (scroll) {
            changeShadowsOnScroll(scroll);
        });

        changeShadowsOnScroll();

    });
</script>

<div id="appLeftMenuContent">

    <div id="appLeftMenuContentHeader">
        <c:if test="${studioConfiguration.advancedMode}">
            <span class="wsm-icon glyphicon glyphicon-th-large blue"></span>
            <a class="apl_wsm" href="app/wsm"><spring:message code="app.leftmenu.wsm"/></a>
        </c:if>
        <div class="addWorkspace btn btn-primary float-right">+</div> 
    </div>
    <div id="appNavigator">

        <div id="appNavigatorInnerWrapper">
            <div class="header"></div>
            <div class="shadowCurvedBottom1"></div>
            <div id="workspacesNavContainerWrapper">
                <ul id="workspacesNavContainer" class="nav nav-pills nav-stacked">

                </ul>
            </div>
            <div class="shadowCurvedTop1"></div>
            <div id="lastWorkspace"></div>

            <ul class="nav nav-pills nav-stacked">

                <%-- 			<li><a class="apl_social"><spring:message code="app.leftmenu.social" /></a><span class="glyphicon glyphicon-chevron-right"></span></li> --%>
                <%-- 			<li><a class="apl_settings"><spring:message code="app.leftmenu.settings" /></a><span class="glyphicon glyphicon-chevron-right"></span></li> --%>
                <security:authorize access="hasAuthority('ADMIN')">
                    <li><a class="apl_administration"><spring:message code="app.leftmenu.administration" /></a><span class="glyphicon glyphicon-chevron-right"></span></li>
                        </security:authorize>
            </ul>

        </div>

        <c:if test="${studioConfiguration.helpMode != 'none' }">
            <ul id="leftMenuHelp" class="nav nav-pills nav-stacked">
                <li><a class="apl_help"><spring:message code="app.leftmenu.help" /></a><span class="glyphicon glyphicon-chevron-right"></span></li>
            </ul>
        </c:if>
        <ul id="leftMenuLogout" class="nav nav-pills nav-stacked">
            <li><a href="j_spring_security_logout" target="_self">
                    <span class="glyphicon glyphicon-log-out"></span>
                    <spring:message code="master.page.logout" />
                </a></li>
        </ul>
    </div>

    <div id="appLeftMenuContentShadow"></div>
</div>