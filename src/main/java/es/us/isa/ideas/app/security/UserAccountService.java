package es.us.isa.ideas.app.security;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
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
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author japarejo
 */
@Service
@Transactional
public class UserAccountService extends BusinessService<UserAccount> {

    public static final String USERNAME_DISAMBIGUATION_SUFIX = "1";

    public static final Authority DEFAULT_AUTHORITY = Authority.get(Authority.RESEARCHER);

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

    public UserAccount create(Integer id, String username, String password, String notificationEmail) {

        UserAccount uac_res = null;
        PasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
        if (id != null) {
            UserAccount uac = findById(id);

            uac.setUsername(username);
            uac.setPassword(encoder.encode(password));
            uac.addAuthority(DEFAULT_AUTHORITY);
            uac_res = userRepository.save(uac);

            if (notificationEmail != null && !notificationEmail.isEmpty()) {
                sendWelcomeMail(notificationEmail, uac_res, password);
            }
        } else {
            UserAccount uac = new UserAccount();
            uac.setUsername(username);
            uac.setPassword(encoder.encode(password));
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
        if (userProfile.getEmail() != null) {
            researcher.setEmail(userProfile.getEmail());
        } else {
            researcher.setEmail(userProfile.getUsername() + "@unknown-email.com");
        }
        researcher.setAddress("Unknown address");
        researcher.setPhone("Unknown phone");
        if (null == userProfile.getFirstName() || userProfile.getFirstName().isEmpty()) {
            researcher.setName(userProfile.getUsername());
        } else if (null != userProfile.getLastName()) {
            researcher.setName(userProfile.getFirstName() + " " + userProfile.getLastName());
        } else {
            researcher.setName(userProfile.getFirstName());
        }

        UserAccount result = create(null, generateUsername(researcher), UUID.randomUUID().toString(), researcher.getEmail());

        researcher.setUserAccount(result);
        researcherService.save(researcher);

        // TODO
        createDemoWorkspace(researcher.getUserAccount().getUsername());

        return result;
    }

    public UserAccount create(Actor actor) {
        return create(actor.getUserAccount().getId(), generateUsername(actor), UUID.randomUUID().toString(), actor.getEmail());
    }

    public String generateUsername(Actor actor) {
        String result = actor.getEmail().substring(0, actor.getEmail().indexOf("@"));

        while (findByUsername(result) != null) {
            result += USERNAME_DISAMBIGUATION_SUFIX;
        }
        return result;
    }

    private void sendWelcomeMail(String email, UserAccount account, String password) {
        Object[] templateCustomizers = {account};
        Map<String, String> finalCustomizations = mailer.extractCustomizations(templateCustomizers);
        finalCustomizations.put("$password", password);
        mailer.sendMail(email, replaceNullValues(finalCustomizations, ""), confirmationDoneTemplate);
    }

    private <K, T> Map<K, T> replaceNullValues(Map<K, T> map, T defaultValue) {
        map = map.entrySet().stream().map((entry) -> {
            if (entry.getValue() == null) {
                entry.setValue(defaultValue);
            }

            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return map;
    }

    public void resetPassword(UserAccount account, String notificationEmail) {
        String password = UUID.randomUUID().toString();
        PasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
        account.setPassword(encoder.encode(password));
        userRepository.save(account);
        if (notificationEmail != null && !notificationEmail.isEmpty()) {
            sendPasswordResetEmail(notificationEmail, account, password);
        }
    }

    private void sendPasswordResetEmail(String email, UserAccount account, String password) {
        Object[] templateCustomizers = {account};
        Map<String, String> finalCustomizations = mailer.extractCustomizations(templateCustomizers);
        finalCustomizations.put("$password", password);
        mailer.sendMail(email, finalCustomizations, resetPasswordDoneTemplate);
    }

    public void modifyPassword(UserAccount userAccount, String oldPass, String newPass) {

        PasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
        String passhash = encoder.encode(oldPass);
        String newpasshash = encoder.encode(newPass);
        UserAccount oldUserAccount = userRepository.findByUsername(userAccount.getUsername());

        if (encoder.matches(oldPass, oldUserAccount.getPassword())) {
            userAccount.setPassword(newpasshash);
            // userRepository.save(userAccount); // Fallo con candado
        } else {
            throw new InvalidParameterException("The value of the old password is wrong.");
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

        } catch (AuthenticationException e) {
            System.out.println("### Error creating demo WS for " + username);
            e.printStackTrace();
        }
    }

}
