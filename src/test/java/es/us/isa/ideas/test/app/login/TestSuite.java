package es.us.isa.ideas.test.app.login;

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
@Suite.SuiteClasses({
    TC01_RegisterMaxLengthFormField.class, 
    TC02_RegisterTwitter.class, 
    TC03_Register.class,
    TC04_RecoverPassword.class
})
public class TestSuite extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

	@BeforeClass
	public static void setUp() {
		LOG.log(Level.INFO, "Starting Login TestSuite...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "Login TestSuite finished");
	}

}
