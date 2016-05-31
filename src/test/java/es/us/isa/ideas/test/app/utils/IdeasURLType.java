/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.us.isa.ideas.test.app.utils;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain.
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public enum IdeasURLType {
    // Please, for future file types named like its module name
    EDITOR_URL("app/editor"), SETTINGS_NEW_USER_URL("/settings/user"), LOGIN_URL("/security/login");
    
    private final String relativeURL;
    
    IdeasURLType(String relativeURL) {
        this.relativeURL = relativeURL;
    }

    @Override
    public String toString() {
        return relativeURL;
    }
    
    
    
}
