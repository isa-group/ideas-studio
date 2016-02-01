<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
    $("#modalCreationField input").val(WorkspaceManager.getSelectedWorkspace());
</script>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h4 class="modal-title">
        Edit Workspace
    </h4>
</div>    

<div class="modal-body">                        
    <div class="input-group" id="modalCreationField" >
        <span class="input-group-addon"><spring:message code="editor.actions.modal.create_workspace.msg" /></span>
        <input type="text" class="form-control focusedInput">
    </div>
        <br>
    <div class="input-group" id="descriptionInput" >
        <span class="input-group-addon"><spring:message code="editor.actions.modal.create_workspace.description" /></span>
        <textarea type="text" class="form-control"></textarea>
    </div>
        <br>        
</div>

<div class="modal-footer">
    <a data-dismiss="modal" class="btn dismiss">Close</a>
    <a class="btn btn-primary continue">
        Save
    </a>
</div>
