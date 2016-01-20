package es.us.isa.ideas.test.app.dashboard;

import static es.us.isa.ideas.test.app.dashboard.TestSuite.getDemoWorkspaceName;
import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.echoCommandApi;
import static es.us.isa.ideas.test.utils.TestCase.getExpectedActions;
import static es.us.isa.ideas.test.utils.TestCase.getJs;
import static es.us.isa.ideas.test.utils.TestCase.getTextFromSelector;
import static es.us.isa.ideas.test.utils.TestCase.getWebDriver;
import static es.us.isa.ideas.test.utils.TestCase.logout;
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
import org.openqa.selenium.By;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC01_PublicDashboard extends TestCase{
    
    private static String user = "guest";
    private static String pass = "guest";
    
    private static final String SELECTOR_DASHBOARD_PUBLIC_DEMO_VIEW = "#viewDemo";
    private static final String SELECTOR_PUBLISH_BUTTON = "#demo-ws";
    private static final String SELECTOR_DEMO_CARD_TITLE = ".demoworkspace .card__meta-content-title";

 
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());
    
    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC01_PublicDashboard...");
        try {
            logout();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC01_PublicDashboard finished");
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

        System.out.println("Login with params...");
        System.out.println("Username: " + user);
        System.out.println("Password: " + pass);

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
    public void step03_openWorkspaceMenu() {

        waitForVisibleSelector(SELECTOR_WS_TOGGLER);
        getJs().executeScript("jQuery('" + SELECTOR_WS_TOGGLER + "').click();");

        // Animation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        // Workspace list is visible
        testResult = getWebDriver().findElement(By.cssSelector(SELECTOR_WS_MANAGER)).isDisplayed();
        assertTrue(testResult);

    }

    @Test
    public void step04_createWorkspace() {

        // Add workspace button
        waitForVisibleSelector(SELECTOR_ADD_BUTTON);
        getExpectedActions().click(By.cssSelector(SELECTOR_ADD_BUTTON));
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC11_CloneDemo.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_INPUT);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_MODAL_INPUT), getDemoWorkspaceName());
        getExpectedActions().click(By.linkText("Create"));

        // Close workspace manager
        waitForVisibleSelector(SELECTOR_WS_LIST);
        getJs().executeScript("jQuery('" + SELECTOR_WS_LIST + "').click();");

        try {
            Thread.sleep(2000); // animation
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        testResult = getDemoWorkspaceName().equals(getTextFromSelector(SELECTOR_WS_CURRENT));

        if (testResult) {
            echoCommandApi("Workspace \"" + getDemoWorkspaceName() + "\" was successfully created.");
        }

        assertTrue(testResult);
    }
    
    @Test
    public void step05_publishDemoWorkspace() {

        //Publish demo workspace button
        waitForVisibleSelector(SELECTOR_PUBLISH_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_PUBLISH_BUTTON + "').click();");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
         try {
            Thread.sleep(2000); // animation
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
        

        IdeasStudioActions.goWSMPage();
        
       
        waitForVisibleSelector(SELECTOR_DEMO_CARD_TITLE);
        testResult = getWebDriver().findElements(By.cssSelector(SELECTOR_DEMO_CARD_TITLE)).size() > 0;
                
        assertTrue(testResult);
    }
    
    @Test
    public void step06_goWSMPage() {
        try {
            logout();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    
        testResult = IdeasStudioActions.goWSMPage();
        assertTrue(testResult);
    }
    
    @Test
    public void step07_viewDemo() {
        
        waitForVisibleSelector(SELECTOR_DASHBOARD_PUBLIC_DEMO_VIEW);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_PUBLIC_DEMO_VIEW + "').click();");
        
        testResult = true; 
        assertTrue(testResult);
    }
}