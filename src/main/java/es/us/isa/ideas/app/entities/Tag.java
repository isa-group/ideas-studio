package es.us.isa.ideas.app.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author danyal
 */
@Entity
@Access(AccessType.PROPERTY)
public class Tag extends DomainEntity implements Serializable {
    
    @NotBlank
    private String name;
    
    private Collection<Workspace> workspaces;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "workspaceTags")
    private Collection<Workspace> getWorkspaces(){
        return this.workspaces;
    }
    
    private void setWorkspaces(Collection<Workspace> wss){
         this.workspaces=wss;
    }
    
}
    
    