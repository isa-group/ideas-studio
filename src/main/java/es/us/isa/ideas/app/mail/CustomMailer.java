package es.us.isa.ideas.app.mail;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.hibernate.annotations.common.util.impl.Log_$logger;

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
        this.sendMailGrid(to, subject, msg);
    }
    
    public void sendMailGrid(String to, String subject, String msg)
    {
        try {
            Email from = new Email(getFrom());
            Email _to = new Email(to);
            Content content = new Content("text/plain", msg);
            Mail mail = new Mail(from, subject, _to, content);

            String env = System.getenv("SENDGRID_API_KEY");
            if (env == null) {
                System.out.println("No environment SENDGRID_API_KEY found. Please, declare SENDGRID_API_KEY to send emails from workbench");
            }
            SendGrid sg = new SendGrid(env);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
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
