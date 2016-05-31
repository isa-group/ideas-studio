package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModulesTestCase extends TestCase {

    @Test
    public void step01_login() {
        LoginPage.testLogin("autotester", "autotester");
    }

    @Test
    public void step02_testModulesCommand() {
        EditorPage.navigateTo()
            .executeCommand("testModules", Keys.RETURN);
        
        By locator = By.id("testModulesResult");
        String testResult = PageObject.getWebDriver().findElement(locator).getText();
		TEST_RESULT = testResult.contains("100%");
		assertTrue(TEST_RESULT);
    }

}
