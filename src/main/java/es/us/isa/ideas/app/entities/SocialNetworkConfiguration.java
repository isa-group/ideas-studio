package es.us.isa.ideas.app.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author japarejo
 */
@Entity
@Access(AccessType.PROPERTY)
public class SocialNetworkConfiguration extends DomainEntity
{
    
    private String service;
    private Actor actor;
    
    // PUBLICATIONS THROUGH USER PROFILE:
    private boolean publishNewPublicExperimentCreated;
    private boolean publishNewPublicExperimentExecutionStarted;
    private boolean publishNewExperimentExecutionFinished;
    private boolean publishExistingExperimentMadePublic;
    
    // NOTIFICATIONS THROUGH USER ACCOUNT:
    private boolean notifyWhenExperimentExecutionFinished;

    
    @ManyToOne(optional=false)
    public Actor getActor() {
        return actor;
    }    
    
    
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }            
        
    
    public boolean isPublishNewPublicExperimentCreated() {
        return publishNewPublicExperimentCreated;
    }

    
    public void setPublishNewPublicExperimentCreated(boolean publishNewPublicExperimentCreated) {
        this.publishNewPublicExperimentCreated = publishNewPublicExperimentCreated;
    }

    
    public boolean isPublishNewPublicExperimentExecutionStarted() {
        return publishNewPublicExperimentExecutionStarted;
    }

    
    public void setPublishNewPublicExperimentExecutionStarted(boolean publishNewPublicExperimentExecutionStarted) {
        this.publishNewPublicExperimentExecutionStarted = publishNewPublicExperimentExecutionStarted;
    }

    
    public boolean isPublishNewExperimentExecutionFinished() {
        return publishNewExperimentExecutionFinished;
    }

    
    public void setPublishNewExperimentExecutionFinished(boolean publishNewExperimentExecutionFinished) {
        this.publishNewExperimentExecutionFinished = publishNewExperimentExecutionFinished;
    }

    
    public boolean isPublishExistingExperimentMadePublic() {
        return publishExistingExperimentMadePublic;
    }

    
    public void setPublishExistingExperimentMadePublic(boolean publishExistingExperimentMadePublic) {
        this.publishExistingExperimentMadePublic = publishExistingExperimentMadePublic;
    }

    
    public boolean isNotifyWhenExperimentExecutionFinished() {
        return notifyWhenExperimentExecutionFinished;
    }

    
    public void setNotifyWhenExperimentExecutionFinished(boolean notifyWhenExperimentExecutionFinished) {
        this.notifyWhenExperimentExecutionFinished = notifyWhenExperimentExecutionFinished;
    }
    
    
    
}
