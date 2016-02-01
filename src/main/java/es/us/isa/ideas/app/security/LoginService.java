package es.us.isa.ideas.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class LoginService implements UserDetailsService {
	


	@Autowired
	UserAccountRepository userRepository;
	

    @Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Assert.notNull(username);

		UserDetails result;

		result = userRepository.findByUsername(username);
		Assert.notNull(result);		
		// WARNING: The following sentences prevent lazy initialisation problems!
		Assert.notNull(result.getAuthorities());
		result.getAuthorities().size();

		return result;
	}

	public static UserAccount getPrincipal() {
		UserAccount result=null;
		SecurityContext context;
		Authentication authentication;
		Object principal;

		// If the asserts in this method fail, then you're
		// likely to have your Tomcat's working directory
		// corrupt. Please, clear your browser's cache, stop
		// Tomcat, update your Maven's project configuration,
		// clean your project, clean Tomcat's working directory,
		// republish your project, and start it over.

		context = SecurityContextHolder.getContext();
		Assert.notNull(context);
		authentication = context.getAuthentication();
		Assert.notNull(authentication);
		principal = authentication.getPrincipal();
		//Assert.isTrue();
		if(principal instanceof UserAccount)
                    result = (UserAccount) principal;
		//Assert.notNull(result);
		//Assert.isTrue(result.getId() != 0);

		return result;
	}
        
        public static void setPrincipal(UserAccount account)
        {
            SecurityContext context;
            context = SecurityContextHolder.getContext();
            Assert.notNull(context);                        
            
            Authentication authentication=new UsernamePasswordAuthenticationToken(account,null);
            
            context.setAuthentication(authentication);            
        }

}
