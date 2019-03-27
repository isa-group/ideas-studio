<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<ideas:app-template>
    <!DOCTYPE html>
    <html lang="en">

        <script>
            $(function () {
                $("#versions").hide();
            });
            WorkspaceManager.setSelectedWorkspace($('#name').val());
        </script>

        <div class="container">

            <h2>Edit Workspace</h2>
            <br/>

            <spring:url value="app/wsm/workspaces" var="wsActionUrl" />

            <form:form class="form-horizontal" method="put" modelAttribute="wsForm" action="${wsActionUrl}">

                <form:hidden path="id" />
                <div class="control-group">
                    <spring:bind path="name">
                        <div class="input-group ${status.error ? 'has-error' : ''}">
                            <span class="input-group-addon">Name</span>

                            <form:input path="name" type="text" class="form-control" 
                                        id="name" placeholder="Name" />
                            <form:errors path="name" class="control-label" />

                        </div>
                    </spring:bind>
                </div>
                <br />
                <div class="control-group">
                    <spring:bind path="description">
                        <div class="input-group ${status.error ? 'has-error' : ''}">
                            <span class="input-group-addon">Description</span>

                            <form:textarea path="description" class="form-control" 
                                           id="description" placeholder="Description" />
                            <form:errors path="description" class="control-label" />

                        </div>
                    </spring:bind>
                </div>
                <br />
                <!--TODO: TAGS-->
                <button type="button" id="saveWS" class="btn btn-primary pull-right">Save</button>
                <script>
                    $(function () {
                        $("#saveWS").click(function (callback) {
                            WorkspaceManager.updateWorkspace(
                                    $('#name').val(),
                                    $('#description').val(),
                                    callback);
                        });
                    });
                </script>
                <a href="/IDEAS/app/wsm" target="_self" type="submit" class="btn pull-right" style="margin-right: 4px;">Back</a>

            </form:form>

        </div>

    </body>
</html>
</ideas:app-template>