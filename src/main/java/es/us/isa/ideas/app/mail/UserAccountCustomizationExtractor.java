package es.us.isa.ideas.app.mail;

import java.util.HashMap;
import java.util.Map;

import es.us.isa.ideas.app.security.Authority;
import es.us.isa.ideas.app.security.UserAccount;

/**
 *
 * @author japarejo
 */
public class UserAccountCustomizationExtractor implements SpecificCustomizationExtractor<UserAccount> {
    public static final String USERNAME_KEY="$username";
    public static final String PASSWORD_KEY="$password";
    public static final String ROLES_KEY="$roles";

    @Override
    public boolean canExtractCustomizations(Object o) {
        return o instanceof UserAccount;
    }

    @Override
    public Map<String, String> extractCustomizations(UserAccount element) {
        Map<String,String> result=new HashMap<String,String>();
        result.put(USERNAME_KEY, element.getUsername());
        result.put(PASSWORD_KEY, element.getUsername());
        StringBuilder sb=new StringBuilder();
        for(Authority a:element.getAuthorities()){
            sb.append(a);        
            sb.append(" ");
        }
        result.put(ROLES_KEY, sb.toString());
        return result;
    }
    
    
}
