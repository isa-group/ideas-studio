
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<script>
	jQuery(function() {
		//Connected with
		var social = localStorage.getItem('social');
		if (social == "facebook" || social == "twitter" || social == "google") {
			var element = $('div#connect-' + social);
			element.removeClass("btn-primary");
			element.addClass("btn-default");
			element.text("Connected with " + social);
		}

		//social loguin
		if (localStorage.getItem("social") != "") {
			$('#userAccount.username').prop('disabled', true);
			$('#oldPass').prop('disabled', true);
			$('#mypass').prop('disabled', true);
			$('#repeatPass').prop('disabled', true);
			var avatar = $("#principalUserAvatar");
			if (avatar.size() != 0)
				$('#settingsButtonPanel')
						.append(
								"<span class='lead ' style='float: left; color: #628FB8;'>Information acquired from social login</span>");
		}

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
			// 			$("#statusPanel").append('<spring:message code="researcher.settings.problems" />');
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
		// 		var form2Changes = false;

		$("#settingsSubmitChanges").prop('disabled', true);
		$("#settingsRevertChanges").prop('disabled', true);

		$('#settingsForm1 input').on("change paste keyup", function() {
			form1Changes = true;
			$("#settingsSubmitChanges").prop('disabled', false);
			$("#settingsRevertChanges").prop('disabled', false);
		});

		if (urlHash == "#account") {
			$('#accountTab').tab('show');
		} else if (urlHash == "#social") {
			$('#socialTab').tab('show');
			$('#settingsButtonPanel').hide();
		} else {
			$('#profileTab').tab('show');
			$('#settingsButtonPanel').show();
		}

		$("#socialTab").click(function() {
			$('#settingsButtonPanel').hide();
		});
		$("#profileTab").click(function() {
			$('#settingsButtonPanel').show();
		});

		$('#settingTabs a').click(function(e) {
			window.location.hash = '#' + $(this).attr('id').replace("Tab", "");
			e.preventDefault();
		});

		$('#settingTabs .nav-tabs li a').click(function(e) {
			e.preventDefault();
			$(this).tab('show');
		});

		$("#settingsSubmitChanges").click(function() {

			if (form1Changes)
				$('#settingsForm1').submit();

		});

		$("#settingsRevertChanges").click(function() {
			location.reload();
		});

		$(".goHome").click(function() {
			window.location = $('base').attr('href');
		});

	});
	function redirecToLogin() {
		window.location.href = $('base').attr('href');
	}
</script>

<!-- return to login -->

<div id="loginOKPanel" class="modal" data-backdrop="static"
	data-keyboard="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Account created successfully</h4>
			</div>
			<div class="modal-body">
				<p>Thanks for signing up, please go to our login page to access
					the application.</p>
				<p>In brief you will receive an email with the information
					necessary for the activation of the account.</p>
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


<div id="loginFailPanel" class="modal" data-backdrop="static"
	data-keyboard="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Sign up error</h4>
			</div>
			<div class="modal-body">
				<p>Has not been able to make sign up due to errors in the fields</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn" onclick="redirecToLogin()">Return
					to Login</button>
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

	<form:form id="settingsForm1" modelAttribute="researcher"
		action="settings/user">

		<!-- PERSONAL INFORMATION -->

		<div class="tab-content">
			<div class="tab-pane fade" id="profileTabContent">
				<div id="Personal Data">


					<form:hidden path="id" />
					<form:hidden path="version" />
					<form:hidden path="userAccount" />
					<div>
						<div class="control-group">
							<div class="input-group">
								<span class="input-group-addon"> <spring:message
										code="researcher.name" />
								</span>
								<form:input path="name" placeholder="Name Surname" type="text"
									class="form-control" maxlength="25" />
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


					<%-- 				</form:form> --%>
				</div>
			</div>


			<!-- ACCOUNT -->


			<security:authorize access="isAuthenticated()">

				<div class="tab-pane fade active" id="accountTabContent">

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
							<form:input path="userAccount.username" placeholder="myusername"
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
							<!-- 							<input type="password" id="mypass" name="mypass" class="form-control" > -->
							<!-- 							<input type="password" id="mypass" name="mypass" class="form-control" > -->
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
							<%-- 						<form:password id="repeatPass" name="repeatPass" class="form-control" path="userAccount.repeatPassword" /> --%>
							<input type="password" id="repeatPass" name="repeatPass"
								class="form-control">
						</div>
						<span id="repeatPassError"
							class="label label-danger hide alert-error"><spring:message
								code="security.differentpasswords.error" /></span>
						<%-- 							<span class="label label-danger"><form:errors path="userAccount.repeatPassword" /></span> --%>
					</div>

					<%-- 				</form:form> --%>
				</div>
				<script src="js/vendor/passfield.min.js"></script>
				<script>
					// 					$("#mypass").passField({ /*options*/});

					$("#password-repeat-errors").hide();

					jQuery(function() {
						$("#submit").click(function() {
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

<!--				<div class="tab-pane fade active" id="socialTabContent">
					<div>
						<jstl:forEach items="${missingServices}" var="snetwork">
							<div id="${snetwork}-connections">
								<div id="connect-${snetwork}"
									class="btn btn-primary ${snetwork}-icon connection"
									onclick='location.href="connect/${snetwork}"'>Connect to
									${snetwork}</div>
								<hr>
							</div>
						</jstl:forEach>
						<jstl:forEach var="sconfig " items="${servicesConfigs}">
							<div id="">
								<div id="${sconfig.service}-configuration">
									<spring:url value="socialnetwork/edit"
										var="${serviceConfigurationURL}">
										<spring:param name="service"
											value="${serviceConfiguration.service}" />
									</spring:url>
									<a href="${serviceConfigurationURL}"> ${sconfig.service} -
										<spring:message code="action.edit" /> Config.
									</a>
								</div>
							</div>
						</jstl:forEach>
					</div>
				</div>-->
				<%-- 		</jstl:if> --%>
			</security:authorize>


			<div id="settingsButtonPanel">
				<span id="statusPanel"> <span class="glyphicon glyphicon-ok"></span>
				</span>
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
	</form:form>
</div>

<div class="shadowCurvedBottom1"></div>

<%--<security:authorize access="isAnonymous()">
	<div id="socialAccountCreation">
		<form id="tw_signin" action="/signin/twitter" method="POST">
			<button type="submit">
				<img src="/img/Facebook.png" />
			</button>
		</form>
		<form id="fb_signin" action="/signin/facebook" method="POST">
			<button type="submit">
				<img src="/img/sign-in-with-facebook.png" />
			</button>
		</form>
	</div>
</security:authorize>--%>


