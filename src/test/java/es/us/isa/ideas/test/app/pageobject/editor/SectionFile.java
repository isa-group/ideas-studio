/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.utils.FileType;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getJs;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriverWait;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class SectionFile extends EditorPage {
    
    @FindBy(css = "#projectsTree > ul > li:nth-child(1) > span > a")
    WebElement projectElement;
    
    // File tests
    
    public static void testOpenFile(By fileLocator) {
        new FileTestCase().testOpenFile(fileLocator);
    }
    
    public static void testCreateFile(String fileName, FileType fileType, By parentLocator) {
        new FileTestCase().testCreateFile(fileName, fileType, parentLocator);
    }

    public static void testRenameFile(String originFileName, String targetFileName) {
        new FileTestCase().testRenameFile(originFileName, targetFileName);
    }

    public static void testEditFile(By fileLocator, String content) {
        new FileTestCase().testEditFile(fileLocator, content);
    }
    
    // Directory tests
    
    public static void testCreateDirectory(String dirName, By parentLocator) {
        new DirectoryTestCase().testCreateDirectory(dirName, parentLocator);
    }
    
    // Project tests
    
    public static void testCreateProject(String projName) {
        new ProjectTestCase().testCreateProject(projName);
    }

    // Others
    
    public WebElement getProjectElement() {
        return PageFactory.initElements(getWebDriver(), SectionFile.class).projectElement;
    }

    /**
     * This class implements all tests related to files management.
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

        /**
         * Checks if the click on fileLocator param activates a tab named like it.
         * @param fileLocator 
         */
        public void testOpenFile(By fileLocator) {

            EditorPage page = EditorPage.navigateTo().expandAllDynatreeNodes();
            ExpectedCondition<WebElement> fileCondition = ExpectedConditions.visibilityOfElementLocated(fileLocator);

            // Open file
            WebElement fileElement = getWebDriverWait().until(fileCondition);
            page.clickOnNotClickableElement(fileElement);

            TEST_RESULT = page.isFileTabActivatedByLocator(fileLocator);

            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + fileElement.getText() + "\" was successfully opened.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        /**
         * Check if a new file persists after a page refresh.
         * @param fileName
         * @param fileType
         * @param parentLocator 
         */
        public void testCreateFile(String fileName, FileType fileType, By parentLocator) {

            EditorPage page = SectionFile.navigateTo()
                .expandAllDynatreeNodes()
                .clickOnNotClickableLocator(parentLocator)
                .clickOnProjectAddButton()
                .clickOnCreateFile(fileType)
                .typeFileName(fileName)
                .clickOnModalContinueButton();

            By fileLocator = By.linkText(fileName + fileType.toString());
            ExpectedCondition<WebElement> fileCondition = ExpectedConditions.visibilityOfElementLocated(fileLocator);

            getWebDriverWait().until(fileCondition);
            getWebDriver().navigate().refresh();

            page.expandAllDynatreeNodes();
            getWebDriverWait().until(fileCondition);

            TEST_RESULT = getWebDriver().findElements(fileLocator).size() > 0;

            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + fileName + fileType.toString() + "\" was successfully created.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        public void testRenameFile(String originFileName, String targetFileName) {

            EditorPage page = SectionFile.navigateTo()
                .expandAllDynatreeNodes()
                .activateDynatreeContextMenuByNodeTitle(originFileName)
                .clickOnContextMenuEditAnchor()
                .typeContextMenuEditField(targetFileName, Keys.RETURN);

            By fileLocator = By.linkText(targetFileName);
            ExpectedCondition<WebElement> fileCondition = ExpectedConditions.visibilityOfElementLocated(fileLocator);

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }

//            getWebDriverWait().until(fileCondition);
            getWebDriver().navigate().refresh();

            page.expandAllDynatreeNodes();
            getWebDriverWait().until(fileCondition);

            TEST_RESULT = getWebDriver().findElements(fileLocator).size() > 0;

            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + originFileName + "\" was successfully renamed to \"" + targetFileName + "\".");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
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
            page.clickOnNotClickableElement(fileElement);

            // Check if content has been successfully saved.
            TEST_RESULT = !isAceEditorEmpty();

            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + fileElement.getText() + "\" was successfully edited.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }
    }

    /**
     * This class implements all tests related to directories management.
     */
    private static class DirectoryTestCase extends TestCase {

        public void testCreateDirectory(String dirName, By parentLocator) {

            EditorPage page = SectionFile.navigateTo()
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

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }
    }
    
    /**
     * This class implements all tests related to projects management.
     */
    private static class ProjectTestCase extends TestCase {

        public void testCreateProject(String projName) {

            EditorPage page = EditorPage.navigateTo()
                .clickOnProjectAddButton()
                .clickOnCreateProjectAnchor()
                .typeProjectName(projName)
                .clickOnModalContinueButton();

            getWebDriver().navigate().refresh();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            WebElement element = getWebDriverWait().until(ExpectedConditions.visibilityOf(new SectionFile().getProjectElement()));

            TEST_RESULT = element.getText().equals(projName);

            if (TEST_RESULT) {
                page.consoleEchoCommand("Project \"" + projName + "\" was successfully created.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }
    }
    
}
