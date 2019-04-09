/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.social;

import es.us.isa.ideas.app.security.UserAccount;
import es.us.isa.ideas.app.security.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

/**
 *
 * @author japarejo
 */
@Service
public class SocialConnectionSignup implements ConnectionSignUp {
 
    @Autowired
    private UserAccountService userAccountService;
 
    @Override
    public String execute(Connection<?> connection) {
        UserAccount userAccount=userAccountService.create(connection);
        return userAccount.getUsername();
    }
}
