package es.us.isa.ideas.test.app.login;

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
import es.us.isa.ideas.test.utils.IdeasStudioActions;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC01_RegisterMaxLengthFormField extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TC01_RegisterMaxLengthFormField.class.getName());
	private static boolean testResult = true;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC02_RegisterMaxLengthFormField...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC02_RegisterMaxLengthFormField finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goToSignUpPage() {
		testResult = IdeasStudioActions.goSignUpPage();
		assertTrue(testResult);
	}

	@Test
	public void step02_insertBigStringToFormFields() {

		ExpectedActions action = getExpectedActions();
		String bigString = "1729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734";

		action.sendKeys(By.id("name"), bigString);
		action.sendKeys(By.id("email"), bigString);
		action.sendKeys(By.id("phone"), bigString);
		action.sendKeys(By.id("address"), bigString);

		testResult = !getInputValueSelector("#name").equals("") && !getInputValueSelector("#email").equals("")
				&& !getInputValueSelector("#phone").equals("") && !getInputValueSelector("#address").equals("");

		assertTrue(testResult);

	}

	@Test
	public void step03_submit() {

		getExpectedActions().click(By.cssSelector("#settingsSubmitChanges"));

		String selectorModalHeader = "#loginFailPanel > div > div > div.modal-header > h4";
		waitForVisibleSelector(selectorModalHeader);

		String modalHeader = getTextFromSelector(selectorModalHeader);
		String modalBody = getTextFromSelector("#loginFailPanel > div > div > div.modal-body > p");
		String msgInvalidEmail = getTextFromSelector("#email\\.errors");

		boolean ret1 = modalHeader.equals("Sign up error");
		boolean ret2 = modalBody.equals("Has not been able to make sign up due to errors in the fields");
		boolean ret3 = "Invalid email".equals(msgInvalidEmail);

		testResult = ret1 && ret2 && ret3;

		assertTrue(testResult);

	}

}
