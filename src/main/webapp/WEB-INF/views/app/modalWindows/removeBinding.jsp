<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.remove_binding.title" /></h4>
    </div>
    
    <div class="modal-body">
        <label><spring:message code="editor.actions.modal.remove_binding.msg_1" />:</label>
        <span>${textNote}</span><br/>
        <label><spring:message code="editor.actions.modal.remove_binding.msg_2" />:</label>
        <span>${getTargetText}</span><br/>
        <label><spring:message code="editor.actions.modal.remove_binding.msg_3" />:</label>
        <span>${rowAux}</span>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss"><spring:message code="app.modal.close" /></a>
        <a class="btn btn-primary continue"><spring:message code="action.ok"/></a>
    </div>
</ideas:app-modal-dialog>