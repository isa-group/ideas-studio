package es.us.isa.ideas.app.social;

import es.us.isa.ideas.app.security.UserAccount;
import es.us.isa.ideas.app.security.UserAccountService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author japarejo
 */
@Component
public class SocialSignInAdapter implements SignInAdapter {

    @Autowired
    private UserAccountService userAccountService;	
    
    @Autowired
    private SignInUtils signInUtils;        
    

    @Autowired
    private ConnectionRepository connectionRepository;
    
    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        String result="/app/editor";
        UserAccount userAccount=signInUtils.signin(localUserId,connection.getKey().getProviderId());
        if(userAccount==null){
            if (connection != null) {
                     result=createAccountAndSignIn(connection,request);
		} else {                    
                        result="redirect:/researcher/create";
		}                
	}
        return result;
    }

    private String createAccountAndSignIn(Connection<?> connection, WebRequest request) {
        String result=null;        
        UserAccount userAccount=userAccountService.create(connection);                
        signInUtils.signin(userAccount.getUsername(), connection.getKey().getProviderId());
        connectionRepository.addConnection(connection);                
        return "redirect:/connect/\"+connection.getKey().getProviderId()";
            
    }
    

    private void removeAutheticationAttributes(HttpSession session) {
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
