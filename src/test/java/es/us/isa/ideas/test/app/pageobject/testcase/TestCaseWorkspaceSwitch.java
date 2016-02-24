package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.editor.DirectorySection;
import es.us.isa.ideas.test.app.pageobject.editor.FileSection;
import es.us.isa.ideas.test.app.pageobject.editor.ProjectSection;
import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceSection;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import static es.us.isa.ideas.test.app.pageobject.testcase.TestCaseDynatree.originFile2LName;
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
public class TestCaseWorkspaceSwitch extends TestCase {

    static String wsName1 = "wsTest1";
    static String wsName2 = "wsTest2";
    
    static String projName1 = "Project1";
    static String projName2 = "Project2";
    
    static String fileName1 = "file1";
    static String fileName2 = "file2";
    static String fileNameExtension = ".txt";

    @Test
    public void step01_login() {
        LoginPage.testLogin("autotester", "autotester");
    }
    
    @Test
    public void step02_createWorkspace() {
        WorkspaceSection.testCreateWorkspace(wsName1, "", "");
    }

    @Test
    public void step03_createProject() {
        ProjectSection.testCreateProject(projName1);
    }
    
    @Test
    public void step04_createFile() {
        By parentLocator = By.linkText(projName1);
        FileSection.testCreateFile(fileName1, parentLocator);
    }
    
    @Test
    public void step05_createWorkspace2() {
        WorkspaceSection.testCreateWorkspace(wsName2, "", "");
    }

    @Test
    public void step06_createProject2() {
        ProjectSection.testCreateProject(projName2);
    }
    
    @Test
    public void step07_createFile2() {
        By parentLocator = By.linkText(projName2);
        FileSection.testCreateFile(fileName2, parentLocator);
    }
    
    @Test
    public void step08_editFile2() {
        By fileLocator = By.linkText(fileName2 + fileNameExtension);
        FileSection.testEditFile(fileLocator, "Hello world!");
    }

    @Test
    public void step09_openWorkspace1() {
        WorkspaceSection.testOpenWorkspace(wsName1);
    }
    
    @Test
    public void step10_editFile1() {
        By fileLocator = By.linkText(fileName1 + fileNameExtension);
        FileSection.testEditFile(fileLocator, "Hello world!");
    }

    @Test
    public void step11_deleteWorkspaces() {
        WorkspaceSection.testDeleteWorkspace(wsName1);
        WorkspaceSection.testDeleteWorkspace(wsName2);
    }
}
