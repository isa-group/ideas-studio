package es.us.isa.ideas.test.app.workspaces.switching;

import es.us.isa.ideas.test.app.dynatree.TC09_EditFile2L;
import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.getJs;
import static es.us.isa.ideas.test.utils.TestCase.getWebDriver;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

/**
 *
 * @author danyal
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC09_OpenAfterSwitching extends es.us.isa.ideas.test.utils.TestCase{
    private static boolean testResult = false;
    private static final String FILE_CONTAINER = TestSuite.getProject();
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC09_OpenAfterSwitching...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC09_OpenAfterSwitching finished");
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

            TestCase.getExpectedActions().click(By.linkText(TestSuite.getFileName1() + TestSuite.getFileExt1()));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC09_OpenAfterSwitching.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            testResult = getWebDriver().findElements(By.cssSelector(".ace_editor")).size() > 0;
       
            assertTrue(testResult);

    }

    }
    
    @Test
    public void step03_switchWorkspace() {
        waitForVisibleSelector(SELECTOR_WS_TOGGLER);
        getJs().executeScript("jQuery('" + SELECTOR_WS_TOGGLER + "').click();");
       
        // Animation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
        
        getJs().executeScript("jQuery('.apl_editor_"+TestSuite.getWorkspace1()+ "').click();");

        // Animation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
        
        // Close workspace manager
        waitForVisibleSelector(SELECTOR_WS_LIST);
        getJs().executeScript("jQuery('" + SELECTOR_WS_LIST + "').click();"); // avoid element not clickable exception

        try {
            Thread.sleep(2000); // animation
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        testResult = TestSuite.getWorkspace1().equals(getTextFromSelector(SELECTOR_WS_CURRENT));
        
        assertTrue(testResult);
    }
    
    @Test
    public void step04_openFile2() {

        if (IdeasStudioActions.expandAllDynatreeNodes()) {

            TestCase.getExpectedActions().click(By.linkText(TestSuite.getFileName1() + TestSuite.getFileExt1()));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC99_RemoveWorkspaces.class.getName()).log(Level.SEVERE, null, ex);
            }

            testResult = getWebDriver().findElements(By.cssSelector(".ace_editor")).size() > 0;

        assertTrue(testResult);

    }

    }

}
