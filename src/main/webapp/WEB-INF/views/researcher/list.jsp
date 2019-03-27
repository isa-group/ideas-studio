<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>


<ideas:pages-template>
    <script>

        jQuery(function () {



            $("#settingsSubmitChanges").prop('disabled', true);
            $("#settingsRevertChanges").prop('disabled', true);


            $("#settingsSubmitChanges").click(function () {

                if (formsChanges)
                    $('#detailsForm').submit();

            });

            $("#settingsRevertChanges").click(function () {
                activateUser(currentId);
            });

            $(".goHome").click(function () {
                window.location = $('base').attr('href');
            });

        });

        var formsChanges = false;
        var currentId = null;
        var activeUserNav = null;

        var activateUser = function (id) {
            if (formsChanges) {
                $('#confirmationModal').modal('show');
            } else {
                continueActivation(id);
            }
        };

        var continueActivation = function (id) {
            currentId = id;
            if (activeUserNav)
                activeUserNav.removeClass("active");

            activeUserNav = $("#usernav_" + id).addClass("active");

            $("#settingsSubmitChanges").prop('disabled', true);
            $("#settingsRevertChanges").prop('disabled', true);

            $.ajax({
                type: "GET",
                url: "settings/detail?userId=" + id,
                success: function (result) {
                    formResponseReceived(result);
                }
            });
        };

        var formResponseReceived = function (content) {
            formsChanges = false;
            $("#settingsSubmitChanges").prop('disabled', true);
            $("#settingsRevertChanges").prop('disabled', true);

            $("#adminUserLoader").empty();
            $("#adminUserLoader").append(content);
        };
    </script>

    <div id="settingsPageHeader">

        <div class="goHome">
            <span class="glyphicon glyphicon-chevron-left"></span> <span
                class="glyphicon glyphicon-home"></span>
        </div>
        <h2>
            <spring:message code="settings.title.admin" />
        </h2>
    </div>
    <div id="adminContent">
        <div id="adminWrapper">
            <ul id="adminUserList" class="nav nav-pills nav-stacked">
                <c:forEach items="${researchers}" var="researcher">
                    <li id="usernav_${researcher.userAccount.id}"
                        onclick='activateUser("${researcher.userAccount.id}");'><a><span>${researcher.name}</span><BR />
                            <span>${researcher.userAccount.username}</span></a></li>
                        </c:forEach>
            </ul>
            <div id="adminUserLoaderWrapper">
                <div id="adminUserLoader"></div>

            </div>
            <div id="settingsButtonPanel">
                <!-- 			<span id="statusPanel"><span class="glyphicon glyphicon-ok"></span></span> -->
                <button type="button" id="settingsRevertChanges"
                        class="btn btn-default">
                    <spring:message code="app.settings.revert" />
                </button>
                <button type="button" id="settingsSubmitChanges"
                        class="btn btn-primary">
                    <spring:message code="app.settings.savechanges" />
                </button>
            </div>
        </div>

        <div class="shadowCurvedBottom1"></div>
    </div>

    <!-- Modal -->
    <div class="modal" id="confirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Unsaved User Changes</h4>
                </div>
                <div class="modal-body">
                    Please save or revert the current changes before switching to another user.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
                    <!--         <button type="button" class="btn btn-primary">Save changes</button> -->
                </div>
            </div>
        </div>
    </div>


    <%--     <spring:url  var="create" value='${"researcher/create"}' />                     --%>
    <%--     <a href="${create}" class="btn btn-success"><i class="icon-plus"></i><spring:message code="action.create"/></a> --%>
</ideas:pages-template>