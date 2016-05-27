/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.social;

/**
 *
 * @author japarejo
 */
public enum SocialNetwork {
    TWITTER("twitter"), GOOGLE("google");
    private String name;

    SocialNetwork(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
