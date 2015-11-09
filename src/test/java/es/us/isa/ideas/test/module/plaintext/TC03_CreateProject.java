package es.us.isa.ideas.test.module.plaintext;

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

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC03_CreateProject extends es.us.isa.ideas.test.utils.TestCase {

	private static String projectName = "Project";

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC02_CreateWorkspace...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC02_CreateWorkspace finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goEditorPage() {
		testResult = IdeasStudioActions.goEditorPage();
		assertTrue(testResult);
	}
	
	@Test
	public void step02_createProject() {
		LOG.info("testCreateProject :: Creating a project...");

		TestCase.getExpectedActions().click(By.cssSelector("div#editorSidePanelHeaderAddProject div.dropdown-toggle"));
		TestCase.getExpectedActions().click(By.linkText("Create Project"));

		LOG.info("\t :: Inserting \"" + projectName + "\" as name project.");

		TestCase.getExpectedActions().sendKeys(By.cssSelector("input.form-control.focusedInput"), projectName);
		TestCase.getExpectedActions().click(By.linkText("Create"));
		
		boolean ret = false;
		Object jsObj = TestCase.getJs().executeScript("return jQuery('#projectsTree > ul > li > span > a').text();");
		String content = "";
		String msg = "";
		if (jsObj != null) {
			content += (String) jsObj;
			ret = content.equals(projectName);
			msg += "\t :: Project \"" + projectName + "\" was successfully created.";
		}
		LOG.info(msg);
		echoCommandApi(msg);
		
		assertTrue(ret);
		
	}

}
