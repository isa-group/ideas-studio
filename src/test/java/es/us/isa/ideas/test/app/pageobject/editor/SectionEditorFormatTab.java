/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.pageobject.login.RegisterPage;
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
public class SectionEditorFormatTab extends EditorPage {

    @FindBy(css = "#editorFormats > li.formatTab.active")
    WebElement formatTabActive;

    @FindBy(id = "editorFormats")
    WebElement editorFormatsContainer;

    // Page functionalities
    public static SectionEditorFormatTab navigateTo() {
        EditorPage.navigateTo();
        return PageFactory.initElements(getWebDriver(), SectionEditorFormatTab.class);
    }

    // Click
    public SectionEditorFormatTab clickOnFormatTabActivated() {
        clickOnNotClickableElement(formatTabActive);
        return PageFactory.initElements(getWebDriver(), SectionEditorFormatTab.class);
    }

    // Test facade
    public static void testIsEditorFormatActivated(String formatName) {
        new SectionEditorFormatTabTestCase().testIsEditorFormatActivated(formatName);
    }

    public static void testActivateFormatTab(String formatName) {
        new SectionEditorFormatTabTestCase().testActivateFormatTab(formatName);
    }

    private static class SectionEditorFormatTabTestCase extends TestCase {

        /**
         * Check if the current format is the one given as param.
         *
         * @param formatName
         */
        public void testIsEditorFormatActivated(String formatName) {
            SectionEditorFormatTab formatTab = SectionEditorFormatTab.navigateTo();
            String formatTabActive = formatTab.formatTabActive.getText();

            TEST_RESULT = formatTabActive.equalsIgnoreCase(formatName);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);
        }

        public void testActivateFormatTab(String formatName) {
            SectionEditorFormatTab formatTab = PageFactory.initElements(getWebDriver(), SectionEditorFormatTab.class);
            WebElement formatElement = formatTab.editorFormatsContainer
                .findElement(By.linkText(formatName));

            formatTab.clickOnClickableElement(formatElement);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionEditorFormatTab.class.getName()).log(Level.SEVERE, null, ex);
            }

            testIsEditorFormatActivated(formatName);
        }
    }
}
