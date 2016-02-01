package es.us.isa.ideas.test.app.editor;

import es.us.isa.ideas.test.app.dashboard.TC11_CloneDemo;
import static es.us.isa.ideas.test.app.editor.TestSuite.getWorkspaceName;
import static es.us.isa.ideas.test.app.editor.TestSuite.getWorkspaceDescription;
import static es.us.isa.ideas.test.app.editor.TestSuite.getWorkspaceTags;
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
 *         Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * 
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC02_CreateWorkspace extends TestCase {

    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC02_CreateWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC02_CreateWorkspace finished");
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
    public void step02_openWorkspaceMenu() {

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
    public void step03_createWorkspace() {

        // Add workspace button
        waitForVisibleSelector(SELECTOR_ADD_BUTTON);
        getExpectedActions().click(By.cssSelector(SELECTOR_ADD_BUTTON));
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC11_CloneDemo.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Modal window
        waitForVisibleSelector(SELECTOR_NEW_WS_MODAL_INPUT_NAME);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_NEW_WS_MODAL_INPUT_NAME), getWorkspaceName());
        waitForVisibleSelector(SELECTOR_NEW_WS_MODAL_INPUT_DESCRIPTION);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_NEW_WS_MODAL_INPUT_DESCRIPTION), getWorkspaceDescription());
        waitForVisibleSelector(SELECTOR_NEW_WS_MODAL_INPUT_TAGS);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_NEW_WS_MODAL_INPUT_TAGS), getWorkspaceTags());
        getExpectedActions().click(By.linkText("Create"));

        // Close workspace manager
        waitForVisibleSelector(SELECTOR_WS_LIST);
        getExpectedActions().click(By.cssSelector(SELECTOR_WS_LIST));

        try {
            Thread.sleep(2000); // animation
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        testResult = getWorkspaceName().equals(getTextFromSelector(SELECTOR_WS_CURRENT));

        if (testResult) {
            echoCommandApi("Workspace \"" + getWorkspaceName() + "\" was successfully created.");
        }

        assertTrue(testResult);
    }

}
