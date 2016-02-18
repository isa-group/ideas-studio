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
public class TC04_CreateFile extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = false;
    private static final String FILE_CONTAINER = TestSuite.getProject();
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC04_CreateFile...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC04_CreateFile finished");
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

        getExpectedActions().click(By.linkText(FILE_CONTAINER)); // activate project
        IdeasStudioActions.expandAddMenu();
        getExpectedActions().click(By.linkText("Create Text file"));

        try {
            Thread.sleep(1000); // modal animation 
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        // Modal window
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_MODAL_INPUT), TestSuite.getFileName1());
        getExpectedActions().click(By.linkText("Create"));

        try {
            Thread.sleep(1000); // modal animation 
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        IdeasStudioActions.expandAllDynatreeNodes();
        
        testResult = getWebDriver()
                .findElements(By.linkText(TestSuite.getFileName1() + TestSuite.getFileExt1())).size() > 0;

        if (testResult) {
            echoCommandApi("File \"" + TestSuite.getFileName1() + TestSuite.getFileExt1() + "\" was successfully created.");
        }
        assertTrue(testResult);
    }
    
    @Test
    public void step03_editFile() {

        if (IdeasStudioActions.expandAllDynatreeNodes()) {

            TestCase.getExpectedActions().click(By.linkText(TestSuite.getFileName1() + TestSuite.getFileExt1()));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC04_CreateFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Set editor content
            IdeasStudioActions.setCurrentEditorContent("Contenido fichero " + TestSuite.getFileName1() + TestSuite.getFileExt1());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC04_CreateFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Check if file content is not empty
            testResult = !IdeasStudioActions.isEditorContentEmpty();
        }

        if (testResult) {
            echoCommandApi("File \"" + TestSuite.getFileName1() + TestSuite.getFileExt1() + "\" was successfully edited.");
        }
        assertTrue(testResult);

    }

}
