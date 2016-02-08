
package es.us.isa.ideas.app.services;

import es.us.isa.ideas.app.entities.OperationalReplication;
import es.us.isa.ideas.app.entities.Workspace;
import es.us.isa.ideas.app.repositories.OperationalReplicationRepository;
import es.us.isa.ideas.app.repositories.WorkspaceRepository;
import java.util.Calendar;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional(readOnly = false)
public class OperationalReplicationService extends BusinessService<OperationalReplication>{
    
    @Inject
    private OperationalReplicationRepository experimentalExecutionRepository;
    @Inject
    private WorkspaceRepository workspaceRepository;
    
    private static final String DEMO_MASTER = "DemoMaster";
    
    
    public OperationalReplication createExperimentalExecution(String workspaceName, 
                                            String operationCode, 
                                            String fileUri,
                                            String dataUri,
                                            String params){
        Assert.notNull(workspaceName);
        Assert.notNull(operationCode);
        Assert.notNull(fileUri);
        
        OperationalReplication res = null;
        Workspace demoWS = workspaceRepository.findByName(workspaceName, DEMO_MASTER);

        boolean existDemoWorkspace = (demoWS != null);
        
        if (existDemoWorkspace){
            OperationalReplication eExec =  new OperationalReplication();
            eExec.setWorkspace(demoWS);
            eExec.setCreationDate(Calendar.getInstance().getTime());
            eExec.setOperation(operationCode);
            eExec.setFileUri(fileUri);
            eExec.setDataUri(dataUri);
            eExec.setLaunches(0);  
            res = experimentalExecutionRepository.saveAndFlush(eExec);
        }
        
        return res;
    }
    
    public Collection<OperationalReplication> findAll() {
        return experimentalExecutionRepository.findAll();
    }

    @Override
    protected JpaRepository<OperationalReplication, Integer> getRepository() {
        return experimentalExecutionRepository;
    }
    
    public String calculateExtFromFileUri(String fileUri) {
        String extension = "";
        Matcher m = Pattern.compile(".*/.*?(\\..*)").matcher(fileUri);//Alt /(?:\.([^.]+))?$/
        if (m.matches()) {
            extension = m.group(1);
        }
        return extension;
    }

}
