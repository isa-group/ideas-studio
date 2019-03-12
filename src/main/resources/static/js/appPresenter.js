$(window).load(function () {

    window.setTimeout(function () {
        $("#appWrapper").css("opacity", "1");
        $("#appLoaderBlocker").css("opacity", "0");
        window.setTimeout(function () {
            $("#appLoaderBlocker").css("visibility", "hidden");
        }, 500);
    }, 500);

});

var principalUser = "";
var principalUserName = "";
var currentSectionElement;
var currentSection;

var transTime = 200;

var hideCurrentSection = function () {

    $('#appBodyBlocker').css("visibility", "visible");
    $('#appBodyBlocker').css("opacity", "0.8");


    setTimeout(function () {
        $('#appBodyLoader').empty();
    }, transTime);
};

var showSection = function (sectionContent) {
    $('#appBodyLoader').append(sectionContent);
    setTimeout(function () {
        $('#appBodyBlocker').css("opacity", "0");
    }, 1);
    setTimeout(function () {
        $('#appBodyBlocker').css("visibility", "hidden");
    }, transTime);
};

var AppPresenter = {

    loadUserData: function (callback) {
        $.ajax("researcher/principaluser", {
            "type": "get",
            "success": function (user) {
                var email = user.email;
                var auths = user.authorities;
                try {
                    $('#principalUserAvatar').css("background-image", "url('https://www.gravatar.com/avatar/" + $.md5(email) + "')");
                } catch (err) {
                    $('#principalUserAvatar').css("background-image", "url('img/ideas/defaultAvatar.png')");

                }
                $('#principalUserInfo span').first().append(user.name);
                principalUser = user.name;
                principalUserName = user.username;
                
                var auths2 = [];
                $.each(auths, function (ident, value) {
                    auths2.push(value.authority.toLowerCase());
                });

                document.auths = auths;
                document.auths2 = auths2;

                $('#principalUserAuths').append(auths2.join(", "));

                if (callback != null)
                    callback();
            },
            "error": function (result) {
                if (result.statusText) {
                    console.log(result.statusText);
                }
            },
            "async": true,
        });
    },

    getCurrentSection: function () {
        return currentSection;
    },

    setCurrentSection: function (section) {
        currentSection = section;

        if (currentSectionElement != null)
            currentSectionElement.removeClass("active");
        if (section == "editor") {
            currentSectionElement = $("#appNavigator .apl_" + section + "_" + WorkspaceManager.getSelectedWorkspace()).parent();
        } else {
            currentSectionElement = $("#appNavigator .apl_" + section).parent();
        }
        currentSectionElement.addClass("active");
    },

    loadSection: function (section, params, callback) {
        hideCurrentSection();
        currentSection = section;
        if (currentSectionElement != null)
            currentSectionElement.removeClass("active");
        currentSectionElement = $("#appNavigator .apl_" + section + "_" + params).parent();
        currentSectionElement.addClass("active");

        $("#workspacesNavContainer li").removeClass("active");

        history.pushState({}, section, "app/" + section);

        var appSectionUri = "app/app_" + section + "_content";

        if (section == "editor") {
            WorkspaceManager.setSelectedWorkspace(params);
            console.log("Section: " + section + "// Switching workspace to: " + WorkspaceManager.getSelectedWorkspace());
        }

        if (section == "help" && "helpMode" in studioConfiguration) {
            var helpMode = studioConfiguration.helpMode;
            if (helpMode == "static") {
                appSectionUri = studioConfiguration.helpURI;
            }
        }

        $.ajax(appSectionUri, {
            "type": "get",
            "success": function (result) {
                setTimeout(function () {
                    location.reload();
                }, transTime);
            },
            "error": function (result) {
                console.error(result);
            },
            "async": true,
        });
    },

    loadDynamically: function (targetId, url, callback) {

        $.ajax(url, {
            "type": "get",
            "success": function (result) {
                $(targetId).empty();
                $(targetId).append(result);
                if (callback != null)
                    callback(result);
            },
            "error": function (result) {
                console.error(result);
            },
            "async": true,
        });
    },

    updateSettingsForm: function (subsection, content) {
        $('#' + subsection + 'Settings').empty();
        $('#' + subsection + 'Settings').append(content);
    },

    getPrincipalUser: function () {
        return principalUser;
    }
};