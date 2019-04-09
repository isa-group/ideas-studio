package es.us.isa.ideas.app.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class reCaptchaConfiguration {
 
    private String site;
    private String secret;
 
    // standard getters and setters

    public String getSite() {
      return this.site;
    }

    public void setSite(String site) {
      this.site = site;
    }

    public String getSecret() {
      return this.secret;
    }

    public void setSecret(String secret) {
      this.secret = secret;
    }
}