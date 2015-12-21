package es.us.isa.ideas.test.module.plaintext;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import static es.us.isa.ideas.test.utils.TestCase.getExpectedActions;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC06_RemoveCurrentWorkspace extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = true;
    private static final String WORKSPACE_NAME = "Workspace";
    private static final Logger LOG = Logger.getLogger(TC06_RemoveCurrentWorkspace.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "Init TC06_RemoveCurrentWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "TC06_RemoveCurrentWorkspace finished");
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
            IdeasStudioActions.executeCommands(gcliCommand);    // executing this command will auto-refresh editor page

            Thread.sleep(2000);
            waitForVisibleSelector("#menuToggler");
            getExpectedActions().click(By.id("menuToggler"));

            Thread.sleep(2000);
            testResult = !IdeasStudioActions.existWorkspaceByName(WORKSPACE_NAME);

            Thread.sleep(2000);
            assertTrue(testResult);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

}
