<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-template>
    <script>
        jQuery(function() {

        AppPresenter.setCurrentSection("settings");
                var urlHash = window.location.hash;
                if (urlHash == "#profile")
                $('#profileSettingsTab').tab('show');
                else if (urlHash == "#account")
                $('#accountSettingsTab').tab('show');
                else if (urlHash == "#social")
                $('#socialSettingsTab').tab('show');
                else
                $('#generalSettingsTab').tab('show');
                $('#generalSettingsTab').click(function(e) {
        window.location.hash = '#general';
                e.preventDefault();
        });
                $('#profileSettingsTab').click(function(e) {
        window.location.hash = '#profile';
                e.preventDefault();
        });
                $('#accountSettingsTab').click(function(e) {
        window.location.hash = '#account';
                e.preventDefault();
        });
                $('#socialSettingsTab').click(function(e) {
        window.location.hash = '#social';
                e.preventDefault();
        });
                // Forms changes

                var generalFormChanged = false;
                var profileFormChanged = false;
                var accountFormChanged = false;
                var socialFormChanged = false;
                var generalFormHasErrors = false;
                var profileFormHasErrors = false;
                var accountFormHasErrors = false;
                var socialFormHasErrors = false;
                $("#settingsSubmitChanges").prop('disabled', true);
                $("#settingsRevertChanges").prop('disabled', true);
                var settingsFormChanged = function(general, profile, account, social) {
                generalFormChanged = general != null && general;
                        profileFormChanged = profile != null && profile;
                        accountFormChanged = account != null && account;
                        socialFormChanged = social != null && social;
                        if (general || profile || account || social) {
                $("#settingsSubmitChanges").prop('disabled', false);
                        $("#settingsRevertChanges").prop('disabled', false);
                }

                console.log(general + " " + profile + " " + account + " " + social);
                };
                var settingsFormHasError = function(general, profile, account, social) {
                generalFormHasErrors = general != null && general;
                        profileFormHasErrors = profile != null && profile;
                        accountFormHasErrors = account != null && account;
                        socialFormHasErrors = social != null && social;
                        if (generalFormHasErrors)
                        $('#generalSettingsTab').addClass("hasErrors");
                        else
                        $('#generalSettingsTab').removeClass("hasErrors");
                        if (profileFormHasErrors)
                        $('#profileSettingsTab').addClass("hasErrors");
                        else
                        $('#profileSettingsTab').removeClass("hasErrors");
                        if (accountFormHasErrors)
                        $('#accountSettingsTab').addClass("hasErrors");
                        else
                        $('#accountSettingsTab').removeClass("hasErrors");
                        if (socialFormHasErrors)
                        $('#socialSettingsTab').addClass("hasErrors");
                        else
                        $('#socialSettingsTab').removeClass("hasErrors");
                };
                var submitChanges = function() {
                $("#settingsSubmitChanges").prop('disabled', true);
                        $("#settingsRevertChanges").prop('disabled', true);
                        if (generalFormChanged)
                        $('#editGeneralForm').submit();
                        if (profileFormChanged)
                        $('#editProfileForm').submit();
                        if (accountFormChanged)
                        $('#editAccountForm').submit();
                        if (socialFormChanged)
                        $('#editSocialForm').submit();
                        generalFormChanged = false;
                        profileFormChanged = false;
                        accountFormChanged = false;
                        socialFormChanged = false;
                };
                // TODO: Refactor. Use dynamic loading per tab.

                AppPresenter.loadDynamically("#generalSettings", "security/useraccount/general", function() {
                AppPresenter.loadDynamically("#profileSettings", "researcher/edit", function() {
                AppPresenter.loadDynamically("#accountSettings", "security/useraccount/edit", function() {
                AppPresenter.loadDynamically("#socialSettings", "researcher/social", function() {
                setupFormHandlers();
                });
                });
                        var setupFormHandlers = function() {

                        // Form submission interception:
                        var generalForm = $('#editGeneralForm');
                                generalForm.submit(function() {

                                $.ajax({
                                type : generalForm.attr('method'),
                                        url : generalForm.attr('action'),
                                        data : generalForm.serialize(),
                                        success : function(result) {
                                        $('#result').attr("value", result);
                                                AppPresenter.updateSettingsForm("general", result);
                                                if ($("#editGeneralForm .formError").length)
                                                settingsFormHasError(true, null, null, null);
                                                setupFormHandlers();
                                        }
                                });
                                        return false;
                                });
                                var profileForm = $('#editProfileForm');
                                profileForm.submit(function() {

                                $.ajax({
                                type : profileForm.attr('method'),
                                        url : profileForm.attr('action'),
                                        data : profileForm.serialize(),
                                        success : function(result) {
                                        $('#result').attr("value", result);
                                                AppPresenter.updateSettingsForm("profile", result);
                                                if ($("#editProfileForm .formError").length)
                                                settingsFormHasError(null, true, null, null);
                                                setupFormHandlers();
                                        }
                                });
                                        return false;
                                });
                                var accountForm = $('#editAccountForm');
                                accountForm.submit(function() {

                                $.ajax({
                                type : accountForm.attr('method'),
                                        url : accountForm.attr('action'),
                                        data : accountForm.serialize(),
                                        success : function(result) {
                                        $('#result').attr("value", result);
                                                AppPresenter.updateSettingsForm("account", result);
                                                if ($("#editAccountForm .formError").length)
                                                settingsFormHasError(null, null, true, null);
                                                setupFormHandlers();
                                        }
                                });
                                        return false;
                                });
                                var socialForm = $('#editSocialForm');
                                accountForm.submit(function() {

                                $.ajax({
                                type : socialForm.attr('method'),
                                        url : socialForm.attr('action'),
                                        data : socialForm.serialize(),
                                        success : function(result) {
                                        $('#result').attr("value", result);
                                                AppPresenter.updateSettingsForm("social", result);
                                                if ($("#editSocialForm .formError").length)
                                                settingsFormHasError(null, null, null, true);
                                                setupFormHandlers();
                                        }
                                });
                                        return false;
                                });
                                $("#editGeneralForm input").on("change paste keyup", function() {
                        settingsFormChanged(true, null, null, null);
                        });
                                $("#editProfileForm input").on("change paste keyup", function() {
                        settingsFormChanged(null, true, null, null);
                        });
                                $("#editAccountForm input").on("change paste keyup", function() {
                        settingsFormChanged(null, null, true, null);
                        });
                                $("#editSocialForm input").on("change paste keyup", function() {
                        settingsFormChanged(null, null, null, true);
                        });
                        };
                        // Form buttons:
                        $("#settingsSubmitChanges").click(function(){
                submitChanges();
                });
                });
    </script>
    <div class="appTabbedView">
        <div class="tabsPanelWrapper">
            <ul class="tabsPanel nav nav-tabs">
                <li><a id="generalSettingsTab" href="#generalSettings" data-toggle="tab"><spring:message code="app.settings.tab.general" /></a></li>
                <li><a id="profileSettingsTab" href="#profileSettings" data-toggle="tab"><spring:message code="app.settings.tab.profile" /></a></li>
                <li><a id="accountSettingsTab" href="#accountSettings" data-toggle="tab"><spring:message code="app.settings.tab.account" /></a></li>
                <li><a id="socialSettingsTab" href="#socialSettings" data-toggle="tab"><spring:message code="app.settings.tab.social" /></a></li>
            </ul>
        </div>
        <div class="tab-contentWrapper">
            <div class="tab-content">
                <div class="tab-pane fade in" id="generalSettings"></div>
                <div class="tab-pane fade in" id="profileSettings"></div>
                <div class="tab-pane fade in" id="accountSettings"></div>
                <div class="tab-pane fade in" id="socialSettings"></div>
            </div>
        </div>
        <div class="buttonPanelWrapper">
            <div class="buttonPanel">
                <button type="button" id="settingsRevertChanges" class="btn btn-default">
                    <spring:message code="app.settings.revert" />
                </button>
                <button type="button" id="settingsSubmitChanges" class="btn btn-primary">
                    <spring:message code="app.settings.savechanges" />
                </button>
            </div>
        </div>

    </div>
</ideas:app-template>