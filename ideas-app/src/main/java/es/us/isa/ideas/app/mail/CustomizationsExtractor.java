package es.us.isa.ideas.app.mail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author japarejo
 */
class CustomizationsExtractor {
    Collection<SpecificCustomizationExtractor> extractors;

    public CustomizationsExtractor(Collection<SpecificCustomizationExtractor> extractors) {
        this.extractors = extractors;
    }

    public Map<String, String> extract(Object researcher) {
        Map<String,String> result=new HashMap<String,String>(); 
        if(researcher.getClass().isArray())
        {
            
            Object[] objects=(Object[])researcher;
            for(Object object:objects)
                result.putAll(extract(object));
        }else{
            for(SpecificCustomizationExtractor extractor:extractors) {
                if(extractor.canExtractCustomizations(researcher)) {
                    result.putAll(extractor.extractCustomizations(researcher));
                }
            }            
        }
        return result;
    }
    
    public Map<String,String> extract(Collection<Object> objects)
    {
        Map<String,String> result=new HashMap<String,String>();
        for(Object object:objects)
            result.putAll(extract(object));
        return result;
    }
    
    
    
}
