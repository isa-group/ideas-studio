package es.us.isa.ideas.test.app.dashboard;

import es.us.isa.ideas.test.utils.TestCase;
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
    TC01_CreatePublicDemoForDashboardTest.class,
    TC02_Login.class,
    TC03_CreateWorkspace.class,
    TC04_OpenWorkspace.class,  
    TC05_DownloadWorkspace.class,
    TC06_EditWorkspace.class,
    TC07_PublishDemo.class,
    TC08_UpdateDemo.class,
    TC09_DepublishDemo.class,
    TC10_DeleteWorkspace.class,
    TC11_CloneDemo.class,
    TC12_DeleteGuestPublicDemo.class
})
public class TestSuite extends TestCase {

    private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "#### Starting Dashboard TestSuite...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "#### Dashboard TestSuite finished");
    }

    protected static String getTestProperty(String key) {
        return getSeleniumGeneralProperties().getProperty(key);
    }

    protected static String getWorkspaceName() {
        return getTestProperty("test.app.dashboard.workspace.name");
    }
    
    protected static String getWorkspaceDescription() {
        return getTestProperty("test.app.dashboard.workspace.description");
    }
    
    protected static String getWorkspaceTags() {
        return getTestProperty("test.app.dashboard.workspace.tags");
    }
    
    protected static String getWorkspaceNewName() {
        return getTestProperty("test.app.dashboard.workspace.name.edited");
    }
    
    protected static String getWorkspaceNewDescription() {
        return getTestProperty("test.app.dashboard.workspace.description.edited");
    }
    
    protected static String getDemoWorkspaceName() {
        return getTestProperty("test.app.dashboard.workspace.demo");
    }
}