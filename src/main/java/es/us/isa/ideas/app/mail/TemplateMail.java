package es.us.isa.ideas.app.mail;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author japarejo
 */
public class TemplateMail {
    private String subject;
    private String content;
    private Set<String> attachments;
    
    public TemplateMail(){
        this("","");        
    }
    
    public TemplateMail(String subject,String content){
        this.subject=subject;
        this.content=content;
        attachments=new HashSet<>();
    }
    
    public String getCustomizedSubject(Map<String,String> customizations)
    {    
        return customize(customizations, getSubject());
    }
    
    public String getCustomizedContent(Map<String,String> customizations)
    {
        return customize(customizations, getContent());
    }
    
    public Set<String> getCustomizedAttachments(Map<String,String> customizations)
    {
        Set<String> result=new HashSet<String>();
        for(String attachment:getAttachments())
        {
            result.add(customize(customizations, attachment));
        }
        return result;
    }
    
    protected String customize(Map<String,String> customizations, String templateString)
    {
        String result=templateString;
        for(String key:customizations.keySet())
        {
            result=result.replace(key, customizations.get(key));
        }
        return result;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the attachments
     */
    public Set<String> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(Set<String> attachments) {
        this.attachments = attachments;
    }
    
    
            
}
