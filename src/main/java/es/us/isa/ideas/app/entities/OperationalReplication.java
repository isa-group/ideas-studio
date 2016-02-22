package es.us.isa.ideas.app.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class OperationalReplication extends DomainEntity implements Serializable {
    
    private String UUID;
    private Workspace workspace;
    private Date creationDate;
    private String operationName;
    private String fileUri;
    private String[] auxParams;
    private Integer launches;
      
    @NotNull
    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    
    @NotNull
    @ManyToOne
    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String[] getAuxParams() {
        return auxParams;
    }

    public void setAuxParams(String[] auxParams) {
        this.auxParams = auxParams;
    }

    public Integer getLaunches() {
        return launches;
    }

    public void setLaunches(Integer launches) {
        this.launches = launches;
    }
        
}