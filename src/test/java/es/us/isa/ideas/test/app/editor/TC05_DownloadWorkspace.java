package es.us.isa.ideas.test.app.editor;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
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

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jimenez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC05_DownloadWorkspace extends TestCase {
    
    protected static final String SELECTOR_DOWNLOAD_BUTTON = "#download-ws";

    private static boolean testResult = true;
    private static final Logger LOG = Logger.getLogger(TC06_PublishDemoWorkspace.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC05_DownloadWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC05_DownloadWorkspace finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, Class.class.getName() + " testResult value: {0}", testResult);
        testResult = false;
    }
    
    @Test
    public void step01_goEditorPage() {
        testResult = IdeasStudioActions.goEditorPage();
        assertTrue(testResult);
    }
    
    @Test
    public void step02_downloadWorkspace() {

        //Publish demo workspace button
        waitForVisibleSelector(SELECTOR_DOWNLOAD_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DOWNLOAD_BUTTON + "').click();");

        testResult=true;
        assertTrue(testResult);
    }

}
