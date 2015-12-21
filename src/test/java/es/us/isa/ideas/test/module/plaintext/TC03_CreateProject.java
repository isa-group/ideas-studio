package es.us.isa.ideas.test.module.plaintext;

import static es.us.isa.ideas.test.module.plaintext.TestSuite.getProject;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC03_CreateProject extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC03_CreateProject...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC03_CreateProject finished");
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
    public void step02_createProject() {

        if (IdeasStudioActions.expandAddMenu()) {

            TestCase.getExpectedActions().click(By.linkText("Create Project"));

            try {
                Thread.sleep(1000); // modal animation 
            } catch (InterruptedException e) {
                LOG.severe(e.getMessage());
            }

            // Modal window
            TestCase.getExpectedActions().sendKeys(By.cssSelector(SELECTOR_MODAL_INPUT), getProject());
            TestCase.getExpectedActions().click(By.linkText("Create")); // submit modal

            waitForVisibleSelector(SELECTOR_PROJECT);
            testResult = getProject().equals(getTextFromSelector(SELECTOR_PROJECT));
        }

        if (testResult) {
            echoCommandApi("Project \"" + getProject() + "\" was successfully created.");
        }
        assertTrue(testResult);

    }

    //TODO: check if project has been created on ideas repository
}
