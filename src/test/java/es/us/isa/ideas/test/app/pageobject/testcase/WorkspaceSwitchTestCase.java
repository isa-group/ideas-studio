package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.editor.SectionFile;
import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceManagerPage;
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
public class WorkspaceSwitchTestCase {

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
        WorkspaceManagerPage.testCreateWorkspace(wsName1, "", "");
    }

    @Test
    public void step03_createProject() {
        SectionFile.testCreateProject(projName1);
    }
    
    @Test
    public void step04_createFile() {
        By parentLocator = By.linkText(projName1);
        SectionFile.testCreateFile(fileName1, parentLocator);
    }
    
    @Test
    public void step05_createWorkspace2() {
        WorkspaceManagerPage.testCreateWorkspace(wsName2, "", "");
    }

    @Test
    public void step06_createProject2() {
        SectionFile.testCreateProject(projName2);
    }
    
    @Test
    public void step07_createFile2() {
        By parentLocator = By.linkText(projName2);
        SectionFile.testCreateFile(fileName2, parentLocator);
    }
    
    @Test
    public void step08_editFile2() {
        By fileLocator = By.linkText(fileName2 + fileNameExtension);
        SectionFile.testEditFile(fileLocator, "Hello world!");
    }

    @Test
    public void step09_openWorkspace1() {
        WorkspaceManagerPage.testOpenWorkspace(wsName1);
    }
    
    @Test
    public void step10_editFile1() {
        By fileLocator = By.linkText(fileName1 + fileNameExtension);
        SectionFile.testEditFile(fileLocator, "Hello world!");
    }

    @Test
    public void step11_deleteWorkspaces() {
        WorkspaceManagerPage.testDeleteWorkspace(wsName1);
        WorkspaceManagerPage.testDeleteWorkspace(wsName2);
    }
}
