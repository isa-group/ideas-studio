package es.us.isa.ideas.test.module.plaintext;

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
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TC01_Login.class, TC02_CreateWorkspace.class, TC03_CreateProject.class, TC04_CreateFile.class,
		TC05_EditFile.class, TC06_RemoveCurrentWorkspace.class })
public class TestSuite extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

	@BeforeClass
	public static void setUp() {
		LOG.log(Level.INFO, "Starting PlainText language module TestSuite...");
	}

	@AfterClass
	public static void tearDown() throws InterruptedException {
		LOG.log(Level.INFO, "Login PlainText language module finished");
	}
    
    protected static String getTestProperty(String key) {
        return getSeleniumGeneralProperties().getProperty(key);
    }

    protected static String getWorkspace() {
        return getTestProperty("test.app.plaintext.workspace");
    }

    protected static String getProject() {
        return getTestProperty("test.app.plaintext.project");
    }
    
    protected static String getFileName1() {
        return getTestProperty("test.app.plaintext.file1.name");
    }
    
    protected static String getFileExt1() {
        return getTestProperty("test.app.plaintext.file1.extension");
    }

}