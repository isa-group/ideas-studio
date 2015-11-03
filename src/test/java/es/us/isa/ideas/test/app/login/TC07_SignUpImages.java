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
import es.us.isa.ideas.test.utils.TestCase;

/**
 * [issue:#29] This test will fail until Twitter and Facebook images on 'Sign
 * Up' page are not load.
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC07_SignUpImages extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TC07_SignUpImages.class
			.getName());
	private static boolean testResult = true;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC07_SignUpImages...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC07_SignUpImages finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goToSignUpPage() {
		testResult = IdeasAppActions.goSignUpPage();
		assertTrue(testResult);
	}

	@Test
	public void step02_getImagesSrc() {

		String srcTwImg = TestCase.getWebDriver()
				.findElement(By.cssSelector("#tw_signin > button > img"))
				.getAttribute("src");
		String srcFbImg = TestCase.getWebDriver()
				.findElement(By.cssSelector("#fb_signin > button > img"))
				.getAttribute("src");

		testResult = !srcTwImg.equals("") && !srcFbImg.equals("");

		assertTrue(testResult);

	}

	@Test
	public void step03_getImagesSrc200StatusCode() throws InterruptedException {

		String srcTwImg = TestCase.getWebDriver()
				.findElement(By.cssSelector("#tw_signin > button > img"))
				.getAttribute("src");
		String srcFbImg = TestCase.getWebDriver()
				.findElement(By.cssSelector("#fb_signin > button > img"))
				.getAttribute("src");

		testResult = getStatusCode(srcTwImg).equals("200")
				&& getStatusCode(srcFbImg).equals("200");

		assertTrue(testResult);

	}

}
