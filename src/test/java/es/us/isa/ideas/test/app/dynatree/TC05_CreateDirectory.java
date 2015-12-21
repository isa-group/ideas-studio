package es.us.isa.ideas.test.app.dynatree;

import static es.us.isa.ideas.test.app.dynatree.TestSuite.getDir1;
import static es.us.isa.ideas.test.app.dynatree.TestSuite.getProject;
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC05_CreateDirectory extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC05_CreateDirectory...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC05_CreateDirectory finished");
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
    public void step02_createDirectory() {

        // Activate project
        getExpectedActions().click(By.linkText(getProject()));

        if (IdeasStudioActions.expandAddMenu()) {
            getExpectedActions().click(By.linkText("Create Directory"));

            try {
                Thread.sleep(500); // modal animation 
            } catch (InterruptedException e) {
                LOG.severe(e.getMessage());
            }

            // Modal window
            getExpectedActions().sendKeys(By.cssSelector(SELECTOR_MODAL_INPUT), getDir1());
            getExpectedActions().click(By.linkText("Create"));

            try {
                Thread.sleep(500); // modal animation 
            } catch (InterruptedException e) {
                LOG.severe(e.getMessage());
            }

            if (IdeasStudioActions.expandAllDynatreeNodes()) {
                waitForVisibleSelector(SELECTOR_DYNATREE);
                testResult = getWebDriver()
                        .findElement(By.cssSelector(SELECTOR_DYNATREE))
                        .findElements(By.linkText(getDir1())).size() > 0;
            }
        }

        if (testResult) {
            echoCommandApi("Directory \"" + getDir1() + "\" was successfully created.");
        }
        assertTrue(testResult);

    }

}
