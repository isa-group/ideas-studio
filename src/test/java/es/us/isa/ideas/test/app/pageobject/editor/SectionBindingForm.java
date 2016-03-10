/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain.
 * This section refers to binding test based on JSON model. 
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class SectionBindingForm extends EditorPage {
    
    // Format view
    
    @FindBy(css = "#editorWrapper .addSlaButton")
    WebElement editorAddSlaButton;
    
    @FindAll(@FindBy(css = "#editorWrapper .ag-template"))
    List<WebElement> editorConstraintCards;
    
    // Inspector
    
    @FindBy(css = "#modelInspectorContent .addSlaButton")
    WebElement inspectorAddSlaButton;
    
    @FindAll(@FindBy(css = "#inspectorModelContent .ag-template"))
    List<WebElement> inspectorConstraintCards;
    
    public static SectionBindingForm navigateTo() {
        EditorPage.navigateTo();
        return PageFactory.initElements(getWebDriver(), SectionBindingForm.class);
    }
    
    // Click
    
    public SectionBindingForm clickOnEditorAddSlaButton() {
        clickOnNotClickableElement(editorAddSlaButton);
        return PageFactory.initElements(getWebDriver(), SectionBindingForm.class);
    }
    
    public SectionBindingForm clickOnInspectorAddSlaButton() {
        clickOnNotClickableElement(inspectorAddSlaButton);
        return PageFactory.initElements(getWebDriver(), SectionBindingForm.class);
    }
    
    public Integer getEditorConstraintCardSize() {
        PageObject.getWebDriverWait().until(ExpectedConditions.visibilityOfAllElements(editorConstraintCards));
        return editorConstraintCards.size();
    }
    
    public Integer getInspectorConstraintCardSize() {
        PageObject.getWebDriverWait().until(ExpectedConditions.visibilityOfAllElements(inspectorConstraintCards));
        return inspectorConstraintCards.size();
    }
    
    // Test facade

    public static void testFormatViewAddSlaButton() {
        new SectionBindingFormTestCase().testFormatViewAddSlaButton();
    }
    
    public static void testNumberOfCreationConstraints(Integer expectedNumber) {
        new SectionBindingFormTestCase().testNumberOfCreationConstraints(expectedNumber);
    }
    
    public static void testNumberInspectorConstraintCards(Integer expectedNumber) {
        new SectionBindingFormTestCase().testNumberInspectorConstraintCards(expectedNumber);
    }

    private static class SectionBindingFormTestCase extends TestCase {
        
        /**
         * Check if you press addSlaButton it creates a new creation constraint 
         * in the modal.
         */
        public void testFormatViewAddSlaButton() {
            
            SectionBindingForm form = SectionBindingForm.navigateTo();
            Integer initialSize = form.getEditorConstraintCardSize();
            
            // Add a new sla constraint
            form.clickOnEditorAddSlaButton();
            
            try {
                Thread.sleep(1000); // animation
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionBindingForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Integer finalSize = form.getEditorConstraintCardSize();
            
            // Check if current formatTab is still FORM
            SectionEditorFormatTab.testIsEditorFormatActivated("FORM");
            
            TEST_RESULT = finalSize == initialSize + 1;
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);
            
        }
        
        /**
         * Check if the current number of creation constraints as expected.
         * @param expectedNumber 
         */
        public void testNumberOfCreationConstraints(Integer expectedNumber) {
            
            SectionBindingForm form = SectionBindingForm.navigateTo();
            
            // Javascript execution
            Object jsObj = PageObject.getJs().executeScript(
                "return Object.keys(angular.element(document.getElementById('editorWrapper')).scope().model.creationConstraints).length.toString();");
            String editorContent = "";
            if (jsObj != null) {
                editorContent = (String) jsObj;
            }

            TEST_RESULT = editorContent.equals(expectedNumber.toString());
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);
            
        }
        
        public void testNumberInspectorConstraintCards(Integer expectedNumber) {
            
            SectionBindingForm form = SectionBindingForm.navigateTo();
            SectionInspector.testOpenInspector();

            TEST_RESULT = Objects.equals(expectedNumber, form.getInspectorConstraintCardSize());
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);
            
        }
        
    }
    
}
