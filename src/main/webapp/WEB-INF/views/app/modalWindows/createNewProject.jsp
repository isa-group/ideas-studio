<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.create_project.title" /></h4>
    </div>
    <div class="modal-body" id="from-scrach">
        <h4><spring:message code="editor.actions.modal.create_project.form_scratch" /></h4>
        <div class="input-group" id="modalCreationField" >
            <span class="input-group-addon input-modal"><spring:message code="editor.actions.modal.create_project.msg" /></span>
            <input type="text" class="modalCreationField form-control focusedInput" onKeypress="checkerName(event)">
        </div>
        <div class="input-group" id="template-selection">
            <h4><spring:message code="editor.actions.modal.create_project.from_template" /></h4>
            <label for="template-from-module"> <spring:message code="editor.actions.modal.create_project.from_template_of_module" /></label>
            <select id="template-from-module" name="template-from-module" class="form-control">
                <option id="module-empty" value=""><spring:message code="editor.actions.modal.create_project.choose_a_template" /></option>
                <jstl:forEach var="language" items="${studioConfiguration.modules}">
                    <optgroup label="${language.key}" id="${language.key}-optgroup">
                    </optgroup>
                </jstl:forEach>
            </select>

            <dir id="specificModuleTemplates">
                <label> <spring:message code="editor.actions.modal.create_project.from_template_of_module" /></label>
                <select id="ProjectTemplate">
                    <option value="">Empty project</option>
                </select>
            </dir>
        </div>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss">Close</a>
        <a class="btn btn-primary continue"><spring:message code="editor.actions.modal.create_workspace.button"/></a>
    </div>
    <script>
        var mytemplates, mytemplate, index;
        $("#template-selection").hide(); //enable it when project templates become active
        <jstl:forEach var="language" items="${studioConfiguration.modules}">
    $.ajax({"url": '${language.value}/template/project',
            success: function (result, textStatus, request) {
            console.log("Templates provided by the ${language.key} module:" + result + "");
                mytemplates = $.parseJSON(result);
                if (mytemplates !== null && mytemplates.length != 0) {
                    for (index = 0; index < mytemplates.length; index++) {
                        mytemplate = mytemplates[index];
                    $("#${language.key}-optgroup").append('<option value="' + mytemplate.name + '">' + mytemplate.name + '</option>');
                    }
                } else {
                    console.log("------" + "${language.key}");
                $("#${language.key}-optgroup").remove();
                }
            },
            error: function (result, textStatus, request) {
                $("#template-selection").remove();
            }
        });
        </jstl:forEach>
        $("#template-selection").show();
    </script>
</ideas:app-modal-dialog>