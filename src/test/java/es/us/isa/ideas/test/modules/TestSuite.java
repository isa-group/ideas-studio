package es.us.isa.ideas.test.modules;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static es.us.isa.ideas.test.utils.TestCase.getAutotesterPassword;
import org.openqa.selenium.By;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 * 
 * This test executes TestModules command in gcli.
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSuite extends es.us.isa.ideas.test.utils.TestCase {

    private static String user = "";
    private static String pass = "";
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

    @BeforeClass
    public static void setUp() throws InterruptedException {
        LOG.log(Level.INFO, "#### Starting TestModules TestSuite...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "#### TestModules TestSuite finished");
    }

    @Test
	public void step01_goHomePage() {
		
        testResult = IdeasStudioActions.goHomePage();
		assertTrue(testResult);
        
	}

	@Test
	public void step02_loadSeleniumUserProperties() {
		
        user = getAutotesterUser();
		pass = getAutotesterPassword();
		testResult = validatePropertyValues(user, pass);
   		assertTrue(testResult);
        
	}

	@Test
	public void step03_loginWithSeleniumProperties() throws InterruptedException {
		
        if (validatePropertyValues(user, pass)) {
			testResult = loginWithParams(user, pass);
		}
		assertTrue(testResult);
        
	}
	
	@Test
	public void step04_executeTestModules() throws InterruptedException {
        
		LOG.info("Waiting for testModules execution");
        IdeasStudioActions.executeCommands("testModules");
        
        Thread.sleep(5000); // avoid blocking chromedriver when you have to wait a selector after a sychronous request
        waitForVisibleSelector("#testModulesResult");
		testResult = getWebDriver().findElement(By.cssSelector("#testModulesResult")).getText().contains("100%");
		assertTrue(testResult);
        
	}
}
