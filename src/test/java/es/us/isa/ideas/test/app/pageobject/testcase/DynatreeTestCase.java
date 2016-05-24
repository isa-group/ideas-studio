package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.SectionFile;
import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceManagerPage;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import es.us.isa.ideas.test.app.utils.FileType;
import es.us.isa.ideas.test.app.utils.TestProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain.
 *
 * This test case needs PLAINTEXT module deployed.
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DynatreeTestCase {

    static String wsName = "My workspace";
    static String wsNameFinal = "My-workspace";
    static String wsDesc = "descTest";
    static String wsTags = "tag1";

    static String projName = "Project";

    static String dir1 = "dir1";
    static String dir2 = "dir2";

    static String originFileName = "file1";
    static String targetFileName = "file1_mod";
    static String originFile2LName = "file2";
    static String targetFile2LName = "file2_mod";

    static FileType fileExt = FileType.PLAINTEXT;

    @Test
    public void step01_unableToCreateDirectoryWithoutWorkspace() {
        
        LoginPage.testLogin(TestProperty.getTestDefaultUser(), TestProperty.getTestDefaultUserPass());
        PageObject.getWebDriver().navigate().refresh();
        
        // Tries to create a directory in a non-existent workspace
        SectionFile.testCreateDirectoryWithNoWorkspace();
        
    }

    @Test
    public void step02_createWorkspace() {
        
        // It will normalize workspace name with 'wsNameFinal'
        WorkspaceManagerPage.testCreateWorkspace(wsName, wsDesc, wsTags);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DynatreeTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        WorkspaceManagerPage.testCreateWorkspaceWithError(wsNameFinal, wsDesc, wsTags);
        
    }

    @Test
    public void step03_createProject() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DynatreeTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        SectionFile.testCreateProject(projName);
    }

    // Tries to create an already created project
    @Test
    public void step04_createProject2() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DynatreeTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        SectionFile.testCreateProjectWithError(projName);
    }

    @Test
    public void step05_createFile() {
        By parentLocator = By.linkText(projName);
        String fileName = originFileName;
        SectionFile.testCreateFile(fileName, fileExt, parentLocator);
    }

    @Test
    public void step06_copyPaste() {
        
        String fileName = originFileName + fileExt.toString();
        String target = fileName;
        
        // Tries to paste any file
        SectionFile.testPaste(target, false);
        
        // Tries to copy a file inside a file
        SectionFile.testCopyPaste(fileName, target, false);
    }

    @Test
    public void step07_createDirectory() {
        By parentLocator = By.linkText(projName);
        SectionFile.testCreateDirectory(dir1, parentLocator);
    }

    // Creates a new directory inside another directory
    @Test
    public void step08_createDirectory2L() {
        By parentLocator = By.linkText(dir1);
        SectionFile.testCreateDirectory(dir2, parentLocator);
    }

    // Creates a file inside a second level directory.
    @Test
    public void step09_createFile2L() {
        By parentLocator = By.linkText(dir2);
        String fileName = originFile2LName;
        SectionFile.testCreateFile(fileName, fileExt, parentLocator);
    }

    @Test
    public void step10_renameFile() {
        SectionFile.testRenameFile(originFileName + fileExt.toString(),
            targetFileName + fileExt.toString());
    }

    @Test
    public void step11_editFile2L() {
        By fileLocator = By.linkText(originFile2LName + fileExt.toString());
        SectionFile.testEditFile(fileLocator, "Hello world!");
    }

    @Test
    public void step12_renameFile2L() {
        SectionFile.testRenameFile(originFile2LName + fileExt.toString(),
            targetFile2LName + fileExt.toString());
    }

    @Test
    public void step13_deleteWorkspace() {
        WorkspaceManagerPage.testDeleteWorkspace(wsNameFinal);
    }

    @Test
    public void step14_uploadWorkspaceZip() {
        WorkspaceManagerPage.testCreateWorkspaceZip(wsDesc, wsTags);
        WorkspaceManagerPage.testDeleteWorkspace("TestWorkspace");
        PageObject.logout();
    }
}
