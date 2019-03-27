/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.configuration;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.ServletContextAttributeExporter;

/**
 *
 * @author japarejo
 */
@Configuration
public class StudioConfigurationInitializator {

    @Autowired
    StudioConfiguration value;
    
    @Bean(name="studioConfiguration")
    public StudioConfiguration load(@Value("#{servletContext.getRealPath('')}") String path) {
        value=StudioConfiguration.load(path);
        return value;
    }
    
    @Bean
    public ServletContextAttributeExporter initializeGlobalStudioConfiguration(){
        ServletContextAttributeExporter result=new ServletContextAttributeExporter();
        result.setAttributes(new HashMap<String,Object>() {{
            put("studioConfiguration", value);
        }});
        
        return result;
    }
		
}
