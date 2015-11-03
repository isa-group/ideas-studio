package es.us.isa.ideas.app.mail;

import java.util.Collection;
import java.util.Map;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;



/**
 *
 * @author japarejo
 */
public class CustomMailer {
    
    private MailSender mailSender;
    private CustomizationsExtractor customizationExtrator;
    private String from;    
    private String [] bcc;
    private String [] cc;
    
    
    public CustomMailer()
    {        
        super();
    }
    
    public void sendMail(Collection<String> recipients, String subject, String msg)
    {
        for(String to:recipients)
            sendMail(to,subject,msg);
    }
    
    public void sendMail(String to, String subject, String msg)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getFrom());
        message.setTo(to);
        message.setCc(getCc());
        message.setBcc(getBcc());
        message.setSubject(subject);
        message.setText(msg);
        getMailSender().send(message);
    }
    
    public void sendMail(String to, Map<String,String> customizations, TemplateMail template)
    {
        sendMail(to,template.getCustomizedSubject(customizations),template.getCustomizedContent(customizations));
    }
    
    public void sendMail(Map<String,Map<String,String>> recipients, TemplateMail template)
    {
        Map<String,String> customizations;
        for(String to:recipients.keySet())
        {
            customizations=recipients.get(to);
            sendMail(to,customizations,template);
        }
    }

    public Map<String, String> extractCustomizations(Object researcher) {
        return getCustomizationExtrator().extract(researcher);
    }

    /**
     * @return the mailSender
     */
    public MailSender getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender the mailSender to set
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @return the customizationExtrator
     */
    public CustomizationsExtractor getCustomizationExtrator() {
        return customizationExtrator;
    }

    /**
     * @param customizationExtrator the customizationExtrator to set
     */
    public void setCustomizationExtrator(CustomizationsExtractor customizationExtrator) {
        this.customizationExtrator = customizationExtrator;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the bcc
     */
    public String[] getBcc() {
        return bcc;
    }

    /**
     * @param bcc the bcc to set
     */
    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    /**
     * @return the cc
     */
    public String[] getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String[] cc) {
        this.cc = cc;
    }
    
    
}
