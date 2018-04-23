<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h4 class="modal-title"><spring:message code="editor.actions.modal.create_language_file.title" arguments="${language}" /></h4>
</div>
<div class="modal-body">
    <div class="input-group" id="modalCreationField" >
        <span class="input-group-addon input-modal"><spring:message code="editor.actions.modal.create_language_file.msg" arguments="${language}" /></span>
        <input id="filename" type="text" class="modalCreationField form-control focusedInput" onKeypress="checkerName(event)">
        <span class="input-group-addon extension"></span>
    </div>
    <div class="input-group" id="createFileFromTemplate">
        <h4> <spring:message code="editor.actions.modal.create_language_file.from_template" /></h4>
        <select id="template-file" name="template-file" class="form-control">
            <option id="module-empty" value=""><spring:message code="editor.actions.modal.create_language_file.no_template" /></option>                            
        </select>        
    </div>
</div>
<div class="modal-footer">
    <a data-dismiss="modal" class="btn dismiss">Close</a>
    <a class="btn btn-primary continue"> <spring:message code="editor.actions.modal.create_language_file.button" /></a>
</div>  
<script>
    	var mytemplates,mytemplate,index;              
        var language=ModeManager.getMode('${languageId}');
        console.dir(language);
        if(language.defaultFileName)
            $("#filename").val(language.defaultFileName);
        $.ajax({"url": '${studioConfiguration.modules[languageId]}/template/document',
        success: function(result, textStatus, request) {
                console.log("Templates provided by the ${language} module:" + result + "");
                if (typeof result === 'string' || result instanceof String)
                    mytemplates=$.parseJSON(result);                
                else
                    mytemplates=result;
                if(mytemplates !== null && mytemplates.length != 0){                    
                    for(index=0;index<mytemplates.length;index++){
                        mytemplate=mytemplates[index];
                        $("#template-file").append('<option value="'+mytemplate.name+'"'+(index==0?' selected':'')+'>'+mytemplate.name+'</option>');                                    
                    }
                }else
                    $("#createFileFromTemplate").remove();
            },
        error: function(result, textStatus, request){
        	$("#createFileFromTemplate").remove();
        }
        });
        $(function(){
        	$(".extension").html("."+language.extension);
        });
</script>