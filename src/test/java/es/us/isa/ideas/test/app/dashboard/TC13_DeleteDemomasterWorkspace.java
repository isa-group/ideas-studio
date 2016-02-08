/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.dashboard;

import static es.us.isa.ideas.test.app.dashboard.TestSuite.getDemoWorkspaceName;
import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.getExpectedActions;
import static es.us.isa.ideas.test.utils.TestCase.getJs;
import static es.us.isa.ideas.test.utils.TestCase.getWebDriver;
import static es.us.isa.ideas.test.utils.TestCase.loginWithParams;
import static es.us.isa.ideas.test.utils.TestCase.logout;
import static es.us.isa.ideas.test.utils.TestCase.validatePropertyValues;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC13_DeleteDemomasterWorkspace extends TestCase{
    
    private static String user = "DemoMaster";
    private static String pass = "DemoMaster";
 
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());
    
    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC13_DeleteDemomasterWorkspace...");
        try {
            logout();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC13_DeleteDemomasterWorkspace finished");
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
    public void step02_loginWithGuestUser() {

        try {
            if (validatePropertyValues(user, pass)) {
                testResult = loginWithParams(user, pass);
            }
            Thread.sleep(1000); // avoid failing sometimes
        } catch (InterruptedException ex) {
            Logger.getLogger(TC02_Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertTrue(testResult);
    }

    @Test
    public void step03_goDashboardPage() {
        testResult = IdeasStudioActions.goWSMPage();
        assertTrue(testResult);
    }
    
    
    @Test
    public void step04_deleteWorkspace() {

        // Delete workspace button
        waitForVisibleSelector(SELECTOR_DASHBOARD_WORKSPACE_CARD_DELETE_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_WORKSPACE_CARD_DELETE_BUTTON + "').click();");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC11_CloneDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC11_CloneDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        testResult = !IdeasStudioActions.existWorkspaceByName(getDemoWorkspaceName());
        
        assertTrue(testResult);
    }
}