package es.us.isa.ideas.test.app.dynatree;

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
import org.openqa.selenium.Keys;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC08_RenameFile extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final String ORIGIN_FILE_NAME_WITH_EXT = TestSuite.getFileName1() + TestSuite.getFileExt1();
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC08_RenameFile...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC08_RenameFile finished");
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
    public void step01_activateContextMenu() {
        if (IdeasStudioActions.expandAllDynatreeNodes()) {
            testResult = IdeasStudioActions
                    .activateDynatreeContextMenuByFileName(ORIGIN_FILE_NAME_WITH_EXT);
        }
        assertTrue(testResult);
    }

    @Test
    public void step02_existsRenameInput() {
        getExpectedActions().click(By.linkText("Edit"));
        waitForVisibleSelector(SELECTOR_EDIT_NODE_INPUT);
        testResult = getWebDriver().findElement(By.cssSelector(SELECTOR_EDIT_NODE_INPUT)).isDisplayed();
        assertTrue(testResult);
    }

    @Test
    public void step03_submitRenameInput() {
        String targetFileName = "mod_" + ORIGIN_FILE_NAME_WITH_EXT;

        waitForVisibleSelector(SELECTOR_EDIT_NODE_INPUT);
        // Clear input
        getJs().executeScript("jQuery('" + SELECTOR_EDIT_NODE_INPUT + "').focus().val('');");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        // Updates node title
        getExpectedActions()
                .sendKeys(By.cssSelector(SELECTOR_EDIT_NODE_INPUT), targetFileName);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        // Selenium bug. It seems to lose focus sometimes
        getJs().executeScript("jQuery('" + SELECTOR_EDIT_NODE_INPUT + "').focus();");
        //  Submits by [enter] key
        getExpectedActions()
                .sendKeys(By.cssSelector(SELECTOR_EDIT_NODE_INPUT), Keys.RETURN);

        try {
            Thread.sleep(2000); // input hiding
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        // Is the renamed file visible
        testResult = getWebDriver()
                .findElements(By.linkText(targetFileName)).size() > 0;

        if (testResult) {
            echoCommandApi("File \"" + ORIGIN_FILE_NAME_WITH_EXT + "\" was successfully renamed to mod_\"" + ORIGIN_FILE_NAME_WITH_EXT + "\"."); 	// saving file
        }
        assertTrue(testResult);
    }

//    @Test
//    public void step04_checkIdeasRepoFileRenamed () {}
    //TODO: check remote repository if file has been successfully renamed
}
