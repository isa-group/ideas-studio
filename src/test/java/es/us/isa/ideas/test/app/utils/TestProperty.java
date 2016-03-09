/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.utils;

import es.us.isa.ideas.test.app.pageobject.TestCase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
    private static final String FILE_NAME = "selenium.properties";
    
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

    public static Properties getTestProperties() {

        Properties prop = new Properties();
        InputStream input;

        try {
            input = TestProperty.class.getResourceAsStream("/" + FILE_NAME);
            if (input != null) {
                prop.load(input);
            }
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }

        return prop;
    }
    
    public static String getTestUser() {
        return getTestProperties().getProperty("test.user");
    }
    
    public static String getTestUserPassword() {
        return getTestProperties().getProperty("test.user.pass");
    }
    
    public static String getTestUserName() {
        return getTestProperties().getProperty("test.user.name");
    }
    
    public static String getTestUserEmail() {
        return getTestProperties().getProperty("test.user.email");
    }
    
    public static String getTestUserEmailPassword() {
        return getTestProperties().getProperty("test.user.email.pass");
    }
    
    public static String getTestUserPhone() {
        return getTestProperties().getProperty("test.user.phone");
    }
    
    public static String getTestUserAddress() {
        return getTestProperties().getProperty("test.user.address");
    }
    
    public static String getTestTwitterUser() {
        return getTestProperties().getProperty("test.tw.user");
    }
    
    public static String getTestTwitterPassword() {
        return getTestProperties().getProperty("test.tw.pass");
    }
    
    public static String getTestGoogleUser() {
        return getTestProperties().getProperty("test.go.user");
    }
    
    public static String getTestGooglePassword() {
        return getTestProperties().getProperty("test.go.pass");
    }
    
    public static void setTestUserPassword(String pass) {
        
        try {
            java.net.URL resource = TestCase.class.getResource("/" + FILE_NAME);
            final File propsFile = new File(resource.toURI());
            
            FileInputStream fileName = new FileInputStream(propsFile);
            Properties props = getTestProperties();
            props.load(fileName);
            props.setProperty("autotester.pass", pass);
            fileName.close();
            
            FileOutputStream outFileName = new FileOutputStream(propsFile);
            props.store(outFileName, "");
            outFileName.close();
        } catch (URISyntaxException | IOException ex) {
            LOG.severe(ex.getMessage());
        }
        
    }

}
