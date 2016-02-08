package es.us.isa.ideas.app.controllers;

import es.us.isa.ideas.app.entities.OperationalReplication;
import es.us.isa.ideas.app.services.OperationalReplicationService;
import es.us.isa.ideas.app.services.WorkspaceService;
import java.util.Collection;
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
    OperationalReplicationService experimentalExecutionService;
    @Autowired
    WorkspaceService workspaceService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Collection<OperationalReplication> list() {
        Collection<OperationalReplication> exc = experimentalExecutionService.findAll();
        return exc;
    }
    
    @RequestMapping(value = "",method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ModelAndView createExperimentalExecution(   @RequestParam("workspace") String workspaceName,
                        @RequestParam("operation") String operationCode,
                        @RequestParam("file") String fileUri,
                        @RequestParam("data") String dataUri,
                        @RequestParam("params") String auxParams){
        
        experimentalExecutionService.createExperimentalExecution(workspaceName, operationCode, fileUri, dataUri, auxParams);
    
        return new ModelAndView();
    }
    
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView executeExperimentalExecution(@PathVariable("id") String id){
        
        ModelAndView result = new ModelAndView("operationalReplication");
        
        OperationalReplication eExec = experimentalExecutionService.findById(Integer.parseInt(id));
        try{
            
            String operationCode = eExec.getOperation();
            String fileContent = "";//FSFacade.getFileContent(eExec.getFileUri(), "DemoMaster");
            String data = "";//FSFacade.getFileContent(eExec.getDataUri(), "DemoMaster");
            String params = eExec.getAuxParams();
            
            result.addObject("id", operationCode);
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
