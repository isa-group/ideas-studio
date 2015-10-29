package es.us.isa.ideas.test.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import es.us.isa.ideas.test.utils.TestCase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	TestSuiteCore.class,
	TestCaseLogin.class
//	TestCaseWS.class
})
public class TestSuiteFull {
	
	private static final Logger LOG = Logger.getLogger(TestSuiteFull.class
			.getName());

	@BeforeClass
	public static void setUp() {
		LOG.log(Level.INFO, "Starting TestSuiteFull...");
	}
	
	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TestSuiteFull finished");
		TestCase.getWebDriver().close();
	}
	
}
