package es.us.isa.ideas.test.module.plaintext;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TC01_Login.class,
	TC02_CreateWorkspace.class,
	TC03_CreateProject.class,
	TC04_CreateFile.class,
	TC05_EditFile.class
})
public class TestSuite extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TestSuite.class
			.getName());

	@BeforeClass
	public static void setUp() {
		LOG.log(Level.INFO, "Starting PlainText language module TestSuite...");
	}

	@AfterClass
	public static void tearDown() throws InterruptedException {
		
		logout();
		getWebDriver().close();
		
		LOG.log(Level.INFO, "Login PlainText language module finished");
		
	}
	
	

}