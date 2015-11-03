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
 * This test will fail until the "Login information" tab located in
 * "User Account" page has a editable username form field.
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC11_RenameUsernameFromLoginInformationTab extends
		es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger
			.getLogger(TC11_RenameUsernameFromLoginInformationTab.class
					.getName());
	private static boolean testResult = true;
	private static String prevUsername = "";
	private static String actualUsername = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC11_RenameUsernameFromLoginInformationTab...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC11_RenameUsernameFromLoginInformationTab finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}
	
	@Test
	public void step01_login() throws InterruptedException {
		testResult = TestCase.login();
		assertTrue(testResult);
	}

	@Test
	public void step02_goUserAccountPage() {
		testResult = IdeasAppActions.goUserAccountPage();
		assertTrue(testResult);
	}

	@Test
	public void step03_modifyUsernameFormField() {

		ExpectedActions action = getExpectedActions();
		String selectorLogInfo = "#accountTab";
		String selectorUsernameInput = "#userAccount\\2e username";

		// open login information tab
		waitForVisibleSelector(selectorLogInfo);
		action.click(By.cssSelector(selectorLogInfo));

		// getting actual user name
		waitForVisibleSelector(selectorUsernameInput);
		prevUsername += getInputValueSelector(selectorUsernameInput);
		action.sendKeys(By.cssSelector(selectorUsernameInput), "2");

		testResult = getInputValueSelector(selectorUsernameInput).endsWith("2");
		assertTrue(testResult);

	}

	@Test
	public void step04_renameUsername() {

		ExpectedActions action = getExpectedActions();
		String selectorLogInfo = "#accountTab";
		String selectorUsernameInput = "#userAccount\\2e username";

		action.click(By.cssSelector("#settingsSubmitChanges"));
		waitForVisibleSelector(selectorLogInfo);
		action.click(By.cssSelector(selectorLogInfo));

		actualUsername += getInputValueSelector(selectorUsernameInput);

		testResult = !actualUsername.equals(prevUsername);
		assertTrue(testResult);

	}

}
