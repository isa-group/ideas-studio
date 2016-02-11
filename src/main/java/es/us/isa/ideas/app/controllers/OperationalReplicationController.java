package es.us.isa.ideas.app.controllers;

import static es.us.isa.ideas.app.controllers.FileController.initRepoLab;
import es.us.isa.ideas.app.entities.OperationalReplication;
import es.us.isa.ideas.app.services.OperationalReplicationService;
import es.us.isa.ideas.app.services.WorkspaceService;
import es.us.isa.ideas.repo.impl.fs.FSFacade;
import java.util.Collection;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/replications")
public class OperationalReplicationController {
    
    @Autowired
    OperationalReplicationService operationalReplicationService;
    @Autowired
    WorkspaceService workspaceService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Collection<OperationalReplication> list() {
        Collection<OperationalReplication> exc = operationalReplicationService.findAll();
        return exc;
    }
    
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public String createOperationalReplicationLink( @RequestParam("workspace") String workspaceName,
                                                    @RequestParam("operation") String operationCode,
                                                    @RequestParam("file") String fileUri,
                                                    @RequestParam("data") String dataUri,
                                                    @RequestParam("params") String auxParams){
        
        String operationUUID = UUID.randomUUID().toString();
        
        OperationalReplication or = operationalReplicationService.createExperimentalExecution(operationUUID, workspaceName, operationCode, fileUri, dataUri, auxParams);
        
        if(or ==null){
            operationUUID="ERROR";
        }
    
        return operationUUID;
    }
    
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView executeExperimentalExecution(@PathVariable("uuid") String uuid){
        
        ModelAndView result = new ModelAndView("operationalReplication");
        
        OperationalReplication eExec = operationalReplicationService.findByUUID(uuid);
        try{
            initRepoLab();
            
            String operationCode = eExec.getOperation();
            String fileContent = FSFacade.getFileContent(eExec.getFileUri(), "DemoMaster");
            
            String data = "";
            if (eExec.getDataUri()!=null && !eExec.getDataUri().equals("")){
                data = FSFacade.getFileContent(eExec.getDataUri(), "DemoMaster");
            }
           
            String params = eExec.getAuxParams();
            
            result.addObject("operation", operationCode);
            result.addObject("workspace", eExec.getFileUri().substring(0, eExec.getFileUri().indexOf("/")));
            result.addObject("content", fileContent);
            result.addObject("fileUri", eExec.getFileUri());
            result.addObject("data", data);
            result.addObject("params", params);
             
        }
        catch (Exception e){
            System.out.println("Error FSFACADE!!");
        }
            
        return result;
    }
}
