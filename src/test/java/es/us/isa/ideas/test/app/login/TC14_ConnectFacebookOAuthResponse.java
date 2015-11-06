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

import es.us.isa.ideas.test.utils.ExpectedActions;
import es.us.isa.ideas.test.utils.IdeasAppActions;
import es.us.isa.ideas.test.utils.TestCase;

/**
 * This test will fail when the application base URL is different from
 * 'https://localhost:8181/ideas-studio/'.
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC14_ConnectFacebookOAuthResponse extends
		es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger
			.getLogger(TC14_ConnectFacebookOAuthResponse.class.getName());
	private static boolean testResult = true;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC14_ConnectFacebookOAuthResponse...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC14_ConnectFacebookOAuthResponse finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_login() {
		try {
			testResult = TestCase.login();
		} catch (InterruptedException e) {
			e.printStackTrace();
			testResult = false;
		}
		assertTrue(testResult);
	}

	@Test
	public void step02_goSocialPage() {
		testResult = IdeasAppActions.goSocialPage();
		assertTrue(testResult);
	}

	@Test
	public void step03_fbOAuthResponse() {

		ExpectedActions action = TestCase.getExpectedActions();
		String selectorConnectFbBtn = "#connect-facebook";
		String selectorGoToFacebookOAuthPage = "#pagesContent > form > p > button";

		waitForVisibleSelector(selectorConnectFbBtn);
		action.click(By.cssSelector(selectorConnectFbBtn));
		waitForVisibleSelector(selectorGoToFacebookOAuthPage);
		action.click(By.cssSelector(selectorGoToFacebookOAuthPage));

		if (!getCurrentUrl().contains("https://www.facebook.com")) {

			String selectorFbResponse = "#u_0_0 > div > div";
			waitForVisibleSelector(selectorFbResponse);
			String errorMsg = TestCase.getWebDriver()
					.findElement(By.cssSelector(selectorFbResponse)).getText();

			boolean ret_en = errorMsg
					.contains("Given URL is not allowed by the Application configuration");

			boolean ret_es = errorMsg
					.contains("La configuración de la aplicación no permite la URL proporcionada");

			testResult = ret_en || ret_es;

		}

		assertTrue(testResult);

	}

}
