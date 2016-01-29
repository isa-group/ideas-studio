package es.us.isa.ideas.test.app.dynatree;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC99_RemoveCurrentWorkspace extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = true;
    private static final String WORKSPACE_NAME = TestSuite.getWorkspace();
    private static final Logger LOG = Logger.getLogger(TC99_RemoveCurrentWorkspace.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC10_RemoveCurrentWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC10_RemoveCurrentWorkspace finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, Class.class.getName() + " testResult value: {0}", testResult);
        testResult = false;
    }

    @Test
    public void step01_goEditorPage() {
        testResult = IdeasStudioActions.goEditorPage();
        assertTrue(testResult);
    }

    @Test
    public void step02_removeCurrentWorkspace() {
        String gcliCommand = "deleteWorkspace " + WORKSPACE_NAME;

        try {
            Thread.sleep(2000); // explicity wait for command response
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }

        IdeasStudioActions.executeCommands(gcliCommand);    // executing this command will auto-refresh editor page
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }

        waitForVisibleSelector(SELECTOR_WS_TOGGLER);
        getJs().executeScript("jQuery('" + SELECTOR_WS_TOGGLER + "').click();");

        try {
            Thread.sleep(2000); // menu toggle animation
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
        testResult = !IdeasStudioActions.existWorkspaceByName(WORKSPACE_NAME);
        assertTrue(testResult);
    }

}
