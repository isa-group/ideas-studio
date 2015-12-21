package es.us.isa.ideas.test.app.dynatree;

import static es.us.isa.ideas.test.app.dynatree.TestSuite.getDir2;
import static es.us.isa.ideas.test.app.dynatree.TestSuite.getFileName2;
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
import static es.us.isa.ideas.test.utils.TestCase.echoCommandApi;
import static es.us.isa.ideas.test.utils.TestCase.getExpectedActions;
import static es.us.isa.ideas.test.utils.TestCase.getWebDriver;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import static es.us.isa.ideas.test.app.dynatree.TestSuite.getFileExt2;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC07_CreateFile extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final String FILE_CONTAINER = getDir2();
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC07_CreateFile...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC07_CreateFile finished");
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
    public void step02_createFile() {

        if (IdeasStudioActions.expandAllDynatreeNodes()) {

            // Activate directory
            getExpectedActions().click(By.linkText(FILE_CONTAINER));

            if (IdeasStudioActions.expandAddMenu()) {

                getExpectedActions().click(By.linkText("Create Text file"));

                try {
                    Thread.sleep(1000); // modal animation 
                } catch (InterruptedException e) {
                    LOG.severe(e.getMessage());
                }

                // Modal window
                getExpectedActions().sendKeys(By.cssSelector(SELECTOR_MODAL_INPUT), getFileName2());
                getExpectedActions().click(By.linkText("Create"));

                try {
                    Thread.sleep(1000); // modal animation 
                } catch (InterruptedException e) {
                    LOG.severe(e.getMessage());
                }

                // Expand project
                getExpectedActions().click(By.linkText(FILE_CONTAINER));

                waitForVisibleSelector(SELECTOR_DYNATREE);
                testResult = getWebDriver()
                        .findElement(By.cssSelector(SELECTOR_DYNATREE))
                        .findElements(By.linkText(getFileName2() + getFileExt2())).size() > 0;

            }
        }

        if (testResult) {
            echoCommandApi("File \"" + getFileName2() + getFileExt2() + "\" was successfully created.");
        }
        assertTrue(testResult);
    }

}
