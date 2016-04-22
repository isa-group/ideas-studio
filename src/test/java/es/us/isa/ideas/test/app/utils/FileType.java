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
    IAGREE_TEMPLATE("at"), IAGREE_OFFER("ao"), IAGREE_AGREEMENT("ag");
    
    private final String extension;
    
    FileType(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "." + extension;
    }
    
    
    
}
