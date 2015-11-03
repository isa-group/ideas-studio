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
 * This test will fail if user is redirected to 'exemplar.us.es'.
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC15_ConnectGithubOAuthResponse extends
		es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger
			.getLogger(TC15_ConnectGithubOAuthResponse.class.getName());
	private static boolean testResult = true;
	private static String git_user = "";
	private static String git_pass = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC15_ConnectGithubOAuthResponse...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC15_ConnectGithubOAuthResponse finished");
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
	public void step03_loadGithubSeleniumProperties() {
		git_user = TestCase.getSeleniumGithubUser();
		git_pass = TestCase.getSeleniumGithubPasswd();
		testResult = validatePropertyValues(git_user, git_pass);
		assertTrue(testResult);
	}

	@Test
	public void step04_gitOAuthResponseInExemplar() {

		ExpectedActions action = TestCase.getExpectedActions();
		String selectorConnectGitBtn = "#connect-github";
		String selectorGoToFacebookOAuthPage = "#pagesContent > form > p > button";

		waitForVisibleSelector(selectorConnectGitBtn);
		action.click(By.cssSelector(selectorConnectGitBtn));
		waitForVisibleSelector(selectorGoToFacebookOAuthPage);
		action.click(By.cssSelector(selectorGoToFacebookOAuthPage));

		String selectorGitLogin = "#login_field";
		waitForVisibleSelector(selectorGitLogin);
		action.sendKeys(By.cssSelector(selectorGitLogin), git_user);
		action.sendKeys(By.cssSelector("#password"), git_pass);
		action.click(By
				.cssSelector("#login > form > div.auth-form-body > input.btn"));

		try {
			testResult = !TestCase.isCurrentUrlContains("exemplar.us.es");
		} catch (InterruptedException e) {
			e.printStackTrace();
			testResult = false;
		}

		assertTrue(testResult);

	}

}
