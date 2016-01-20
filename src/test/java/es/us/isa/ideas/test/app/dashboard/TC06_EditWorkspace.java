/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.dashboard;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.getExpectedActions;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC06_EditWorkspace extends TestCase{
    
    private static final String SELECTOR_WORSPACE_CARD_TITLE= ".workspace  .card__meta-content-title";
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC05_EditWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC05_EditWorkspace finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, "testResult: {0}", testResult);
        testResult = false;
    }
    
    @Test
    public void step01_goDashboardPage() {
        testResult = IdeasStudioActions.goWSMPage();
        assertTrue(testResult);
    }
    
    @Test
    public void step02_openWorkspaceEditForm() {

        waitForVisibleSelector(SELECTOR_DASHBOARD_WORKSPACE_CARD_EDIT_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_WORKSPACE_CARD_EDIT_BUTTON + "').click();");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC11_CloneDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        waitForVisibleSelector(SELECTOR_WORKSPACE_FORM_INPUT_NAME);

        testResult = getWebDriver().findElement(By.cssSelector(SELECTOR_WORKSPACE_FORM_INPUT_NAME)).isDisplayed();
        assertTrue(testResult);

    }
    
    @Test
    public void step03_editWorkspace() {

        waitForVisibleSelector(SELECTOR_WORKSPACE_FORM_INPUT_NAME);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_WORKSPACE_FORM_INPUT_NAME), " Edited");
        waitForVisibleSelector(SELECTOR_WORKSPACE_FORM_INPUT_DESCRIPTION);
        getExpectedActions().sendKeys(By.cssSelector(SELECTOR_WORKSPACE_FORM_INPUT_DESCRIPTION), " Edited");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC11_CloneDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        IdeasStudioActions.goWSMPage();

        waitForVisibleSelector(SELECTOR_WORSPACE_CARD_TITLE);
        testResult = getWebDriver().findElement(By.cssSelector(SELECTOR_WORSPACE_CARD_TITLE)).isDisplayed();

        assertTrue(testResult);
    }

}
