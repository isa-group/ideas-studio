package es.us.isa.ideas.test.module.plaintext;

import static es.us.isa.ideas.test.module.plaintext.TestSuite.getFileExt1;
import static es.us.isa.ideas.test.module.plaintext.TestSuite.getFileName1;
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
public class TC05_EditFile extends es.us.isa.ideas.test.utils.TestCase {

    private static boolean testResult = true;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() throws InterruptedException {
        LOG.log(Level.INFO, "Init TC09_OpenFile2L...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "TC09_OpenFile2L finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, "testResult value: {0}", testResult);
    }

    @Test
    public void step01_goEditorPage() {
        testResult = IdeasStudioActions.goEditorPage();
        assertTrue(testResult);
    }

    @Test
    public void step02_editFile() {

        if (IdeasStudioActions.expandAllDynatreeNodes()) {

            TestCase.getExpectedActions().click(By.linkText(getFileName1() + getFileExt1()));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC05_EditFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Set editor content
            IdeasStudioActions.setCurrentEditorContent("Contenido fichero " + getFileName1() + getFileExt1());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC05_EditFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Check if file content is not empty
            testResult = !IdeasStudioActions.isEditorContentEmpty();
        }

        if (testResult) {
            echoCommandApi("File \"" + getFileName1() + getFileExt1() + "\" was successfully edited.");
        }
        assertTrue(testResult);

    }

}
