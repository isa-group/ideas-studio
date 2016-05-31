<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!-- Modal for all app -->
<div class="modal" id="appGenericModal" data-backdrop="true" data-keyboard="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <tiles:insertAttribute name="body"/>
        </div>
    </div>
</div>
             