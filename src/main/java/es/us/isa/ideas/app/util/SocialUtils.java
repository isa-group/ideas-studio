package es.us.isa.ideas.app.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.social.connect.Connection;
import org.springframework.util.MultiValueMap;

import es.us.isa.ideas.app.social.SocialNetwork;

public class SocialUtils {

  public static List<String> getMissingServices(MultiValueMap<String, Connection<?>> userConnections) {
    List<String> result=new ArrayList<String>();

    for(SocialNetwork network: SocialNetwork.values()) {
      List<Connection<?>> connections = userConnections.get(network.getName());

      if(connections.size() < 1) {
        result.add(network.getName());
      }
    }
        
    return result;
  }

  public static List<String> getConnectedServices(MultiValueMap<String, Connection<?>> userConnections) {
    Set<String> result=new HashSet<String>();

    for(SocialNetwork network: SocialNetwork.values()) {
      List<Connection<?>> connections = userConnections.get(network.getName());

      if(connections.size() > 0) {
        result.add(network.getName());
      }
    }
        
    return new LinkedList<String>(result);
  }
}