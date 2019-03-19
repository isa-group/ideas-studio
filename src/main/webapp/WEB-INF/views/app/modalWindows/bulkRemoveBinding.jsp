<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.bulk_remove_bind.title" /></h4>
    </div>

    <div class="modal-body">
        <jstl:choose>
            <jstl:when test="${hasBindingsToRemove}">
                <div class="bulkRemover"> ${listContainer}
                <div style="float:right;"><a class="bulkSelectAll"><spring:message code="editor.actions.modal.all" /></a> | <a class="bulkSelectNone"><spring:message code="editor.actions.modal.none" /></a></div>
                </div>
                <script>
                    $(document).ready(
                        function () {
                            $(".bulkRemover a.bulkSelectAll").unbind("click").on("click", function (e) {
                                e.preventDefault();
                                $(".bulkRemover input").prop("checked", true);
                            });
                            $(".bulkRemover a.bulkSelectNone").unbind("click").on("click", function (e) {
                                e.preventDefault();
                                $(".bulkRemover input").prop("checked", false);
                            });
                        }
                    );
                </script>
            </jstl:when>
            <jstl:otherwise>
                <span><spring:message code="editor.actions.modal.bulk_remove_bind.no_msg" /></span>
            </jstl:otherwise>
        </jstl:choose>
    </div>

    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss"><spring:message code="app.modal.close" /></a>
        <a class="btn btn-primary continue"><spring:message code="action.ok"/></a>
    </div>
</ideas:app-modal-dialog>