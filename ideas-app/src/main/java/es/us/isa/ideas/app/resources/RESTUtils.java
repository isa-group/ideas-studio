/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Function;

/**
 *
 * @author japarejo
 */
public class RESTUtils {
    
    public static final String SEDL_VERSION="1";
    public static final String SEDL_NAMESPACE="http://moses.us.es/schemas/sedl/v"+SEDL_VERSION+"/utils";
    public static final String SEDL_PREFIX="sedlUtils";
    
    
    public void signRequest(Request req)
    {
        
    }
    
    public static <T> String [] buildURLArray(Collection<T> items, Function<T,String> getID, UriInfo uriInfo)
    {
        String [] uriArray=new String[items.size()];
        int index=0;
        for (T item : items) {
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI userUri = ub.
                    path(getID.apply(item)).
                    build();
            uriArray[index]=userUri.toASCIIString();
            index++;
        }
        return uriArray;
    }
    
    
    public static <T> List<String> buildListOfURIs(Collection<T> items, Function<T,String> getID, UriInfo uriInfo)
    {
        List<String> uriList=new JAXBList<String>();
        
        URI userUri=null;
        for (T item : items) {
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
             userUri= ub.
                    path(getID.apply(item)).
                    build();
             uriList.add(userUri.toASCIIString());
        }
        return uriList;
    }
    
    @XmlRootElement(name="List")
    private static final class JAXBList<T> extends ArrayList<T>{  
        @XmlElement(name="item")
        @JsonIgnore
        public List<T> getList(){
            return this;
        }
        
        @JsonIgnore
        public void setList(List<T> value){
            this.clear();
            this.addAll(value);
        }
                
    }
    

        
}
