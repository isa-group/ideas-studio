<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">
            <spring:message code="editor.actions.modal.create_workspace.title" />
        </h4>
    </div>    

    <div class="modal-body">                        
        <div class="input-group" id="modalCreationField" >
            <span class="input-group-addon"><spring:message code="editor.actions.modal.create_workspace.msg" /></span>
            <input type="text" class="modalCreationField form-control focusedInput">
        </div>
        <br>
        <div class="input-group" id="descriptionInput" >
            <span class="input-group-addon"><spring:message code="editor.actions.modal.create_workspace.description" /></span>
            <textarea type="text" class="form-control"></textarea>
        </div>
        <br>
        <div class="input-group" id="tagsInput" >
            <span class="input-group-addon"><spring:message code="editor.actions.modal.create_workspace.tags" /></span>
            <textarea type="text" class="form-control"></textarea>
        </div>

        <div class="input-group">     
            <div class="checkbox">
                <label>
                    <input class="btn btn-primary" type="checkbox" data-toggle="collapse" data-target="#zipFileDiv" aria-expanded="false" aria-controls="zipFileDiv">
                    <spring:message code="editor.actions.modal.initialize_with_zip_file.msg" />
                </label>
            </div>                                                            
            <div class="collapse" id="zipFileDiv">            
                <div class="input-group">
                    <form action="files/uploadAndExtract/" method="POST" enctype="multipart/form-data">
                        <input type="file" name="zipFile" id="zipFile" class="form-control"/>            
                        <input type="submit" id="uploadSubmit" class="hidden"/>
                    </form>
                </div>
            </div>
        </div>    
    </div>

    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss">Close</a>
        <a class="btn btn-primary continue">
            <spring:message code="editor.actions.modal.create_workspace.button" />
        </a>
    </div>
</ideas:app-modal-dialog>