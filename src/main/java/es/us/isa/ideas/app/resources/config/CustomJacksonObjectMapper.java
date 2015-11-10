/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.resources.config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializationProblemHandler;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author japarejo
 */
public class CustomJacksonObjectMapper extends ObjectMapper {
	public CustomJacksonObjectMapper() {
		super();
		getDeserializationConfig().addHandler(
				new DeserializationProblemHandler() {

					@Override
					public boolean handleUnknownProperty(
							DeserializationContext ctxt,
							JsonDeserializer<?> deserializer,
							Object beanOrClass, String propertyName)
							throws IOException, JsonProcessingException {
						Logger.getLogger(
								CustomJacksonObjectMapper.class.getName())
								.log(Level.WARNING,
										String.format(
												"Could not deserialize property with name '%s' on object of type '%s'",
												propertyName, beanOrClass
														.getClass().getName()));
						return true;
					}

				});
	}
}
