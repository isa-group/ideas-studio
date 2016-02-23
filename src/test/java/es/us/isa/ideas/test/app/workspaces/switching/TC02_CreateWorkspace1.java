package es.us.isa.ideas.test.app.workspaces.switching;

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
public class TC02_CreateWorkspace1 extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC02_CreateWorkspace_1...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC02_CreateWorkspace_1 finished");
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

    /**
     * Can open workspace menu
     */
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
        getJs().executeScript("jQuery('" + SELECTOR_ADD_BUTTON + "').click();"); // avoid element not clickable exception

        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_INPUT);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_MODAL_INPUT), TestSuite.getWorkspace1());
        getExpectedActions().click(By.linkText("Create"));

        // Close workspace manager
        waitForVisibleSelector(SELECTOR_WS_LIST);
        getJs().executeScript("jQuery('" + SELECTOR_WS_LIST + "').click();"); // avoid element not clickable exception

        try {
            Thread.sleep(2000); // animation
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        testResult = TestSuite.getWorkspace1().equals(getTextFromSelector(SELECTOR_WS_CURRENT));

        if (testResult) {
            echoCommandApi("Workspace \"" + TestSuite.getWorkspace1() + "\" was successfully created.");
        }

        assertTrue(testResult);
    }

}