<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>

<ideas:pages-template>
    <script>
        jQuery(function () {
            //Connected with
            var isAutheniticated =<security:authorize access="isAuthenticated()">true</security:authorize><security:authorize access="isAnonymous()">false</security:authorize>;
                    var social = localStorage.getItem('social');
                    if (social == "facebook" || social == "twitter" || social == "google") {
                        var element = $('div#connect-' + social);
                        element.removeClass("btn-primary");
                        element.addClass("btn-default");
                        //			element.text("Connected with " + social);
                    }

                    //social login
                    if (localStorage.getItem("social")) {
                        $('#userName').prop('readonly', true);
                        $('#oldPass').prop('readonly', true);
                        $('#mypass').prop('readonly', true);
                        $('#repeatPass').prop('readonly', true);
                        var avatar = $("#principalUserAvatar");
                    }
                    $('#userName').prop('readonly', true);

                    $('#loginOKPanel').modal('hide');
                    $('#loginFailPanel').modal('hide');
                    // Check for status
                    var postSuccess = "${success}";
                    console.log(postSuccess);
                    var avatar = $("#principalUserAvatar");
                    if (postSuccess == "true") {
                        $("#statusPanel").addClass("success");
                        $("#statusPanel span").addClass("glyphicon-ok");

                        if (avatar.size() == 0) {
                            $('#loginOKPanel').modal('show');
                        }
                    } else if (postSuccess == "false") {
                        console.log("captured");
                        $("#statusPanel").addClass("problems");
                        $("#statusPanel span").addClass("glyphicon-remove");
                        //$("#statusPanel").append('<spring:message code="researcher.settings.problems" />');
                        //
                        // Check repeated email
                        var repeatedEmail = "${repeatedEmail}";
                        if (repeatedEmail == "true") {
                            $("#statusPanel")
                                    .append(
                                            '<spring:message code="security.repeatedEmail.error" />');
                        } else {
                            $("#statusPanel")
                                    .append(
                                            '<spring:message code="researcher.settings.problems" />');
                            if (avatar.size() == 0) {
                                $('#loginFailPanel').modal('show');
                            }
                        }
                    }

                    var badPassword = "${badPassword}";
                    if (badPassword == "true") {
                        $("#badPassError").removeClass("hide");
                    }

                    var urlHash = window.location.hash;

                    var form1Changes = false;
                    var form2Changes = false;

                    $("#settings1SubmitChanges").prop('disabled', true);
                    $("#settings2SubmitChanges").prop('disabled', true);

                    $('#settingsForm1 input').on("change paste keyup", function () {
                        form1Changes = true;
                        $("#settings1SubmitChanges").prop('disabled', false);
                    });

                    $('#settingsForm2 input').on("change paste keyup", function () {
                        form2Changes = true;
                        $("#settings2SubmitChanges").prop('disabled', false);
                    });


                    if (urlHash == "#account") {
                        $('#accountTab').tab('show');
                    } else if (urlHash == "#social") {
                        $('#socialTab').tab('show');
                    } else {
                        $('#profileTab').tab('show');
                    }

                    $('#settingTabs a').click(function (e) {
                        window.location.hash = '#' + $(this).attr('id').replace("Tab", "");
                        e.preventDefault();
                    });

                    $('#settingTabs .nav-tabs li a').click(function (e) {
                        e.preventDefault();
                        $(this).tab('show');
                    });

                    $("#settings1SubmitChanges").click(function () {
                        $('#settingsForm1').submit();
                    });

                    $("#settings2SubmitChanges").click(function () {
                        var message = "";
                        if ($("#oldPass").val() == "") {
                            console.log("missing oldPass value");
                            message += '"Old password" field is missing.';
                        }
                        if ($("#mypass").val() == "") {
                            console.log("missing mypass value");
                            message += '<br>"Password" field is missing.';
                        }
                        if ($("#repeatPass").val() == "") {
                            console.log("missing repeatPass value");
                            message += '<br>"Repeat password" field is missing.';
                        }
                        if ($("#repeatPass").val() != $("#mypass").val()) {
                            console.log("passwords not matching");
                            message += '<br>Passwords do not match.';
                        }
                        console.log(message);
                        if (message) {
                            if (message.indexOf("<br>") >= 0) {
                                $("#pagesContent #settingsButtonPanel").css("padding", "1em 1em 4em 1em");
                            }
                            $("#statusPanel").removeClass("success").addClass("problems").html("").append('<span class="glyphicon glyphicon-ok glyphicon-remove"></span>' + message + '</span>');
                        } else {
                            $('#settingsForm2').submit();
                        }
                    });

                    $("#settingsRevertChanges").click(function () {
                        window.location = $('base').attr('href');
                    });

                    $(".goHome").click(function () {
                        window.location = $('base').attr('href');
                    });

                });

                function redirecToLogin() {
                    window.location.href = $('base').attr('href');
                }
    </script>

    <!-- return to login -->

    <div id="loginOKPanel" class="modal" data-backdrop="false" data-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Account created successfully</h4>
                </div>
                <div class="modal-body">
                    <p>Thanks for signing up, please go to our login page to access the application.</p>
                    <p>In brief you will receive an email with the information necessary for the activation of the account.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary"
                            onclick="redirecToLogin()">Return to Login</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->


    <div id="loginFailPanel" class="modal" data-backdrop="true" data-keyboard="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Sign up error</h4>
                </div>
                <div class="modal-body">
                    <p>Unable to sign up. Check the errors in red.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->

    <div id="settingsPageHeader">

        <div class="goHome">
            <span class="glyphicon glyphicon-chevron-left"></span> <span
                class="glyphicon glyphicon-home"></span>
        </div>
        <security:authorize access="isAuthenticated()">
            <h2>
                <spring:message code="settings.title.edit" />
            </h2>
        </security:authorize>
        <security:authorize access="isAnonymous()">
            <h2>
                <spring:message code="settings.title.new" />
            </h2>
        </security:authorize>

    </div>

    <div id="settingsContainer">
        <ul class="nav nav-tabs" id="settingTabs">
            <li><a href="#profileTabContent" data-toggle="tab"
                   id="profileTab"><spring:message code="userAccount.showHeader" /></a></li>
                <security:authorize access="isAuthenticated()">
                <li><a href="#accountTabContent" data-toggle="tab"
                       id="accountTab"><spring:message code="userAccount.editHeader" /></a></li>
                <li><a href="#socialTabContent" data-toggle="tab" id="socialTab"><spring:message
                            code="socialnetwork.editHeader" /></a></li>
                    </security:authorize>
        </ul>
        <!-- PERSONAL INFORMATION -->
        <security:authorize access="isAuthenticated()">

            <div class="tab-content">

                <div class="tab-pane fade" id="profileTabContent">
                    <div id="Personal Data">
                        <form:form id="settingsForm1" modelAttribute="researcher"
                                   action="settings/user">
                            <form:hidden path="id" />
                            <form:hidden path="version" />
                            <form:hidden path="userAccount"/>
                            <form:hidden path="userAccount.username" />
                            <form:hidden path="userAccount.password" />
                            <jstl:forEach items="${researcher.userAccount.authorities}"
                                        var="authority" varStatus="status">
                                <form:hidden path="userAccount.authorities[${status.index}]" />
                            </jstl:forEach>
                            <input type="hidden" name="oldPass">
                            <input type="hidden" name="repeatPass">

                            <div>
                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <spring:message
                                                code="researcher.name" />
                                        </span>
                                        <form:input path="name" placeholder="Name Surname" type="text"
                                                    class="form-control" />
                                    </div>
                                    <span class="label label-danger"><form:errors path="name" /></span>
                                </div>

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <spring:message
                                                code="researcher.email" />
                                        </span>
                                        <form:input path="email" placeholder="myname@domain.com"
                                                    type="email" class="form-control" maxlength="50" />
                                    </div>
                                    <span class="label label-danger"><form:errors path="email" /></span>
                                </div>

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <spring:message
                                                code="researcher.phone" />
                                        </span>
                                        <form:input path="phone" placeholder="" type="text"
                                                    class="form-control" maxlength="9" />
                                    </div>
                                    <span class="label label-danger"><form:errors path="phone" /></span>
                                </div>

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <spring:message
                                                code="researcher.address" />
                                        </span>
                                        <form:input path="address" placeholder="" type="text"
                                                    class="form-control" maxlength="50" />
                                    </div>
                                    <span class="label label-danger"><form:errors
                                            path="address" /></span>
                                </div>
                            </div>

                            <div id="settingsButtonPanel">
                                <span id="statusPanel"> <span class="glyphicon glyphicon-ok"></span>
                                </span>
                                <button type="button" id="settings1RevertChanges"
                                        class="btn btn-default">
                                    <spring:message code="app.settings.revert" />
                                </button>
                                <button type="button" id="settings1SubmitChanges"
                                        class="btn btn-primary">
                                    <spring:message code="app.settings.savechanges" />
                                </button>
                            </div>

                        </form:form>
                    </div>
                </div>


                <!-- ACCOUNT -->
                <div class="tab-pane fade active" id="accountTabContent">
                    <form:form id="settingsForm2" modelAttribute="researcher"
                               action="settings/user">
                        <form:hidden path="id" />
                        <form:hidden path="version" />
                        <form:hidden path="userAccount"/>
                        <form:hidden path="name" />
                        <form:hidden path="email" />
                        <form:hidden path="phone" />
                        <form:hidden path="address" />

                        <div id="Authorities">
                            <jstl:forEach items="${researcher.userAccount.authorities}"
                                          var="authority" varStatus="status">
                                <form:hidden path="userAccount.authorities[${status.index}]" />
                            </jstl:forEach>
                        </div>

                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon"><spring:message
                                        code="security.username" /></span>
                                    <form:input id="userName" path="userAccount.username" placeholder="myusername"
                                                type="text" class="form-control" />
                            </div>
                            <span class="label label-danger"><form:errors
                                    path="userAccount.username" /></span>
                        </div>


                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon"><spring:message
                                        code="security.oldpassword" /></span> <input type="password"
                                               id="oldPass" name="oldPass" class="form-control">
                            </div>
                            <span id="badPassError"
                                  class="label label-danger hide alert-error"><spring:message
                                    code="security.badpassword.error" /></span>

                        </div>

                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon"><spring:message
                                        code="security.newpassword" /></span>
                                <form:password id="mypass" name="mypass" class="form-control"
                                               path="userAccount.password" />
                            </div>
                            <span class="label label-danger"><form:errors
                                    path="userAccount.password" /></span>

                        </div>

                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon"><spring:message
                                        code="security.repeatpassword" /></span>
                                <input type="password" id="repeatPass" name="repeatPass"
                                       class="form-control">
                            </div>
                            <span id="repeatPassError"
                                  class="label label-danger hide alert-error"><spring:message
                                    code="security.differentpasswords.error" /></span>
                        </div>

                        <div id="settingsButtonPanel">
                            <span id="statusPanel"> <span class="glyphicon glyphicon-ok"></span>
                            </span>
                            <button type="button" id="settings2RevertChanges"
                                    class="btn btn-default">
                                <spring:message code="app.settings.revert" />
                            </button>
                            <button type="button" id="settings2SubmitChanges"
                                    class="btn btn-primary">
                                <spring:message code="app.settings.savechanges" />
                            </button>
                        </div>

                    </form:form>
                </div>
                <script>
                    $("#password-repeat-errors").hide();

                    jQuery(function () {
                        $("#submit").click(function () {
                            $("#password-repeat-errors").hide();
                            var hasError = false;
                            var passwordVal = $("#mypass").val();
                            var checkVal = $("#repeatPass").val();
                            if (passwordVal != checkVal) {
                                $("#password-repeat-errors").show();
                                hasError = true;
                            }
                            if (hasError == true) {
                                return false;
                            }
                        });
                    });
                </script>

                <!-- SOCIAL -->

                <div class="tab-pane fade active" id="socialTabContent">
                    <jstl:if test="${missingServices.size() > 0}">
                        <h4><spring:message code="researcher.settings.not_connected_services" /></h4>
                    </jstl:if>
                    <jstl:forEach items="${missingServices}" var="snetwork">
                        <form action="connect/${snetwork}" method="POST" class="form-horizontal">
                            <jstl:choose>
                                <jstl:when test="${snetwork == 'google'}">
                                    <input type="hidden" name="scope" value="email profile openid" />
                                    <button type="submit" class="btn btn-social btn-sm btn-${snetwork}-plus">
                                        <i class="fab fa-${snetwork}"></i> <spring:message code="researcher.settings.connect_to" /> <span class="capitalize">${snetwork}</span>
                                    </button>
                                </jstl:when>
                                <jstl:otherwise>
                                    <button type="submit" class="btn btn-social btn-sm btn-${snetwork}">
                                        <i class="fab fa-${snetwork}"></i> <spring:message code="researcher.settings.connect_to" /> <span class="capitalize">${snetwork}</span>
                                    </button>
                                </jstl:otherwise>
                            </jstl:choose>
                        </form>
                        <br />
                    </jstl:forEach>

                    <jstl:if test="${servicesConfigs.size() > 0}">
                        <h4><spring:message code="researcher.settings.connected_services" /></h4>
                    </jstl:if>
                    <jstl:forEach items="${servicesConfigs}" var="service">
                        <form action="connect/${service}" method="POST" class="form-horizontal">
                            <input type="hidden" name="_method" value="delete" />
                            <jstl:choose>
                                <jstl:when test="${service == 'google'}">
                                    <button type="submit" class="btn btn-social btn-sm btn-${service}-plus">
                                        <i class="fab fa-${service}"></i> <spring:message code="researcher.settings.disconnect_from" /> <span class="capitalize">${service}</span>
                                    </button>
                                </jstl:when>
                                <jstl:otherwise>
                                    <button type="submit" class="btn btn-social btn-sm btn-${service}">
                                        <i class="fab fa-${service}"></i> <spring:message code="researcher.settings.disconnect_from" /> <span class="capitalize">${service}</span>
                                    </button>
                                </jstl:otherwise>
                            </jstl:choose>                            
                        </form>
                        <br />
                    </jstl:forEach>
                </div>
            </div>
        </security:authorize>
    </div>

    <div class="shadowCurvedBottom1"></div>
</ideas:pages-template>