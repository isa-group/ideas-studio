package es.us.isa.ideas.test.app.dashboard;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
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
public class TC08_UpdateDemo extends TestCase{
    
    private static final String SELECTOR_CARD_DEMO_WORKSPACE_NAME = ".demoworkspace .card__meta-content-title";
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC08_UpdateDemo...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC08_UpdateDemo finished");
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
    public void step02_updateDemoWorkspace() {

        // Update demo workspace button
        waitForVisibleSelector(SELECTOR_DASHBOARD_DEMO_CARD_UPDATE_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_DEMO_CARD_UPDATE_BUTTON + "').click();");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC11_CloneDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");

        
        //waitTwoMoreSeconds();

        waitForVisibleSelector(SELECTOR_CARD_DEMO_WORKSPACE_NAME);
        testResult = getWebDriver().findElements(By.cssSelector(SELECTOR_CARD_DEMO_WORKSPACE_NAME)).size() > 0;

        //testResult = getWorkspaceNewName().equals(getTextFromSelector(SELECTOR_CARD_DEMO_WORKSPACE_NAME));
        
        assertTrue(testResult);
    }
}
