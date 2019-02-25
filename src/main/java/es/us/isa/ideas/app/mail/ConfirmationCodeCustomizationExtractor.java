package es.us.isa.ideas.app.mail;

import java.util.HashMap;
import java.util.Map;

import es.us.isa.ideas.app.entities.Confirmation;

/**
 *
 * @author japarejo
 */
public class ConfirmationCodeCustomizationExtractor implements SpecificCustomizationExtractor<Confirmation>
{
    public static final String CONFIRMATION_CODE_KEY="$code";

    @Override
    public boolean canExtractCustomizations(Object o) {
        return o instanceof Confirmation;
    }

    @Override
    public Map<String, String> extractCustomizations(Confirmation element) {
        Map<String,String> result=new HashMap<String,String>();
        result.put(CONFIRMATION_CODE_KEY, element.getConfirmationCode());
        return result;
    }
    
    
}
