package es.us.isa.ideas.test.app.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TC01_Register.class,
	TC02_RegisterMaxLengthFormField.class,
	TC03_RegisterGoogleOAuthResponse.class,
	TC04_RegisterTwitter.class,
	TC05_RegisterImageTwitter.class,
	TC06_RegisterImageFacebook.class,
	TC07_SignUpImages.class,
	TC08_Login.class,
	TC09_TwitterLogin.class,
	TC11_RenameUsernameFromLoginInformationTab.class,
	TC12_ResetPassword.class
})
public class TestSuite extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TestSuite.class
			.getName());

	@BeforeClass
	public static void setUp() {
		LOG.log(Level.INFO, "Starting Login TestSuite...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "Login TestSuite finished");
		getWebDriver().close();
	}

}
