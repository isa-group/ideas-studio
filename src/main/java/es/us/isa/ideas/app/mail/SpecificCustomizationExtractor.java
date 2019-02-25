package es.us.isa.ideas.app.mail;

import java.util.Map;

/**
 *
 * @author japarejo
 */
public interface SpecificCustomizationExtractor<X> {        
    public boolean canExtractCustomizations(Object o);        
    public Map<String,String> extractCustomizations(X element);
}
