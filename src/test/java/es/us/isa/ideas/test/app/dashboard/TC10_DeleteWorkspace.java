package es.us.isa.ideas.test.app.dashboard;

import static es.us.isa.ideas.test.app.dashboard.TestSuite.getWorkspaceNewName;
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
public class TC10_DeleteWorkspace extends TestCase{
    
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init T10_DeleteWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## T10_DeleteWorkspace finished");
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
    public void step02_deleteWorkspace() {

        // Delete workspace button
        waitForVisibleSelector(SELECTOR_DASHBOARD_WORKSPACE_CARD_DELETE_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_WORKSPACE_CARD_DELETE_BUTTON + "').click();");
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        testResult = !IdeasStudioActions.existWorkspaceByName(getWorkspaceNewName());
        
        assertTrue(testResult);
    }
}
