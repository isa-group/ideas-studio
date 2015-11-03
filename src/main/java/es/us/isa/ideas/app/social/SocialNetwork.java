/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.social;

/**
 *
 * @author japarejo
 */
public enum SocialNetwork { FACEBOOK("facebook"), TWITTER("twitter"), 
                    LINKEDIN("linkedin"), GOOGLE("google"), GITHUB("github") ;
    private String name;
    
    SocialNetwork(String name){
        this.name=name;
    }
    
    public String getName(){
        return name;
    }
}
