package es.us.isa.ideas.test.app.login;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

import es.us.isa.ideas.test.utils.IdeasAppActions;

/**
 * [issue:#29] This test will fail until Facebook image link located at 'Sign Up'
 * page not works.
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC06_RegisterImageFacebook extends
		es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger
			.getLogger(TC06_RegisterImageFacebook.class.getName());
	private static boolean testResult = true;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC06_RegisterImageFacebook...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC06_RegisterImageFacebook finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goSignUpPage() {
		testResult = IdeasAppActions.goSignUpPage();
		assertTrue(testResult);
	}

	@Test
	public void step02_FacebookOAuthURLExists() {

		String selectorFbImg = "#fb_signin > button > img";
		waitForVisibleSelector(selectorFbImg);
		String fbOAuthRequest = getWebDriver().findElement(
				By.cssSelector("#fb_signin")).getAttribute("action");

		testResult = fbOAuthRequest != "";

		assertTrue(testResult);

	}

	@Test
	public void step03_checkOAuth200StatusCode() throws InterruptedException {

		String fbOAuthRequest = getWebDriver().findElement(
				By.cssSelector("#fb_signin")).getAttribute("action");
		String urlString = getBaseUrl() + fbOAuthRequest;
		String statusCode = getStatusCode(urlString);

		testResult = "200".equals(statusCode);

		assertTrue(testResult);

	}
}
