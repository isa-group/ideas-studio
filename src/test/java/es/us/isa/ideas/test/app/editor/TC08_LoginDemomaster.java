package es.us.isa.ideas.test.app.editor;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.loginWithParams;
import static es.us.isa.ideas.test.utils.TestCase.logout;
import static es.us.isa.ideas.test.utils.TestCase.validatePropertyValues;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC08_LoginDemomaster extends es.us.isa.ideas.test.utils.TestCase {

    private static String user = "";
    private static String pass = "";
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC01_Login...");
        try {
            logout();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC01_Login finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, "testResult: {0}", testResult);
        testResult = false;
    }

    @Test
    public void step01_goHomePage() {
        testResult = IdeasStudioActions.goHomePage();
        assertTrue(testResult);
    }

    @Test
    public void step02_loadSeleniumUserProperties() {
        user = "DemoMaster";
        pass = "DemoMaster";
        testResult = validatePropertyValues(user, pass);
        assertTrue(testResult);
    }

    @Test
    public void step03_loginWithSeleniumProperties() {

        System.out.println("Login with params...");
        System.out.println("Username: " + user);
        System.out.println("Password: " + pass);

        try {
            if (validatePropertyValues(user, pass)) {
                testResult = loginWithParams(user, pass);
            }
            Thread.sleep(1000); // avoid failing sometimes
        } catch (InterruptedException ex) {
            Logger.getLogger(TC01_LoginAutotester.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertTrue(testResult);
    }

}
