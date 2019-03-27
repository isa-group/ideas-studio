/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.configuration;

import es.us.isa.ideas.app.mail.ConfirmationCodeCustomizationExtractor;
import es.us.isa.ideas.app.mail.ConstantCustomizer;
import es.us.isa.ideas.app.mail.CustomizationsExtractor;
import es.us.isa.ideas.app.mail.ResearcherCustomizationExtractor;
import es.us.isa.ideas.app.mail.SpecificCustomizationExtractor;
import es.us.isa.ideas.app.mail.TemplateMail;
import es.us.isa.ideas.app.mail.UserAccountCustomizationExtractor;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/*
 * @author japarejo
 */
@Configuration
public class MailConfiguration {

    @Autowired
    Environment env;

    @Bean()
    public MailSender mailSender() {
        JavaMailSenderImpl result = new JavaMailSenderImpl();
        result.setHost(env.getProperty("mailserver.host"));
        result.setPort(Integer.valueOf(env.getProperty("mailserver.port")));
        result.setProtocol(env.getProperty("mailserver.protocol"));
        result.setUsername(env.getProperty("mailserver.username"));
        result.setUsername(env.getProperty("mailserver.password"));        
        Properties props = new Properties();
        props.setProperty("mail.smtps.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.EnableSSL.enable", "true");
        result.setJavaMailProperties(props);
        return result;
    }
    
    @Bean()
    public CustomizationsExtractor customizationExtractor(){
        ConstantCustomizer cc=new ConstantCustomizer();
        cc.setKey("$confirmationUrl");
        List<SpecificCustomizationExtractor> extractors=new ArrayList<>();
        extractors.add(new ResearcherCustomizationExtractor());
        extractors.add(new UserAccountCustomizationExtractor());
        extractors.add(new ConfirmationCodeCustomizationExtractor());
        extractors.add(cc);
        return new CustomizationsExtractor(extractors);
    }

    @Bean(name = "registrationDoneTemplate")
    public TemplateMail registrationDoneTemplate() {
        return new TemplateMail(
                env.getProperty("mail.template.registrationDone.subject"),
                env.getProperty("mail.template.registrationDone.text")
        );
    }

    @Bean(name = "registrationConfirmationTemplate")
    public TemplateMail registrationConfirmationTemplate() {
        return new TemplateMail(
                env.getProperty("mail.template.registrationConfirmation.subject"),
                env.getProperty("mail.template.registrationConfirmation.text")
        );
    }

    @Bean(name = "resetPasswordConfirmationTemplate")
    public TemplateMail resetPasswordConfirmationTemplate() {
        return new TemplateMail(
                env.getProperty("mail.template.resetPasswordConfirmation.subject"),
                env.getProperty("mail.template.resetPasswordConfirmation.text")
        );
    }

    @Bean(name = "resetPasswordDoneTemplate")
    public TemplateMail resetPasswordDoneTemplate() {
        return new TemplateMail(
                env.getProperty("mail.template.resetPassword.subject"),
                env.getProperty("mail.template.resetPassword.text")
        );
    }

}
