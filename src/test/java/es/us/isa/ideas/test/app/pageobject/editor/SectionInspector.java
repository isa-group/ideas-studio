/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.utils.FileType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class SectionInspector extends EditorPage {

    @FindBy(id = "editorToggleInspector")
    WebElement inspectorToggleSpan;
    
    @FindBy(css = "#inspectorModelContent > a.buildExampleFormCreator")
    WebElement buildExampleFormCreatorAnchor;
    
    @FindBy(id = "inspectorModelContent")
    WebElement inspectorModelContentContainer;

    // Page functionalities
    public static SectionInspector navigateTo() {
        EditorPage.navigateTo();
        return PageFactory.initElements(getWebDriver(), SectionInspector.class);
    }
    
    // Click
    
    public SectionInspector clickOnInspectorToggle() {
        clickOnNotClickableElement(inspectorToggleSpan);
        return PageFactory.initElements(getWebDriver(), SectionInspector.class);
    }
    
    public SectionInspector clickOnBuildExampleFormCreator() {
        clickOnClickableElement(buildExampleFormCreatorAnchor);
        return PageFactory.initElements(getWebDriver(), SectionInspector.class);
    }
    
    // Getters
    
    public Boolean isInspectorOpened() {
        PageObject.getWebDriverWait()
            .until(ExpectedConditions.visibilityOf(inspectorToggleSpan));
        
        return inspectorToggleSpan.getAttribute("class").contains("hdd");
    }
    

    // Test facade
    public static void testOpenInspector() {
        new SectionInspectorTestCase().testOpenInspector();
    }

    public static void testInspectorFormTabStructureLoaded() {
        new SectionInspectorTestCase().testInspectorFormTabStructureLoaded();
    }

    public static void testBuildExampleFormFromFilename(String fileName) {
        new SectionInspectorTestCase().testBuildExampleFormFromFilename(fileName);
    }

    public static void testInspectorFormTabContentContains(String content) {
        new SectionInspectorTestCase().testInspectorFormTabContentContains(content);
    }

    /**
     * This class implements all tests related to files management.
     */
    private static class SectionInspectorTestCase extends TestCase {

        /**
         * Check if inspector can be opened. If it already opened, test won't
         * fail.
         */
        public void testOpenInspector() {

            SectionInspector inspector = SectionInspector.navigateTo();

            if ( !inspector.isInspectorOpened() ) {
                inspector.clickOnInspectorToggle();

                try {
                    Thread.sleep(1000); // open animation
                } catch (InterruptedException ex) {
                    Logger.getLogger(SectionInspector.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                // inspector is already opened
                // do nothing
            }

            TEST_RESULT = inspector.isInspectorOpened();
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }
        
        /**
         * Check if creating a file form from a fileName opens a form file with the same name.
         * @param fileName 
         */
        public void testBuildExampleFormFromFilename(String fileName) {
            
            SectionInspector inspector = SectionInspector.navigateTo();
            
            // Make sure inspector is opened
            SectionInspector.testOpenInspector();
            
            // Is button visible
            PageObject.getWebDriverWait().until(ExpectedConditions.visibilityOf(inspector.buildExampleFormCreatorAnchor));
            
            // Click on create example form
            inspector.clickOnBuildExampleFormCreator();
            
            try {
                Thread.sleep(1000); // creating a new file
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionInspector.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            By formFileLocator = By.linkText(fileName + FileType.ANGULAR);
            PageObject.getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(formFileLocator));
            
            TEST_RESULT = inspector.isFileTabActivatedByLocator(formFileLocator);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);
            
        }
        
        /**
         * Check if inspector form tab contains a param content.
         * @param content 
         */
        public void testInspectorFormTabContentContains(String content) {
            
            SectionInspector inspector = SectionInspector.navigateTo();
            
            PageObject.getWebDriverWait().until(ExpectedConditions.visibilityOf(inspector.inspectorModelContentContainer));
            
            TEST_RESULT = inspector.inspectorModelContentContainer.getText().contains(content);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);
            
        }
        
        public void testInspectorFormTabStructureLoaded() {
            // nothing yet
        }

    }

}
