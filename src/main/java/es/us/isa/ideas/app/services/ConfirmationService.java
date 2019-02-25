package es.us.isa.ideas.app.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import es.us.isa.ideas.app.entities.Actor;
import es.us.isa.ideas.app.entities.Confirmation;
import es.us.isa.ideas.app.entities.Researcher;
import es.us.isa.ideas.app.mail.CustomMailer;
import es.us.isa.ideas.app.mail.TemplateMail;
import es.us.isa.ideas.app.repositories.ConfirmationRepository;
import es.us.isa.ideas.app.security.UserAccountService;
import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author japarejo
 */
@Service
@Transactional
public class ConfirmationService extends BusinessService<Confirmation> {
	
    @Autowired
    ConfirmationRepository repository;

    @Autowired
    CustomMailer mailer;

    @Autowired
    ResearcherService researcherService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    @Qualifier("registrationConfirmationTemplate")
    TemplateMail registrationConfirmationTemplate;

    @Autowired
    @Qualifier("resetPasswordConfirmationTemplate")
    TemplateMail resetPasswordConfirmationTemplate;

    @Autowired
    HttpServletRequest request;

    public Confirmation findByResearcherId(int id) {
        return repository.getByResearcherId(id);
    }

    public Confirmation createRegistrationConfirmation(int researcherId) {
        Researcher researcher = researcherService.findById(researcherId);
        return createRegistrationConfirmation(researcher);
    }

    public Confirmation createRegistrationConfirmation(Actor actor) {

        Assert.notNull(actor);

        Confirmation result = new Confirmation();
        result.setRegistrationDate(new Date());
        result.setConfirmationDate(null);
        result.setResearcher((Researcher) actor);
        result.setConfirmationCode(UUID.randomUUID().toString());

        repository.save(result);

        sendRegistrationConfirmationMail(result, new HashMap<String, String>());
        return result;
    }

    public void delete(Confirmation rc) {
        repository.delete(rc);
    }

    public Confirmation confirmRegistration(String code) {
        try {
            Confirmation confirmation = repository.getByConfirmationCode(code);
            Assert.notNull(confirmation);
            confirmation.setConfirmationDate(new Date());
            repository.save(confirmation);
            userAccountService.create(confirmation.getResearcher());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return repository.getByConfirmationCode(code);
    }

    public void createPasswordResetConfirmation(Actor actor) {

        Confirmation confirmation;
        if (repository.getByResearcherId(actor.getId()) != null) {
            confirmation = repository.getByResearcherId(actor.getId());
        } else {
            confirmation = new Confirmation();
            confirmation.setResearcher((Researcher) actor);
        }
        
        confirmation.setRegistrationDate(new Date());
        confirmation.setConfirmationCode(UUID.randomUUID().toString());
        confirmation.setConfirmationDate(null);
        repository.save(confirmation);
        
        sendPasswordResetConfirmationMail(confirmation,
                new HashMap<String, String>());

    }

    public Confirmation resetPassword(String code) {
        Confirmation confirmation = repository.getByConfirmationCode(code);
        Assert.notNull(code);
        confirmation.setConfirmationDate(new Date());
        repository.save(confirmation);
        userAccountService.resetPassword(confirmation.getResearcher()
                .getUserAccount(), confirmation.getResearcher().getEmail());
        return confirmation;
    }

    private void sendRegistrationConfirmationMail(Confirmation result,
            Map<String, String> customization) {
        Object[] templateCustomizers = {result.getResearcher(), result};
        Map<String, String> finalCustomizations = mailer
                .extractCustomizations(templateCustomizers);
        finalCustomizations.putAll(customization);
        finalCustomizations.put("$confirmationUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/confirm/");
        mailer.sendMail(result.getResearcher().getEmail(), finalCustomizations,
                registrationConfirmationTemplate);
    }

    private void sendPasswordResetConfirmationMail(Confirmation confirmation,
            Map<String, String> customization) {
        Researcher researcher = confirmation.getResearcher();
        Object[] templateCustomizers = {researcher,
            researcher.getUserAccount()};
        Map<String, String> finalCustomizations = mailer
                .extractCustomizations(templateCustomizers);
        finalCustomizations.putAll(customization);
        finalCustomizations.put("$code", confirmation.getConfirmationCode());
        finalCustomizations.put("$confirmationUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/confirm/");
        mailer.sendMail(researcher.getEmail(), finalCustomizations,
                resetPasswordConfirmationTemplate);
    }

    public void createPasswordResetConfirmation(String email) {
        Researcher researcher = researcherService.findByEmail(email);
        Assert.notNull(researcher);
        createPasswordResetConfirmation(researcher);
    }

    @Override
    protected JpaRepository<Confirmation, Integer> getRepository() {
        return repository;
    }

}
