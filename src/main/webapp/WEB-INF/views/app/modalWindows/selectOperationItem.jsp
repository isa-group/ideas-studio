<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.operation.title"/></h4>
    </div>
    <div class="modal-body">
        <div class="input-group" id="modalCreationField" >
            <span class="">
                <spring:message code="editor.actions.modal.operation.msg" />
            </span>
            <div id="operationsTypes"></div>
            <button  class="box-select btn" id="removeAll">Remove all</button>
            <button  class="box-select btn" id="addAll">Add all</button>
        </div>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss">Close</a>
        <a class="btn btn-primary continue"><spring:message code="editor.actions.modal.operation.button"/></a>
    </div>  


    <script>
        $(function () {

            var fileUri = EditorManager.getCurrentUri();
            var opMap = ModeManager.getOperations(ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(fileUri)));
            var opTypes = $("#operationsTypes");
            var opTypesUl = $("#ulOperationsTypes");

            if (opMap != undefined)
                for (var i = 0; i < opMap.length; i++) {
                    opTypes.append('<input type="checkbox" class="box-operation" value="' + opMap[i].id + '" id="box-' + opMap[i].id + '" checked>' + opMap[i].name + '</br>');
                }

            $("#addAll").click(function () {
                $(".box-operation").each(function () {
                    console.log("--->>OK1" + this);
                    this.checked = true;
                });
            });
            $("#removeAll").click(function () {
                $(".box-operation").each(function () {
                    console.log("--->>OK3" + this);
                    this.checked = false;
                });
            });


        });
    </script>
</ideas:app-modal-dialog>