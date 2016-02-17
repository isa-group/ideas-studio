package es.us.isa.ideas.app.security;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.isa.ideas.app.controllers.FileController;
import es.us.isa.ideas.app.controllers.WorkspaceController;
import es.us.isa.ideas.app.entities.Actor;
import es.us.isa.ideas.app.entities.Researcher;
import es.us.isa.ideas.app.entities.Workspace;
import es.us.isa.ideas.app.mail.CustomMailer;
import es.us.isa.ideas.app.mail.TemplateMail;
import es.us.isa.ideas.app.repositories.WorkspaceRepository;
import es.us.isa.ideas.app.services.BusinessService;
import es.us.isa.ideas.app.services.ResearcherService;
import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.impl.fs.FSFacade;
import es.us.isa.ideas.repo.impl.fs.FSWorkspace;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author japarejo
 */
@Service
@Transactional
public class UserAccountService extends BusinessService<UserAccount> {

	public static final String USERNAME_DISAMBIGUATION_SUFIX = "1";

	public static final Authority DEFAULT_AUTHORITY = Authority
			.get(Authority.RESEARCHER);

	@Autowired
	UserAccountRepository userRepository;

	@Autowired
	ResearcherService researcherService;
        
        @Autowired
	WorkspaceRepository workspaceRepository;

	@Autowired
	CustomMailer mailer;

	@Autowired
	ConnectionRepository connectionRepository;

	@Autowired
	@Qualifier("registrationDoneTemplate")
	TemplateMail confirmationDoneTemplate;

	@Autowired
	@Qualifier("resetPasswordDoneTemplate")
	TemplateMail resetPasswordDoneTemplate;

	public Collection<UserAccount> findAll() {
		return userRepository.findAll();
	}

	public UserAccount findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public void save(UserAccount userAccount) {
		userRepository.saveAndFlush(userAccount);
	}

	void delete(UserAccount account) {
		userRepository.delete(account);
	}

	public UserAccount create(Integer id, String username, String password,
			String notificationEmail) {
		
		UserAccount uac_res = null;
		
		if (id != null) {
			UserAccount uac = findById(id);
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			uac.setUsername(username);
			uac.setPassword(encoder.encodePassword(password, null));
			uac.addAuthority(DEFAULT_AUTHORITY);
			uac_res = userRepository.save(uac);

			if (notificationEmail != null && !notificationEmail.isEmpty()) {
				sendWelcomeMail(notificationEmail, uac, password);
			}
		} else {
			UserAccount uac = new UserAccount();
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			uac.setUsername(username);
			uac.setPassword(encoder.encodePassword(password, null));
			uac.addAuthority(DEFAULT_AUTHORITY);
			uac_res = userRepository.save(uac);
		}

		// TODO
		createDemoWorkspace(username);

		return uac_res;
	}

	public UserAccount create(Connection<?> connection) {
		UserProfile userProfile = connection.fetchUserProfile();
		Researcher researcher = new Researcher();
		researcher.setName(userProfile.getUsername());
		if (userProfile.getEmail() != null)
			researcher.setEmail(userProfile.getEmail());
		else
			researcher.setEmail(userProfile.getUsername()
					+ "@unknown-email.com");
		researcher.setAddress("Unknown");
		researcher.setPhone("Unknown");
		researcher.setName(userProfile.getFirstName() + " "
				+ userProfile.getLastName());
		
		UserAccount result = create(null, generateUsername(researcher),
				UUID.randomUUID().toString(), researcher.getEmail());
		
		researcher.setUserAccount(result);
		researcherService.save(researcher);

		// TODO
		createDemoWorkspace(researcher.getUserAccount().getUsername());

		return result;
	}

	public UserAccount create(Actor actor) {
		return create(actor.getUserAccount().getId(), generateUsername(actor),
				UUID.randomUUID().toString(), actor.getEmail());
	}

	public String generateUsername(Actor actor) {
		String result = actor.getEmail().substring(0,
				actor.getEmail().indexOf("@"));

		while (findByUsername(result) != null) {
			result += USERNAME_DISAMBIGUATION_SUFIX;
		}
		return result;
	}

	private void sendWelcomeMail(String email, UserAccount account,
			String password) {
		Object[] templateCustomizers = { account };
		Map<String, String> finalCustomizations = mailer
				.extractCustomizations(templateCustomizers);
		finalCustomizations.put("$password", password);
		mailer.sendMail(email, finalCustomizations, confirmationDoneTemplate);
	}

	public void resetPassword(UserAccount account, String notificationEmail) {
		String password = UUID.randomUUID().toString();
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		account.setPassword(encoder.encodePassword(password, null));
		userRepository.save(account);
		if (notificationEmail != null && !notificationEmail.isEmpty()) {
			sendPasswordResetEmail(notificationEmail, account, password);
		}
	}

	private void sendPasswordResetEmail(String email, UserAccount account,
			String password) {
		Object[] templateCustomizers = { account };
		Map<String, String> finalCustomizations = mailer
				.extractCustomizations(templateCustomizers);
		finalCustomizations.put("$password", password);
		mailer.sendMail(email, finalCustomizations, resetPasswordDoneTemplate);
	}

	public void modifyPassword(UserAccount userAccount, String oldPass,
			String newPass) {

		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		String passhash = encoder.encodePassword(oldPass, null);
		String newpasshash = encoder.encodePassword(newPass, null);
		UserAccount oldUserAccount = userRepository.findByUsername(userAccount
				.getUsername());

		if (passhash.equals(oldUserAccount.getPassword())) {
			userAccount.setPassword(newpasshash);
			// userRepository.save(userAccount); // Fallo con candado
		} else {
			throw new InvalidParameterException(
					"The value of the old password is wrong.");
		}
	}

	@Override
	protected JpaRepository<UserAccount, Integer> getRepository() {
		return userRepository;
	}

	// TODO: For demo (7/3/14)
	
	public void createDemoWorkspace(String username) {
		System.out.println("### Creating demo WS for " + username);
		FSWorkspace demoWS = new FSWorkspace("SampleWorkspace", "DemoMaster");
		FSWorkspace newWS = new FSWorkspace("SampleWorkspace", username);
		try {
			FileController.initRepoLab();
			IdeasRepo.get().getRepo().move(demoWS, newWS, true);
                        if(workspaceRepository.findByName("SampleWorkspace", "DemoMaster")==null){
                            Workspace wsdemo = new Workspace();
                            wsdemo.setName("SampleWorkspace");
                            wsdemo.setOwner(researcherService.findByUserName("DemoMaster"));
                            wsdemo.setWsVersion(0);
                            workspaceRepository.save(wsdemo);
                            try {
                                FSFacade.saveSelectedWorkspace("SampleWorkspace", "DemoMaster");
                            } catch (IOException ex) {
                                Logger.getLogger(UserAccountService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if(!username.startsWith("demo")){
                            Workspace ws = new Workspace();
                        ws.setName("SampleWorkspace");
                        ws.setWsVersion(0);
                        ws.setOwner(researcherService.findByUserName(username));
                        workspaceRepository.save(ws);
                            try {
                                FSFacade.saveSelectedWorkspace("SampleWorkspace", username);
                            } catch (IOException ex) {
                                Logger.getLogger(UserAccountService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        
		} catch (AuthenticationException e) {
			System.out.println("### Error creating demo WS for " + username);
			e.printStackTrace();
		}
	}

}
