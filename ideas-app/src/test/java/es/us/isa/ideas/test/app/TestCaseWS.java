package es.us.isa.ideas.test.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import es.us.isa.ideas.test.utils.ExpectedActions;
import es.us.isa.ideas.test.utils.IdeasAppActions;
import es.us.isa.ideas.test.utils.TestCase;

public class TestCaseWS {
	
	private static final Logger LOG = Logger.getLogger(TestCaseWS.class
			.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {

		LOG.log(Level.INFO, "Starting TestSuiteWS...");

		TestCase.logout();
		TestCase.login();

	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TestSuiteWS finished");
	}
	
	/**
	 * No se puede renombrar un proyecto.
	 * @throws InterruptedException 
	 */
	@Test
	public void testRenameAndIntro() throws InterruptedException {
		
		IdeasAppActions.goEditorPage();
		
		ExpectedActions action = TestCase.getExpectedActions();
		
//		IdeasAppActions.expandMenu("DefaultProject");
		Thread.sleep(500);	// animation
		action.contextNodeMenu(By.cssSelector("#projectsTree > ul > li > span > a"));
		Thread.sleep(500);	// animation
		
		TestCase.getExpectedActions().click(By.linkText("Edit"));
		TestCase.getExpectedActions().sendKeys(By.cssSelector("input#editNode"), "mod_",
				Keys.RETURN);
		
	}

}
