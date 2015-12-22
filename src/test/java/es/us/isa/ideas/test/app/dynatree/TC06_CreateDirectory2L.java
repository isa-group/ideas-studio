package es.us.isa.ideas.test.app.dynatree;

import static es.us.isa.ideas.test.app.dynatree.TestSuite.getDir1;
import static es.us.isa.ideas.test.app.dynatree.TestSuite.getDir2;
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
public class TC06_CreateDirectory2L extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC06_CreateDirectory2L...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC06_CreateDirectory2L finished");
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

        if (IdeasStudioActions.expandAllDynatreeNodes()) {

            // Activate parent directory
            getExpectedActions().click(By.linkText(getDir1()));

            if (IdeasStudioActions.expandAddMenu()) {
                getExpectedActions().click(By.linkText("Create Directory"));

                try {
                    Thread.sleep(500); // modal animation 
                } catch (InterruptedException e) {
                    LOG.severe(e.getMessage());
                }

                // Modal window
                getExpectedActions().sendKeys(By.cssSelector("#modalCreationField > input"), getDir2());
                getExpectedActions().click(By.linkText("Create"));

                try {
                    Thread.sleep(500); // modal animation 
                } catch (InterruptedException e) {
                    LOG.severe(e.getMessage());
                }

                IdeasStudioActions.expandAllDynatreeNodes();

                waitForVisibleSelector(SELECTOR_DYNATREE);
                testResult = getWebDriver()
                        .findElement(By.cssSelector(SELECTOR_DYNATREE))
                        .findElements(By.linkText(getDir2())).size() > 0;
            }
        }

        if (testResult) {
            echoCommandApi("Directory \"" + getDir2() + "\" was successfully created.");
        }
        assertTrue(testResult);

    }

}
