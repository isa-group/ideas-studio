/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import static es.us.isa.ideas.test.app.pageobject.testcase.PageObject.getWebDriver;
import static es.us.isa.ideas.test.app.pageobject.testcase.PageObject.getWebDriverWait;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCase;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class DirectorySection extends EditorPage {

    public static void testCreateDirectory(String dirName, By parentLocator) {
        new DirectoryTestCase(dirName, parentLocator)
            .testCreateDirectory();
    }

    /**
     * This class implements all tests available in EditorPage.
     */
    private static class DirectoryTestCase extends TestCase {

        private final String dirName;
        private final By parentLocator;

        public DirectoryTestCase(String dirName, By parentLocator) {
            this.dirName = dirName;
            this.parentLocator = parentLocator;
        }

        public void testCreateDirectory() {

            EditorPage page = DirectorySection.navigateTo()
                .expandAllDynatreeNodes()
                .clickOnNotClickableLocator(parentLocator)
                .expandAllDynatreeNodes()
                .clickOnProjectAddButton()
                .clickOnCreateDirectoryAnchor()
                .typeDirectoryName(dirName)
                .clickOnModalContinueButton()
                .expandAllDynatreeNodes();

            By fileLocator = By.linkText(dirName);
            ExpectedCondition<WebElement> dirCondition = ExpectedConditions.visibilityOfElementLocated(fileLocator);

            getWebDriverWait().until(dirCondition);
            getWebDriver().navigate().refresh();

            page.expandAllDynatreeNodes();
            getWebDriverWait().until(dirCondition);

            TEST_RESULT = getWebDriver().findElements(fileLocator).size() > 0;

            if (TEST_RESULT) {
                page.consoleEchoCommand("Directory \"" + dirName + "\" was successfully created.");
            }

            Assert.assertTrue(TEST_RESULT);

        }
    }
}
