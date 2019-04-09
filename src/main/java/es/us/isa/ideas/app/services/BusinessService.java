package es.us.isa.ideas.app.services;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.isa.ideas.app.entities.DomainEntity;
    
/**
 *
 * @author japarejo
 * @param <X>
 */
public abstract class BusinessService<X extends DomainEntity> {
    public X findById(int id)
    {        
        return getRepository().getOne(id);
    }

    protected abstract JpaRepository<X,Integer> getRepository();
    
}
