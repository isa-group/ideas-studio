/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.resources.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



//import es.us.isa.sedl.sedl4json.SEDLModule;


/**
 *
 * @author japarejo
 */
public class CustomJacksonObjectMapper extends ObjectMapper {
    //SEDLModule sedlModule;
    public CustomJacksonObjectMapper()
    {
        super();
        //sedlModule=new SEDLModule();
        //registerModule(sedlModule);
        //sedlModule.configure(this);        
        /*
        getDeserializationConfig().getHandlerInstantiator().addHandler(new DeserializationProblemHandler() {

            @Override
            public boolean handleUnknownProperty(DeserializationContext ctxt, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException, JsonProcessingException {
                Logger.getLogger(CustomJacksonObjectMapper.class.getName()).log(Level.WARNING,String.format("Could not deserialize property with name '%s' on object of type '%s'", propertyName, beanOrClass.getClass().getName()));
                return true;
            }
            
        });*/
    }
    
    public void refresh()
    {
        /*sedlModule.refreshExtensionPointsRegistries();
        registerModule(sedlModule);
        sedlModule.configure(this);        */
    }
}
