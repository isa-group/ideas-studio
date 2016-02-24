
package es.us.isa.ideas.app.services;

import es.us.isa.ideas.app.entities.OperationalReplication;
import es.us.isa.ideas.app.entities.Workspace;
import es.us.isa.ideas.app.repositories.OperationalReplicationRepository;
import es.us.isa.ideas.app.repositories.WorkspaceRepository;
import java.util.Calendar;
import java.util.Collection;
import javax.inject.Inject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional(readOnly = false)
public class OperationalReplicationService extends BusinessService<OperationalReplication>{
    
    @Inject
    private OperationalReplicationRepository operationalReplicationRepository;
    @Inject
    private WorkspaceRepository workspaceRepository;
     
    private static final String DEMO_MASTER = "DemoMaster";
    
    
    public OperationalReplication createExperimentalExecution(
                                            String operationUUID,
                                            String workspaceName, 
                                            String operationCode, 
                                            String fileUri,
                                            String auxArg0, String auxArg1, String auxArg2, String auxArg3, String auxArg4){
        Assert.notNull(operationUUID);
        Assert.notNull(workspaceName);
        Assert.notNull(operationCode);
        Assert.notNull(fileUri);
        
        OperationalReplication res = null;
        Workspace demoWS = workspaceRepository.findByName(workspaceName, DEMO_MASTER);

        boolean existDemoWorkspace = (demoWS != null);
        
        if (existDemoWorkspace){
            OperationalReplication eExec =  new OperationalReplication();
            eExec.setUUID(operationUUID);
            eExec.setWorkspace(demoWS);
            eExec.setCreationDate(Calendar.getInstance().getTime());
            eExec.setOperationName(operationCode);
            eExec.setFileUri(fileUri);
            eExec.setAuxArg0(auxArg0);
            eExec.setAuxArg1(auxArg1);
            eExec.setAuxArg2(auxArg2);
            eExec.setAuxArg3(auxArg3);
            eExec.setAuxArg4(auxArg4);
            eExec.setLaunches(0);  
            res = operationalReplicationRepository.saveAndFlush(eExec);
        }
        
        return res;
    }
    
    public OperationalReplication findByUUID(String operationUUID) {
        return operationalReplicationRepository.findByUUID(operationUUID);
    }
    
    public Collection<OperationalReplication> findAll() {
        return operationalReplicationRepository.findAll();
    }

    @Override
    protected JpaRepository<OperationalReplication, Integer> getRepository() {
        return operationalReplicationRepository;
    }
    
}
