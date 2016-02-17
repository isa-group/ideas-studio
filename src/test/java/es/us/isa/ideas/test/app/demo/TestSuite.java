package es.us.isa.ideas.test.app.demo;

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
    TC01_CreatePublicDemo.class,
    TC02_CheckDemoLink.class,
    TC03_DeleteDemo.class
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
    
    protected static String getDemoWorkspaceName() {
        return getTestProperty("test.app.dashboard.workspace.demo");
    }
}