package es.us.isa.ideas.test.app.dynatree;

import static es.us.isa.ideas.test.app.dynatree.TestSuite.getFileExt2;
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
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC09_EditFile2L extends es.us.isa.ideas.test.utils.TestCase {

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

            TestCase.getExpectedActions().click(By.linkText(getFileName2() + getFileExt2()));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC09_EditFile2L.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Set editor content
            IdeasStudioActions.setCurrentEditorContent("Contenido fichero " + getFileName2() + getFileExt2());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TC09_EditFile2L.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Check if file content is not empty
            testResult = !IdeasStudioActions.isEditorContentEmpty();
        }

        if (testResult) {
            echoCommandApi("File \"" + getFileName2() + getFileExt2() + "\" was successfully edited.");
        }
        assertTrue(testResult);

    }

}
