<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.upload_file.title"/></h4>
    </div>
    <div class="modal-body">
        <span class="btn btn-success fileinput-button">
            <i class="glyphicon glyphicon-plus"></i>
            <span>Select files...</span>
            <!-- The file input field used as target for the file upload widget -->
            <input id="fileupload" type="file" name="files[]" multiple>
        </span>
        <br>
        <!-- The global progress bar -->
        <div id="progress" class="progress">
            <div class="progress-bar progress-bar-success"></div>
        </div>
        <!-- The container for the uploaded files -->
        <div id="files" class="files"></div>
        <script>
            var nodeUri = FileApi.calculateNodeUri(currentSelectedNode);
            var url = "files/upload/" + nodeUri;
            $("#fileupload").fileupload(
                    {
                        url: url,
                        dataType: 'json',
                        done: function (e, data) {
                            $.each(data.result, function (index, file) {
                                console.info("Uploaded: " + file.fileName + " of " + file.fileSize + " bytes");
                                var myfileExtension = extractFileExtension(file.fileName);
                                var node = currentSelectedNode;
                                var tittleAsArray = (node.data.title).split(".");
                                var parent = null;
                                if (tittleAsArray.length > 1) {
                                    parent = node.getParent();
                                    if (parent != null) {
                                        node = parent;
                                    }
                                }
                                var nodeUri = FileApi.calculateNodeUri(node);
                                fileUri = WorkspaceManager.getSelectedWorkspace() + "/" + nodeUri + "/" + file.fileName;
                                var keyPath = nodeUri + "/" + file.fileName;
                                var newChild = buildChild(file.fileName, false, getIconName(myfileExtension), keyPath); //"file_icon");                                                            
                                node.addChild(newChild);
                                node.sortChildren();
                                $('<p/>').text(file.fileName + " (" + file.fileSize + ") Uploaded to: " + fileUri).appendTo('#files');
                                if (fileUploadCallback) {
                                    fileUploadCallback(nodeUri + "/" + file.fileName)
                                }
                            });
                        },
                        progressall: function (e, data) {
                            var progress = parseInt(data.loaded / data.total * 100, 10);
                            $('#progress .progress-bar').css('width', progress + '%');
                        }
                    }
            ).prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
        </script>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-primary dismiss">Close</a>    
    </div>  
</ideas:app-modal-dialog>