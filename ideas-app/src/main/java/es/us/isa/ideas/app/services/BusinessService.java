/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.services;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.isa.ideas.app.entities.DomainEntity;
    
/**
 *
 * @author japarejo
 */
public abstract class BusinessService<X extends DomainEntity> {
    public X findById(int id)
    {        
        return getRepository().findOne(id);
    }

    protected abstract JpaRepository<X,Integer> getRepository();
    
}
