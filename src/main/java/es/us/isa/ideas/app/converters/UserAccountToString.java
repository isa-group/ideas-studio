/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.converters;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.us.isa.ideas.app.security.UserAccount;

/**
 *
 * @author japarejo
 */
@Component
@Transactional
public class UserAccountToString extends DomainEntityToString<UserAccount> {}

