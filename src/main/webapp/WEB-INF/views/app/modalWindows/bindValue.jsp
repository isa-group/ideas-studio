<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.bind_value.title" /></h4>
    </div>
    
    <div class="modal-body">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-3">
                    <label style="color: #aaa;"><spring:message code="editor.actions.modal.bind_value.original_value" />:</label>
                </div>
                <div class="col-xs-9">
                    <span id="ideasBindingOriginalBindingToModify" style="color: #aaa;">${bindingText}</span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-3">
                    <label><spring:message code="editor.actions.modal.new_value" />:</label>
                </div>
                <div class="col-xs-9">
                    <input type="text" id="ideasBindingValueToModify" value="${valueToModifyText}"
                        title='<spring:message code="editor.actions.modal.new_value_description" />' style="width:100%;" />
                </div>
            </div>
            <!-- Use it to show any validation message -->
            <div type="ideasBindingValidation"></div>
        </div>
    </div>

    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss"><spring:message code="app.modal.close" /></a>
        <a class="btn btn-primary continue"><spring:message code="action.ok"/></a>
    </div>
</ideas:app-modal-dialog>