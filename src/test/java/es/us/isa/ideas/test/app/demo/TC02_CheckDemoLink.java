package es.us.isa.ideas.test.app.demo;

import static es.us.isa.ideas.test.app.demo.TestSuite.getDemoWorkspaceName;
import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.getJs;
import static es.us.isa.ideas.test.utils.TestCase.getWebDriver;
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC02_CheckDemoLink extends TestCase{
    
    private static final String SELECTOR_WS_INFO = "#editorSidePanelHeaderWorkspaceInfo";

    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC02_CheckDemoLink...");
        try {
            logout();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC02_CheckDemoLink finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, "testResult: {0}", testResult);
        testResult = false;
    }
    
    @Test
    public void step01_goDashboardPage() {
        testResult = IdeasStudioActions.goWSMPage();
        assertTrue(testResult);
    }
    
    @Test
    public void step02_visitDemoWorkspaceLink() throws InterruptedException {
  
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC02_CheckDemoLink.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Publish workspace button
//        waitForVisibleSelector(SELECTOR_DASHBOARD_DEMO_VIEW_BUTTON);    
        
        By locator = By.cssSelector(SELECTOR_DASHBOARD_DEMO_VIEW_BUTTON);
        getExpectedActions().getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        getExpectedActions().getWait().until(ExpectedConditions.elementToBeSelected(locator));
        
        WebElement element = getWebDriver().findElement(locator);
        

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ex.getMessage());
        }

        JavascriptExecutor js = ((JavascriptExecutor) getWebDriver());
        js.executeScript("window.scrollTo(0," + element.getLocation().x + ")");
        element.click();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC02_CheckDemoLink.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");

        testResult  = getWebDriver().findElement(By.cssSelector(SELECTOR_WS_INFO)).getText().equals(getDemoWorkspaceName()) 
                        && isCurrentUrlContains("/IDEAS/app/editor");;    
        
        assertTrue(testResult);
    }
}
