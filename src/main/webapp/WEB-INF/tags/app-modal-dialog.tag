<%@tag description="Layout for the app modal dialog" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- Modal for all app -->
<div class="modal" id="appGenericModal" data-backdrop="true" data-keyboard="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <jsp:doBody/>
        </div>
    </div>
</div>
             