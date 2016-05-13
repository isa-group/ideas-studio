// App. Initialization:
jQuery(function () {

    $('.dropdown-toggle').click(function (e) {
        e.preventDefault();
        setTimeout($.proxy(function () {
            if ('ontouchstart' in document.documentElement) {
                $(this).siblings('.dropdown-backdrop').off().remove();
            }
        }, this), 0);
    });

    // Menu toggler:
    $("#menuToggler").click(function () {
        toggleMenu();
    });
    $("#appMainContentBlocker").click(function () {
        toggleMenu();
    });

    $(".addWorkspace").click(function () {
        showContentAsModal("app/modalWindows/createWorkspace", function () {
            var workspaceName = $("#modalCreationField input").val();
            var description = $("#descriptionInput textarea").val();
            var tags = $("#tagsInput textarea").val();
            $("#workspacesNavContainer li").removeClass("active");
            WorkspaceManager.createWorkspace(workspaceName, description, tags);
            AppPresenter.loadSection("editor", workspaceName, function () {
                WorkspaceManager.loadWorkspace();
            });
        });
        // Update workspace name from file
        setTimeout(function () {
            $(".modal-body").find('[data-toggle="collapse"][data-target="#zipFileDiv"]').unbind("click").on("click", function () {
                $("#zipFile").unbind("change").on("change", function () {
                    var fpath = $(this).val().replace(/\\/g, '/');
                    var fname = fpath.substring(fpath.lastIndexOf('/')+1, fpath.lastIndexOf('.'));
                    $("#modalCreationField input").val(fname);
                });
            });
        }, 500);
    });

    WorkspaceManager.readSelectedWorkspace(function (sws) {
        var sws = WorkspaceManager.getSelectedWorkspace();
        WorkspaceManager.getWorkspaces(function (wss) {
            if (!sws) {
                if (wss.length > 0) {
                    WorkspaceManager.setSelectedWorkspace(wss[0].name);
                    WorkspaceManager.loadWorkspace();
                } else if (wss.length == 0) {
                    CommandApi.importDemoWorkspace("SampleWorkspace",
                        "SampleWorkspace");
                }
            } else {
                WorkspaceManager.loadWorkspace();
            }
        });

    });

    // Show the version info content
    $("#version").click(function () {
        $("#versions").show();
    });
    $("#vClose").click(function () {
        $("#versions").hide();
    });



});

// Modal:
var showModal = function (title, content, primaryText, primaryHandler,
    cancelHandler, closeHandler) {
    if ($("#appGenericModal"))
        $("#appGenericModal").remove();
    $("body")
        .append(
            '<!-- Modal for all app -->'
            + '<div class="modal" id="appGenericModal" data-backdrop="static" data-keyboard="false">'
            + '<div class="modal-dialog">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>'
            + '<h4 class="modal-title">Modal title</h4></div>'
            + '<div class="modal-body">  ...</div>'
            + '<div class="modal-footer">'
            + '  <a data-dismiss="modal" class="btn dismiss">Close</a>'
            + '  <a class="btn btn-primary continue"></a></div>  </div></div></div>');
    $('#appGenericModal .modal-title').text(title);
    $('#appGenericModal .modal-body').empty();
    $('#appGenericModal .modal-body').append(content);
    $('#appGenericModal .continue').text(primaryText);
    $('#appGenericModal .close').click(cancelHandler);
    $('#appGenericModal .dismiss').click(cancelHandler);
    $('#appGenericModal .continue').click(primaryHandler);
    $('#appGenericModal').modal({
        show: true
    });
};

var showContentAsModal = function (url, primaryHandler, cancelHandler,
    closeHandler) {
    if ($("#appGenericModal"))
        $("#appGenericModal").remove();
    $.ajax({
        "url": url,
        success: function (result, textStatus, request) {
            $("body").append(result);
            $('#appGenericModal .close').click(cancelHandler);
            $('#appGenericModal .dismiss').click(cancelHandler);
            $('#appGenericModal .continue').click(primaryHandler);
            $('#appGenericModal').modal({
                show: true
            });
            $('.focusedInput').focus();
            $('.modal-content').keypress(function (e) {
                var code = e.which;
                if (code == 13) {
                    primaryHandler();
                }
            });
        }
    });

}

var showError = function (title, content, primaryHandler) {
    if ($("#appGenericError"))
        $("#appGenericError").remove();
    $("body")
        .append(
            '<!-- Modal for all app --><div class="modal" id="appGenericError"><div class="modal-dialog">  <div class="modal-content"><div class="modal-header">  <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>  <h4 class="modal-title">Modal title</h4></div><div class="modal-body"> </div><div class="modal-footer">   <a class="btn btn-primary continue"></a></div>  </div></div></div>');
    $('#appGenericError .modal-title').text(title);
    $('#appGenericError .modal-body').html(content);
    $('#appGenericError .continue').text("OK");

    $('#appGenericError .continue').click(primaryHandler);
    $('#appGenericError').modal({
        show: true
    });
};

var hideError = function () {
    $('#appGenericError').modal('hide');
    $(".modal-backdrop").remove();
};

var hideModal = function () {
    $('#appGenericModal').modal('hide');
};

var toggleMenu = function () {
    var leftMenu = $("#appLeftMenu");
    var contentBlocker = $("#appMainContentBlocker");
    if (leftMenu.hasClass("menuClose")) {
        leftMenu.addClass("menuOpen");
        leftMenu.removeClass("menuClose");
        contentBlocker.addClass("visible");
        contentBlocker.removeClass("hidden");
    } else {
        leftMenu.addClass("menuClose");
        leftMenu.removeClass("menuOpen");
        contentBlocker.addClass("hidden");
        contentBlocker.removeClass("visible");
    }
};

var compareObjects = function equals(obj1, obj2) {
    function _equals(obj1, obj2) {
        var clone = $.extend(true, {}, obj1),
            cloneStr = JSON.stringify(clone);
        return cloneStr === JSON.stringify($.extend(true, clone, obj2));
    }

    return _equals(obj1, obj2) && _equals(obj2, obj1);
}