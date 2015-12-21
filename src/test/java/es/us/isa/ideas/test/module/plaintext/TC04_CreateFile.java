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

import es.us.isa.ideas.test.utils.ExpectedActions;
import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC04_CreateFile extends es.us.isa.ideas.test.utils.TestCase {
	
	private static String fileName = "file";

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC04_CreateFile...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC04_CreateFile finished");
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
	public void step02_createFile() {
		
		ExpectedActions action = TestCase.getExpectedActions();

		LOG.info("testCreateFile :: Creating a file...");

		action.click(By.cssSelector("a.dynatree-title"));
		action.click(By.cssSelector("div#editorSidePanelHeaderAddProject div.dropdown-toggle"));
		action.click(By.linkText("Create Text file"));

		LOG.info("\t :: Inserting \"" + fileName + "\" as file name.");

		action.sendKeys(By.cssSelector("input.form-control.focusedInput"), fileName);
		action.click(By.linkText("Create"));

		action.click(By.cssSelector("span.dynatree-expander"));
		
		boolean ret = false;
		Object jsObj = TestCase.getJs().executeScript("return jQuery('#projectsTree > ul > li > ul > li > span > a').text();");
		String content = "";
		String msg = "";
		if (jsObj != null) {
			content += (String) jsObj;
			ret = content.equals(fileName + ".txt");
			msg += "\t :: File \"" + fileName + ".txt\" was successfully created.";
		}
		LOG.info(msg);
		echoCommandApi(msg);
		
		assertTrue(ret);

	}

}
