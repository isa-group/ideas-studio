/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.entities;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author japarejo
 */
@Entity
@Access(AccessType.PROPERTY)
public class Confirmation extends DomainEntity {
    
    private Date registrationDate;
    private Date confirmationDate;
    private String confirmationCode;
    
    private Researcher researcher;

    /**
     * @return the registrationDate
     */
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @param registrationDate the registrationDate to set
     */
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * @return the confirmationDate
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getConfirmationDate() {
        return confirmationDate;
    }

    /**
     * @param confirmationDate the confirmationDate to set
     */
    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    /**
     * @return the confimationCode
     */
    @NotBlank
    @Column(unique=true)
    public String getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * @param confimationCode the confimationCode to set
     */
    public void setConfirmationCode(String confimationCode) {
        this.confirmationCode = confimationCode;
    }

    /**
     * @return the researcher
     */
    @NotNull
    @Valid    
    @OneToOne(optional=false)
    public Researcher getResearcher() {
        return researcher;
    }

    /**
     * @param researcher the researcher to set
     */
    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }
    
    
    
    
}
