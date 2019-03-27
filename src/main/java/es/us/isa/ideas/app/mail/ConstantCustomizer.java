package es.us.isa.ideas.app.mail;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author japarejo
 */
public class ConstantCustomizer implements SpecificCustomizationExtractor<Object> {

    private String key;
    private String value;

    @Override
    public boolean canExtractCustomizations(Object o) {
        return true;
    }

    @Override
    public Map<String, String> extractCustomizations(Object element) {
        Map<String,String> result=new HashMap<String,String>();
        result.put(key,value);
        return result;
    }

    /**
     * @return the Key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param Key the Key to set
     */
    public void setKey(String Key) {
        this.key = Key;
    }

    /**
     * @return the Value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param Value the Value to set
     */
    public void setValue(String Value) {
        this.value = Value;
    }
    
}
