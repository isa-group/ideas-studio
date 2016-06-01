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
public enum FileType {
    // Please, for future file types named like its module name
    ANGULAR("ang"), ANGULAR_CONTROLLER("ctl"), PLAINTEXT("txt"), JSON("json"), YAML("yaml"),
    IAGREE_TEMPLATE("iat"), IAGREE_OFFER("iao"), IAGREE_AGREEMENT("iag");
//    IAGREE_TEMPLATE_OLD("at"), IAGREE_OFFER_OLD("ao"), IAGREE_AGREEMENT_OLD("ag");
    
    private final String extension;
    
    FileType(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "." + extension;
    }
    
    
    
}
