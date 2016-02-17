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

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC02_CheckDemoLink extends TestCase{
    
    private static final String SELECTOR_CARD_DEMO_WORKSPACE_NAME = ".demoworkspace .card__meta-content-title";
    private static final String SELECTOR_WS_INFO = "#editorSidePanelHeaderWorkspaceInfo";
    private static final String DEMO_URL = "#demoURL";

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
  
        // Publish workspace button
        waitForVisibleSelector(SELECTOR_DASHBOARD_DEMO_VIEW_BUTTON);    
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_DEMO_VIEW_BUTTON + "').click();");
        
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
