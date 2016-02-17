package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.editor.DirectorySection;
import es.us.isa.ideas.test.app.pageobject.editor.FileSection;
import es.us.isa.ideas.test.app.pageobject.editor.ProjectSection;
import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceSection;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCaseDynatree extends TestCase {

    static String wsName = "wsTest";
    static String wsDesc = "descTest";
    static String wsTags = "tag1";
    
    static String projName = "Project";
    
    static String dir1 = "dir1";
    static String dir2 = "dir2";
    
    static String originFileName = "file1";
    static String targetFileName = "file1_mod";
    static String originFile2LName = "file2";
    static String targetFile2LName = "file2_mod";
    static String fileNameExtension = ".txt";

    @Test
    public void step01_login() {
        LoginPage.testLogin("DemoMaster", "DemoMaster");
    }

    /**
     * Creates a workspace.
     */
    @Test
    public void step02_createWorkspace() {
        WorkspaceSection.testCreateWorkspace(wsName, wsDesc, wsTags);
    }

    @Test
    public void step03_createProject() {
        ProjectSection.testCreateProject(projName);
    }

    /**
     * Creates a new file.
     */
    @Test
    public void step04_createFile() {
        By parentLocator = By.linkText(projName);
        String fileName = originFileName;
        FileSection.testCreateFile(fileName, parentLocator);
    }

    /**
     * Creates a directory inside the project.
     */
    @Test
    public void step05_createDirectory() {
        By parentLocator = By.linkText(projName);
        DirectorySection.testCreateDirectory(dir1, parentLocator);
    }

    /**
     * Creates a new directory inside another directory.
     */
    @Test
    public void step06_createDirectory2L() {
        By parentLocator = By.linkText(dir1);
        DirectorySection.testCreateDirectory(dir2, parentLocator);
    }

    /**
     * Creates a file inside a second level directory.
     */
    @Test
    public void step07_createFile2L() {
        By parentLocator = By.linkText(dir2);
        String fileName = originFile2LName;
        FileSection.testCreateFile(fileName, parentLocator);
    }

    @Test
    public void step08_renameFile() {
        FileSection.testRenameFile(originFileName + fileNameExtension,
            targetFileName + fileNameExtension);
    }

    @Test
    public void step09_editFile2L() {
        By fileLocator = By.linkText(originFile2LName + fileNameExtension);
        FileSection.testEditFile(fileLocator, "Hello world!");
    }

    @Test
    public void step10_renameFile2L() {
        FileSection.testRenameFile(originFile2LName + fileNameExtension,
            targetFile2LName + fileNameExtension);
    }

    @Test
    public void step11_deleteWorkspace() {
        WorkspaceSection.testDeleteWorkspace(wsName);
    }
}
