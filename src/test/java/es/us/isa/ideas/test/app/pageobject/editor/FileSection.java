/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import static es.us.isa.ideas.test.app.pageobject.testcase.PageObject.getJs;
import static es.us.isa.ideas.test.app.pageobject.testcase.PageObject.getWebDriver;
import static es.us.isa.ideas.test.app.pageobject.testcase.PageObject.getWebDriverWait;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
public class FileSection extends EditorPage {

    public static void testCreateFile(String fileName, By parentLocator) {
        new FileTestCase().testCreateFile(fileName, parentLocator);
    }

    public static void testRenameFile(String originFileName, String targetFileName) {
        new FileTestCase().testRenameFile(originFileName, targetFileName);
    }

    public static void testEditFile(By fileLocator, String content) {
        new FileTestCase().testEditFile(fileLocator, content);
    }

    /**
     * This class implements all tests available in EditorPage.
     */
    private static class FileTestCase extends TestCase {

        public static boolean isAceEditorEmpty() {

            boolean ret = false;

            Object jsObj = getJs().executeScript(
                "return document.editor ? document.editor.getValue() : null;");
            String editorContent = "";
            if (jsObj != null) {
                editorContent = (String) jsObj;
                ret = editorContent.equals("");
            }

            return ret;

        }

        public void testCreateFile(String fileName, By parentLocator) {

            EditorPage page = FileSection.navigateTo()
                .expandAllDynatreeNodes()
                .clickOnNotClickableLocator(parentLocator)
                .clickOnProjectAddButton()
                .clickOnCreateTxtFileAnchor()
                .typeFileName(fileName)
                .clickOnModalContinueButton();

            By fileLocator = By.linkText(fileName + ".txt");
            ExpectedCondition<WebElement> fileCondition = ExpectedConditions.visibilityOfElementLocated(fileLocator);

            getWebDriverWait().until(fileCondition);
            getWebDriver().navigate().refresh();

            page.expandAllDynatreeNodes();
            getWebDriverWait().until(fileCondition);

            TEST_RESULT = getWebDriver().findElements(fileLocator).size() > 0;

            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + fileName + ".txt\" was successfully created.");
            }

            Assert.assertTrue(TEST_RESULT);

        }

        public void testRenameFile(String originFileName, String targetFileName) {

            EditorPage page = FileSection.navigateTo()
                .expandAllDynatreeNodes()
                .activateDynatreeContextMenuByNodeTitle(originFileName)
                .clickOnContextMenuEditAnchor()
                .typeContextMenuEditField(targetFileName, Keys.RETURN);

            By fileLocator = By.linkText(targetFileName);
            ExpectedCondition<WebElement> fileCondition = ExpectedConditions.visibilityOfElementLocated(fileLocator);

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileSection.class.getName()).log(Level.SEVERE, null, ex);
            }

//            getWebDriverWait().until(fileCondition);
            getWebDriver().navigate().refresh();

            page.expandAllDynatreeNodes();
            getWebDriverWait().until(fileCondition);

            TEST_RESULT = getWebDriver().findElements(fileLocator).size() > 0;

            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + originFileName + "\" was successfully renamed to \"" + targetFileName + "\".");
            }

            Assert.assertTrue(TEST_RESULT);

        }

        public void testEditFile(By fileLocator, String content) {

            EditorPage page = EditorPage.navigateTo()
                .expandAllDynatreeNodes();

            ExpectedCondition<WebElement> fileCondition = ExpectedConditions.visibilityOfElementLocated(fileLocator);

            // Edit file content
            WebElement fileElement = getWebDriverWait().until(fileCondition);
            page.clickOnNotClickableElement(fileElement)
                .aceEditorContent(content);

            getWebDriver().navigate().refresh();

            // Re-open file
            page.expandAllDynatreeNodes();
            fileElement = getWebDriverWait().until(fileCondition);
            page.clickOnClickableElement(fileElement);

            // Check if content has been successfully saved.
            TEST_RESULT = !isAceEditorEmpty();

            if (TEST_RESULT) {
                page.consoleEchoCommand("File was successfully edited.");
            }

            Assert.assertTrue(TEST_RESULT);

        }
    }

}
