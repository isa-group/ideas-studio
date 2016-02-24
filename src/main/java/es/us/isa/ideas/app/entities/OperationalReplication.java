package es.us.isa.ideas.app.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
    private String auxArg0;
    private String auxArg1;
    private String auxArg2; 
    private String auxArg3;
    private String auxArg4;
    private Integer launches;
      
    @NotNull
    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    
    @ManyToOne
    @NotNull
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

    @Lob
    public String getAuxArg0() {
        return auxArg0;
    }

    public void setAuxArg0(String auxArg0) {
        this.auxArg0 = auxArg0;
    }

    @Lob
    public String getAuxArg1() {
        return auxArg1;
    }

    public void setAuxArg1(String auxArg1) {
        this.auxArg1 = auxArg1;
    }

    @Lob
    public String getAuxArg2() {
        return auxArg2;
    }

    public void setAuxArg2(String auxArg2) {
        this.auxArg2 = auxArg2;
    }

    @Lob
    public String getAuxArg3() {
        return auxArg3;
    }

    public void setAuxArg3(String auxArg3) {
        this.auxArg3 = auxArg3;
    }

    @Lob
    public String getAuxArg4() {
        return auxArg4;
    }

    public void setAuxArg4(String auxArg4) {
        this.auxArg4 = auxArg4;
    }

    public Integer getLaunches() {
        return launches;
    }

    public void setLaunches(Integer launches) {
        this.launches = launches;
    }
        
}