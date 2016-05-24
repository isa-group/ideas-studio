/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.utils.FileType;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getJs;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.pageobject.login.RegisterPage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
    public static void testOpenFile(WebElement element) {
        new FileTestCase().testOpenFile(element);
    }

    public static void testCloseFile(WebElement element) {
        new FileTestCase().testCloseFile(element);
    }

    public static void testCreateFile(String fileName, FileType fileType, By parentLocator) {
        new FileTestCase().testCreateFile(fileName, fileType, parentLocator);
    }

    public static void testRenameFile(String originFileName, String targetFileName) {
        new FileTestCase().testRenameFile(originFileName, targetFileName);
    }

    public static void testCopyPaste(String originFileName, String targetFileName, Boolean cpExpect) {
        new FileTestCase().testCopyPaste(originFileName, targetFileName, cpExpect);
    }

    public static void testPaste(String targetFileName, Boolean cpExpect) {
        new FileTestCase().testPaste(targetFileName, cpExpect);
    }

    public static void testEditFile(By fileLocator, String content) {
        new FileTestCase().testEditFile(fileLocator, content);
    }

    // Directory tests
    public static void testCreateDirectory(String dirName, By parentLocator) {
        new DirectoryTestCase().testCreateDirectory(dirName, parentLocator);
    }
    
    public static void testCreateDirectoryWithNoWorkspace() {
        new DirectoryTestCase().testCreateDirectoryWithNoWorkspace();
    }

    // Project tests
    public static void testCreateProject(String projName) {
        new ProjectTestCase().testCreateProject(projName);
    }
    
    public static void testCreateProjectWithError(String projName) {
        new ProjectTestCase().testCreateProjectWithError(projName);
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
         * Checks if the click on fileLocator param activates a tab named like
         * it.
         *
         * @param element
         */
        public void testOpenFile(WebElement element) {

            EditorPage page = EditorPage.navigateTo()
                .expandAllDynatreeNodes()
                .clickOnNotClickableElement(element);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = page.isFileTabActivatedByLocator(element);
            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + element.getText() + "\" was successfully opened.");
            }
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        public void testCloseFile(WebElement element) {

            String fileName = element.getText();
            EditorPage page = EditorPage.navigateTo();

            TEST_RESULT = false;
            if (page.tabActivatedElement.getText().equals(fileName)) {
                page.closeFileByName(fileName);

                try {
                    Thread.sleep(2000); // tab animation
                } catch (InterruptedException ex) {
                    Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
                }

                TEST_RESULT = page.isFileTabActivatedByLocator(element);
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        /**
         * Check if a new file persists after a page refresh.
         *
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

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            getWebDriver().navigate().refresh();
            page.expandAllDynatreeNodes();

            By fileLocator = By.linkText(fileName + fileType.toString());
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

            getWebDriver().navigate().refresh();
            page.expandAllDynatreeNodes();

            By fileLocator = By.linkText(targetFileName);
            TEST_RESULT = getWebDriver().findElements(fileLocator).size() > 0;

            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + originFileName + "\" was successfully renamed to \"" + targetFileName + "\".");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        /**
         * Copy a file into a target
         * @param fileName
         * @param target 
         * @param cpExpect 
         */
        public void testCopyPaste(String fileName, String target, Boolean cpExpect) {

            EditorPage page = SectionFile.navigateTo()
                .expandAllDynatreeNodes()
                .activateDynatreeContextMenuByNodeTitle(fileName)
                .clickOnContextMenuCopyAnchor();
            
            By fileLocator = By.linkText(fileName);
            Integer prevNumberElements = PageObject.getWebDriver().findElements(fileLocator).size();
            
            try {
                Thread.sleep(500); // animation
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            page.activateDynatreeContextMenuByNodeTitle(target)
                .clickOnContextMenuPasteAnchor();
            
            try {
                Thread.sleep(500); // animation
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            fileLocator = By.linkText(fileName);
            if (cpExpect) {
                TEST_RESULT = getWebDriver().findElements(fileLocator).size() == prevNumberElements + 1;
            } else {
                TEST_RESULT = getWebDriver().findElements(fileLocator).size() == prevNumberElements;
            }
            
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }
        
        public void testPaste(String target, Boolean successExpected) {

            EditorPage page = SectionFile.navigateTo()
                .activateDynatreeContextMenuByNodeTitle(target)
                .clickOnContextMenuPasteAnchor();
            
            try {
                Thread.sleep(500); // animation
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            TEST_RESULT = false;
            if (successExpected) {
                // TODO
            } else {
                TEST_RESULT = EditorPage.isConsoleLastMessageError("Clipboard is empty");
            }
            
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        public void testEditFile(By fileLocator, String content) {

            EditorPage page = EditorPage.navigateTo()
                .expandAllDynatreeNodes()
                .clickOnNotClickableElement(getWebDriver().findElement(fileLocator));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.aceEditorContent(content);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            getWebDriver().navigate().refresh();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Re-open file
            EditorPage.navigateTo()
                .expandAllDynatreeNodes()
                .clickOnNotClickableElement(getWebDriver().findElement(fileLocator));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SectionFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Check if content has been successfully saved.
            TEST_RESULT = !isAceEditorEmpty();
            if (TEST_RESULT) {
                page.consoleEchoCommand("File \"" + getWebDriver().findElement(fileLocator).getText() + "\" was successfully edited.");
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

            getWebDriver().navigate().refresh();
            page.expandAllDynatreeNodes();

            By fileLocator = By.linkText(dirName);
            TEST_RESULT = getWebDriver().findElements(fileLocator).size() > 0;
            if (TEST_RESULT) {
                page.consoleEchoCommand("Directory \"" + dirName + "\" was successfully created.");
            }
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        // #55 Fix
        public void testCreateDirectoryWithNoWorkspace() {
            
            EditorPage page = SectionFile.navigateTo();
            WebElement lastElement = PageObject.getWebDriver().findElement(By.id("appFooter"));
            
            page.expandAllDynatreeNodes()
                .clickOnProjectAddButton()
                .clickOnCreateDirectoryAnchor();

            TEST_RESULT = false;
            try {
                TEST_RESULT = !page.modalBackground.isDisplayed();
            } catch (NoSuchElementException ex) {
                TEST_RESULT = true;
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
                Logger.getLogger(WorkspaceManagerPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            WebElement element = new SectionFile().getProjectElement();
            TEST_RESULT = false;
            if (element != null) {
                TEST_RESULT = element.getText().equals(projName);
                if (TEST_RESULT) {
                    page.consoleEchoCommand("Project \"" + projName + "\" was successfully created.");
                }
            }
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }
        
        public void testCreateProjectWithError(String projName) {

            EditorPage page = EditorPage.navigateTo();
            WebElement lastElement = PageObject.getWebDriver().findElement(By.id("appFooter"));

            if (page != null && lastElement != null) {
                page = EditorPage.navigateTo()
                    .clickOnProjectAddButton()
                    .clickOnCreateProjectAnchor()
                    .typeProjectName(projName)
                    .clickOnModalContinueButton();

                TEST_RESULT = page.modalErrorContent.getText().contains("Error creating new project");
                Assert.assertTrue(TEST_RESULT);

                page.clickOnModalErrorContinueButton();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
                }

                TEST_RESULT = false;
                try {
                    TEST_RESULT = !page.modalBackground.isDisplayed();
                } catch (NoSuchElementException ex) {
                    TEST_RESULT = true;
                }
                
                getWebDriver().navigate().refresh();
                
                Assert.assertTrue(TEST_RESULT);
                LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);

            }
        }
    }

}
