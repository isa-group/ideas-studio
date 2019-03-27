//package es.us.isa.ideas.app.services;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import es.us.isa.ideas.app.entities.Confirmation;
//import es.us.isa.ideas.app.entities.Researcher;
//import es.us.isa.ideas.app.mail.CustomMailer;
//import es.us.isa.ideas.app.mail.TemplateMail;
//import es.us.isa.ideas.app.repositories.ConfirmationRepository;
//import es.us.isa.ideas.app.security.UserAccountService;
//
//@Service
//@Transactional
//public class MailService extends BusinessService{
//	
//	@Autowired
//    ConfirmationRepository repository;
//    
//    @Autowired 
//    CustomMailer mailer;
//    
//    @Autowired
//    ResearcherService researcherService;
//    
//    @Autowired
//    UserAccountService userAccountService;            
//    
//    @Autowired
//    //@Qualifier("documentShared")
//    TemplateMail mail;
//    
//    public Confirmation findByResearcherId(int id){
//        return repository.getByResearcherId(id);
//    }
//    
//    public void sendMail(Confirmation result, Map<String,String> customization) {
//        Researcher researcher=result.getResearcher();        
//        Object[] templateCustomizers={result.getResearcher(),result};        
//        Map<String,String> finalCustomizations=mailer.extractCustomizations(templateCustomizers);
//        finalCustomizations.putAll(customization);
//        mailer.sendMail(result.getResearcher().getEmail(),  finalCustomizations, mail);
//    }
//    
//	@Override
//	protected JpaRepository getRepository() {
//		return repository;
//	}
//
//}
