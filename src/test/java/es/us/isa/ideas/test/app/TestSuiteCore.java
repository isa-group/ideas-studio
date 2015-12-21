package es.us.isa.ideas.test.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import es.us.isa.ideas.test.utils.TestCase;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	//empty
})
public class TestSuiteCore {
	
	private static final Logger LOG = Logger.getLogger(TestSuiteCore.class
			.getName());

	@BeforeClass
	public static void setUp() {
		LOG.log(Level.INFO, "Starting TestSuiteCore...");
	}
	
	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TestSuiteCore finished");
		TestCase.getWebDriver().close();
	}
	
}
