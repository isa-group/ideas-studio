/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.testcase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class TestProperty {

    private static final Logger LOG = Logger.getLogger(TestProperty.class.getName());

    public static Properties getTestProperties() {

        Properties prop = new Properties();
        InputStream input;

        try {
            input = TestProperty.class.getResourceAsStream("/selenium.properties");
            if (input != null) {
                prop.load(input);
            }
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }

        return prop;
    }

    public static String getBaseUrl() {

        String baseUrl = "";

        try {
            String environmentProperty = getTestProperties().getProperty("test.environment");

            if (environmentProperty.equals("REMOTE")) {
                baseUrl += "https://labs.isa.us.es:8181/IDEAS-pre";
            } else {
                baseUrl += "https://localhost:8181/IDEAS";
            }
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }

        return baseUrl;
    }

}
