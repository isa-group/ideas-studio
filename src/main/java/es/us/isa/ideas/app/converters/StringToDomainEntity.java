
package es.us.isa.ideas.app.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.us.isa.ideas.app.entities.DomainEntity;
import es.us.isa.ideas.app.services.BusinessService;

/**
 *
 * @author japarejo
 */

@Component
@Transactional
public abstract class StringToDomainEntity<X extends DomainEntity> implements Converter<String, X>
{            
    @Override
    public X convert(String text) {
        X result;
        int id;

        try {
            id = Integer.valueOf(text);
            result = getBusinessService().findById(id);
        } catch (Throwable oops) { 
            throw new IllegalArgumentException(oops); 
        }
        return result;
    }

    public abstract BusinessService<X> getBusinessService();    
    
    
}
