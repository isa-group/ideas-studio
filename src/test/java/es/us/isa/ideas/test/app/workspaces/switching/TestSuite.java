package es.us.isa.ideas.test.app.workspaces.switching;

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
    TC01_Login.class,
    TC02_CreateWorkspace1.class,
    TC03_CreateProject.class,
    TC04_CreateFile.class,
    TC05_CreateWorkspace2.class,
    TC06_CreateProject.class,
    TC07_CreateFile.class,
    TC09_OpenAfterSwitching.class,
    TC99_RemoveWorkspaces.class
})
public class TestSuite extends TestCase {

    private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "#### Starting Workspaces TestSuite...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "#### Dashboard Workspaces finished");
    }

    protected static String getTestProperty(String key) {
        return getSeleniumGeneralProperties().getProperty(key);
    }

    protected static String getWorkspace1() {
        return getTestProperty("test.app.dynatree.workspace")+"1";
    }
    
    protected static String getWorkspace2() {
        return getTestProperty("test.app.dynatree.workspace")+"2";
    }

    protected static String getProject() {
        return getTestProperty("test.app.dynatree.project");
    }
    
    protected static String getFileName1() {
        return getTestProperty("test.app.dynatree.file1.name");
    }
    
    protected static String getFileExt1() {
        return getTestProperty("test.app.dynatree.file1.extension");
    }
    
    protected static String getFileName2() {
        return getTestProperty("test.app.dynatree.file2.name");
    }
    
    protected static String getFileExt2() {
        return getTestProperty("test.app.dynatree.file2.extension");
    }
    
}