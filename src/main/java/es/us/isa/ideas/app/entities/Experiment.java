/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Experiment extends DomainEntity {

    private String experimentId;
    private String experimentName;
    private String description;
    private boolean publicExperiment;
    /*private Date creation;
     private Date lastModification;*/
    private Researcher owner;

    @NotBlank
    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    @NotBlank
    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }    

    public boolean getPublicExperiment() {
        return publicExperiment;
    }

    public void setPublicExperiment(boolean publicExperiment) {
        this.publicExperiment = publicExperiment;
    }
    
    @NotNull
    @Valid
    @ManyToOne(optional = false)
    public Researcher getOwner() {
        return owner;
    }

    public void setOwner(Researcher owner) {
        this.owner = owner;
    }
}
