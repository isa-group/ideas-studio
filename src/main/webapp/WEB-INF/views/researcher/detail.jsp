
<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>


<script>
    jQuery(function () {
        $('#detailsForm input').on("change paste keyup", function () {
            formsChanges = true;
            $("#settingsSubmitChanges").prop('disabled', false);
            $("#settingsRevertChanges").prop('disabled', false);
        });

        var detailsForm = $('#detailsForm');
        detailsForm.submit(function () {
            alert(currentId);
            $.ajax({
                type: detailsForm.attr('method'),
                url: detailsForm.attr('action') + "?userId=" + currentId,
                data: detailsForm.serialize(),
                success: function (result) {
                    formResponseReceived(result);
                }
            });
            return false;
        });
    });
</script>

<form:form id="detailsForm" modelAttribute="researcher" action="${url}">

    <div id="Authorities">
        <jstl:forEach items="${researcher.userAccount.authorities}"
                      var="authority" varStatus="status">
            <form:hidden path="userAccount.authorities[${status.index}]" />
        </jstl:forEach>
    </div>

    <div class="control-group">
        <div class="input-group">
            <span class="input-group-addon"><spring:message
                    code="security.username" /></span>
                <form:input path="userAccount.username" placeholder="myusername"
                            type="text" class="form-control" />
        </div>
        <span class="label label-danger"><form:errors
                path="userAccount.username" /></span>
    </div>


    <div class="control-group">
        <div class="input-group">
            <span class="input-group-addon"><spring:message
                    code="security.password" /></span>
                <form:password id="mypass" name="mypass" class="form-control"
                               path="userAccount.password" />
        </div>
        <span class="label label-danger"><form:errors
                path="userAccount.password" /></span>
    </div>




    <BR />

    <form:hidden path="id" />
    <form:hidden path="version" />
    <form:hidden path="userAccount" />
    <div>
        <div class="control-group">
            <div class="input-group">
                <span class="input-group-addon"> <spring:message
                        code="researcher.name" />
                </span>
                <form:input path="name" placeholder="Name Surname" type="text"
                            class="form-control" />
            </div>
            <span class="label label-danger"><form:errors path="name" /></span>
        </div>

        <div class="control-group">
            <div class="input-group">
                <span class="input-group-addon"> <spring:message
                        code="researcher.email" />
                </span>
                <form:input path="email" placeholder="myname@domain.com"
                            type="email" class="form-control" />
            </div>
            <span class="label label-danger"><form:errors path="email" /></span>
        </div>

        <div class="control-group">
            <div class="input-group">
                <span class="input-group-addon"> <spring:message
                        code="researcher.phone" />
                </span>
                <form:input path="phone" placeholder="" type="text"
                            class="form-control" />
            </div>
            <span class="label label-danger"><form:errors path="phone" /></span>
        </div>

        <div class="control-group">
            <div class="input-group">
                <span class="input-group-addon"> <spring:message
                        code="researcher.address" />
                </span>
                <form:input path="address" placeholder="" type="text"
                            class="form-control" />
            </div>
            <span class="label label-danger"><form:errors path="address" /></span>
        </div>
    </div>

</form:form>