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
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC07_DeleteWorkspace extends TestCase {

    protected static final String SELECTOR_DELETE_BUTTON = "#delete-ws";
    
    private static boolean testResult = true;
    private static final String WORKSPACE_NAME = TestSuite.getWorkspaceNewName();
    private static final Logger LOG = Logger.getLogger(TC07_DeleteWorkspace.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC07_DeleteWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC07_DeleteWorkspace finished");
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
    public void step02_deleteCurrentWorkspace() {
        
        // Delete workspace button
        waitForVisibleSelector(SELECTOR_DELETE_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DELETE_BUTTON + "').click();");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC07_DeleteWorkspace.class.getName()).log(Level.SEVERE, null, ex);
        }


        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }

        testResult = !IdeasStudioActions.existWorkspaceByName(WORKSPACE_NAME);
        assertTrue(testResult);
    }
}
