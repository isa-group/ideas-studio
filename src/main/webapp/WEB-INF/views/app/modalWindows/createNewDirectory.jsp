<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.create_directory.title"/></h4>
    </div>
    <div class="modal-body">
        <div class="input-group" id="modalCreationField" >
            <span class="input-group-addon">
                <spring:message code="editor.actions.modal.create_directory.msg" />
            </span>
            <input type="text" class="modalCreationField form-control focusedInput" onKeypress="checkerName(event)">
        </div>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss">Close</a>
        <a class="btn btn-primary continue"><spring:message code="editor.actions.modal.create_directory.button"/></a>
    </div>
</ideas:app-modal-dialog>