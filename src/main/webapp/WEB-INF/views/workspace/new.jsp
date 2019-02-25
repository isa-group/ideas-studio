<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="tab-content">
    <div class="tab-pane fade" id="profileTabContent">
	<div id="workspaceEditForm">
            <form:hidden path="id" />
            <form:hidden path="version" />
            <div>
                <div class="control-group">
                    <div class="input-group">
                        <span class="input-group-addon"> 
                            <spring:message code="workspace.name" />
                        </span>
                        <form:input path="name" 
                                    placeholder="Name" 
                                    type="text"
                                    class="form-control" 
                                    autofocus="true"/>
                    </div>
                    <span class="label label-danger">
                        <form:errors path="name" />
                    </span>
                </div>
                <div class="control-group">
                    <div class="input-group">
                        <span class="input-group-addon"> 
                            <spring:message code="workspace.description" />
                        </span>
                        <form:input path="description" 
                                    placeholder="Description"
                                    type="text" 
                                    class="form-control" />
                    </div>
                    <span class="label label-danger">
                        <form:errors path="description" />
                    </span>
                </div>

                <div class="control-group">
                    <div class="input-group">
                        <span class="input-group-addon"> 
                            <spring:message code="workspace.tags" />
                        </span>
                        <form:input path="tags" 
                                    placeholder="Tags (comma separated)" 
                                    type="text"
                                    class="form-control" />
                    </div>
                    <span class="label label-danger">
                        <form:errors path="tags" />
                    </span>
                </div>
            </div>
            <button type="button" id="saveWS" class="btn btn-primary">
                <spring:message code="workspace.save" />
            </button>
        </div>
    </div>
</div>