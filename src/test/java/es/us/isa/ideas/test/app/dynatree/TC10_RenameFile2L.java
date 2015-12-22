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
public class TC10_RenameFile2L extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final String ORIGIN_FILE_NAME_WITH_EXT = TestSuite.getFileName2() + TestSuite.getFileExt2();
    private static final String TARGET_FILE_NAME_WITH_EXT = "mod_" + ORIGIN_FILE_NAME_WITH_EXT;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC10_RenameFile2L...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC10_RenameFile2L finished");
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
    public void step02_openFile() {
        if (IdeasStudioActions.expandAllDynatreeNodes()) {
            getExpectedActions().click(By.linkText(ORIGIN_FILE_NAME_WITH_EXT));
        }
        testResult = IdeasStudioActions.getActiveNodeName().equals(ORIGIN_FILE_NAME_WITH_EXT);
    }

    @Test
    public void step03_activateContextMenu() {
        if (IdeasStudioActions.expandAllDynatreeNodes()) {
            testResult = IdeasStudioActions
                    .activateDynatreeContextMenuByFileName(ORIGIN_FILE_NAME_WITH_EXT);
        }
        assertTrue(testResult);
    }

    @Test
    public void step04_existsRenameInput() throws InterruptedException {
        String selectorEditNode = "input#editNode";

        getExpectedActions().click(By.linkText("Edit"));
        waitForVisibleSelector(selectorEditNode);
        testResult = getWebDriver().findElement(By.cssSelector(selectorEditNode)).isDisplayed();
        assertTrue(testResult);
    }

    @Test
    public void step05_submitRenameInput() {

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
                .sendKeys(By.cssSelector(SELECTOR_EDIT_NODE_INPUT), TARGET_FILE_NAME_WITH_EXT);

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
                .findElements(By.linkText(TARGET_FILE_NAME_WITH_EXT)).size() > 0;

        if (testResult) {
            echoCommandApi("File \"" + ORIGIN_FILE_NAME_WITH_EXT + "\" was successfully renamed to mod_\"" + ORIGIN_FILE_NAME_WITH_EXT + "\"."); 	// saving file
        }
        assertTrue(testResult);
    }
    
    @Test
    public void step06_checkTabRename() {
        
        try {
            Thread.sleep(1000); // tab animation
        } catch (InterruptedException ex) {
            Logger.getLogger(TC10_RenameFile2L.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String selectorTab = "div#editorMainPanel ul#editorTabs > li > a";
        String selectorTabText = getWebDriver().findElement(By.cssSelector(selectorTab)).getText();
        
        testResult = selectorTabText.equals(TARGET_FILE_NAME_WITH_EXT);
        assertTrue(testResult);
        
    }
    
    @Test
    public void step07_onlyOneActiveTab() {
        
        getExpectedActions().click(By.linkText("mod_" + TestSuite.getFileName1() + TestSuite.getFileExt1()));
        
        try {
            Thread.sleep(1000); // tab animation
        } catch (InterruptedException ex) {
            Logger.getLogger(TC10_RenameFile2L.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Reactivate previous tab
        getExpectedActions().click(By.linkText(TARGET_FILE_NAME_WITH_EXT));
        
        try {
            Thread.sleep(1000); // tab animation
        } catch (InterruptedException ex) {
            Logger.getLogger(TC10_RenameFile2L.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        testResult = getWebDriver().findElements(By.cssSelector(SELECTOR_TAB_ACTIVE)).size() == 1;
        assertTrue(testResult);
        
    }
    
}
