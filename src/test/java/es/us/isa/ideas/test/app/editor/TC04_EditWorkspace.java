package es.us.isa.ideas.test.app.editor;

import static es.us.isa.ideas.test.app.editor.TestSuite.getWorkspaceName;
import static es.us.isa.ideas.test.app.editor.TestSuite.getWorkspaceNewName;
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
import es.us.isa.ideas.test.utils.TestCase;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC04_EditWorkspace extends TestCase {

    protected static final String SELECTOR_EDIT_BUTTON = "#edit-ws";
    protected static final String SELECTOR_FORM_SAVE_BUTTON = "#wsForm > button";

    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC04_EditWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC04_EditWorkspace finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, "testResult: {0}", testResult);
        testResult = false;
    }

    @Test
    public void step01_goEditorPage() {
        testResult = IdeasStudioActions.goEditorPage();
        assertTrue(testResult);
    }

    @Test
    public void step02_openWorkspaceEditFrom() {

        waitForVisibleSelector(SELECTOR_EDIT_BUTTON);
        getExpectedActions().click(By.cssSelector(SELECTOR_EDIT_BUTTON));

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
    public void step03_editWorkspace() {

        waitForVisibleSelector(SELECTOR_WORKSPACE_FORM_INPUT_NAME);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_WORKSPACE_FORM_INPUT_NAME), " modified");
        waitForVisibleSelector(SELECTOR_WORKSPACE_FORM_INPUT_DESCRIPTION);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_WORKSPACE_FORM_INPUT_DESCRIPTION), " (Edit test)");
        getExpectedActions().click(By.cssSelector(SELECTOR_FORM_SAVE_BUTTON));

        try {
            Thread.sleep(2000); // animation
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        IdeasStudioActions.goEditorPage();
        
        testResult = getWorkspaceNewName().equals(getTextFromSelector(SELECTOR_WS_CURRENT));

        if (testResult) {
            echoCommandApi("Workspace \"" + getWorkspaceName() + "\" was successfully modified.");
        }

        assertTrue(testResult);
    }

}
