/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import es.us.isa.ideas.app.security.UserAccount;
import es.us.isa.ideas.app.security.UserAccountService;
import es.us.isa.ideas.app.services.BusinessService;

/**
 *
 * @author japarejo
 */
public class StringToUserAccount extends StringToDomainEntity<UserAccount> implements Converter<String,UserAccount>{
    @Autowired
    UserAccountService service;

    @Override
    public BusinessService<UserAccount> getBusinessService() {
        return service;
    }
        
    
}
