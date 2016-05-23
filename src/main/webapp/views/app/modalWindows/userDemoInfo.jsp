<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div class="modal-header">
    <img id="imgLogoModal" src="/img/${studioConfiguration.images.logo}">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <%--     <h4 class="modal-title"><spring:message code="editor.actions.modal.userDemoInfo.title"/></h4> --%>

</div>
<div class="modal-body">
    <div class="input-group" id="modalCreationField" >
        <p class="">This is a <b>anonymous session</b> of ideas without persistence and <b>all your changes will be</b></p>
        <p class=""><b>lost when you close the your browser or spend some time without activity</b>. If you are</p>
        <p class="">interested in a persistent workspace please consider <a data-dismiss="modal" onclick="location.href = 'j_spring_security_logout'" href="j_spring_security_logout" target="_self">register</a>.</p>
    </div>
</div>
<div class="modal-footer">
    <!--     <a data-dismiss="modal" class="btn dismiss" href="j_spring_security_logout" target="_self">Register</a> -->
    <a class="btn btn-primary continue">OK</a>
</div> 

<style>
    #imgLogoModal {
        width: 12em;
    }
</style>