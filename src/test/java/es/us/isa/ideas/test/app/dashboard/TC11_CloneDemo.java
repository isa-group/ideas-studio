package es.us.isa.ideas.test.app.dashboard;

import static es.us.isa.ideas.test.app.dashboard.TestSuite.getDemoWorkspaceName;
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

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC11_CloneDemo extends TestCase{
    
    private static final String SELECTOR_CARD_PUBLIC_DEMO_NAME = ".publicdemo  .card__meta-content-title";
    
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC11_CloneDemo...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC11_CloneDemo finished");
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
    public void step02_cloneDemo() {

        // Clone demo button
        waitForVisibleSelector(SELECTOR_DASHBOARD_PUBLIC_DEMO_CLONE_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_PUBLIC_DEMO_CLONE_BUTTON + "').click();");
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        IdeasStudioActions.goWSMPage();
        
        waitForVisibleSelector(SELECTOR_CARD_PUBLIC_DEMO_NAME);
        testResult = getTextFromSelector(SELECTOR_CARD_PUBLIC_DEMO_NAME).equals("Demo Workspace");
        
        assertTrue(testResult);
    }
    
    public void step03_deleteClone() {

        // Delete workspace button
        waitForVisibleSelector(SELECTOR_DASHBOARD_WORKSPACE_CARD_DELETE_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_WORKSPACE_CARD_DELETE_BUTTON + "').click();");
        
        try {
            Thread.sleep(2000); //Extra waiting time solves error
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        try {
            Thread.sleep(2000); //Extra waiting time solves error
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        testResult = !IdeasStudioActions.existWorkspaceByName(getDemoWorkspaceName());
        
        assertTrue(testResult);
    }
}

