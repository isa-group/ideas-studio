package es.us.isa.ideas.test.app.editor;

import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.getSeleniumGeneralProperties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TC01_LoginAutotester.class,
    TC02_CreateWorkspace.class,
    TC04_EditWorkspace.class,
    TC05_DownloadWorkspace.class,
    TC06_PublishDemoWorkspace.class,
    TC07_DeleteWorkspace.class,
    TC08_LoginDemomaster.class,
    TC09_DeleteWorkspace.class
    
})
public class TestSuite extends TestCase {

    private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "#### Starting editor left menu workspace actions TestSuite...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "#### Starting editor left menu workspace actions TestSuite finished");
    }

    protected static String getTestProperty(String key) {
        return getSeleniumGeneralProperties().getProperty(key);
    }
    
    protected static String getWorkspaceName() {
        return getTestProperty("test.app.editor.workspace.name");
    }
    
    protected static String getWorkspaceDescription() {
        return getTestProperty("test.app.editor.workspace.description");
    }
    
    protected static String getWorkspaceTags() {
        return getTestProperty("test.app.editor.workspace.tags");
    }
    
    protected static String getWorkspaceNewName() {
        return getTestProperty("test.app.editor.workspace.name.edited");
    }
}