/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.editor;


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
 * @author Daniel Francisco Alonso Jimenez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC06_PublishDemoWorkspace extends TestCase{

    protected static final String SELECTOR_PUBLISH_BUTTON = "#demo-ws";
    private static final String SELECTOR_DEMO_CARD_TITLE = ".demoworkspace .card__meta-content-title";
    
    private static boolean testResult = true;
    private static final Logger LOG = Logger.getLogger(TC06_PublishDemoWorkspace.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC06_PublishDemoWorkspace...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC06_PublishDemoWorkspace finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, Class.class.getName() + " testResult value: {0}", testResult);
        testResult = false;
    }
    
    @Test
    public void step01_goEditorPage() {
        testResult = IdeasStudioActions.goEditorPage();
        assertTrue(testResult);
    }
    
    @Test
    public void step02_publishDemoWorkspace() {

        //Publish demo workspace button
        waitForVisibleSelector(SELECTOR_PUBLISH_BUTTON);
        getJs().executeScript("jQuery('" + SELECTOR_PUBLISH_BUTTON + "').click();");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
        
        // Modal window
        waitForVisibleSelector(SELECTOR_MODAL_CONTINUE);
        getJs().executeScript("jQuery('" + SELECTOR_MODAL_CONTINUE + "').click();");
        
        IdeasStudioActions.goWSMPage();
        
        waitForVisibleSelector(SELECTOR_DEMO_CARD_TITLE);
        testResult = getWebDriver().findElements(By.cssSelector(SELECTOR_DEMO_CARD_TITLE)).size() > 0;
                
        assertTrue(testResult);
    }
    
}
