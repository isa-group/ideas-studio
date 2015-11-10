package es.us.isa.ideas.test.module;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Before executing this TestSuite, be sure the user specified in
 * selenium.propeties has no workspace created.
 * 
 * @author feserafim
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ es.us.isa.ideas.test.module.plaintext.TestSuite.class })
public class TestSuite {

	private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Starting modules TestSuite...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "Modules TestSuite finished");
	}

}
