package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceSection;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCaseWorkspaceEditor extends TestCase {

    static String originWSName = "New Workspace from Editor";
    static String originWSDesc = "Selemium Editor New Workspace Test";
    static String originWSTags = "test selenium";

    static String targetWSName = "New Workspace from Editor modified";
    static String targetWSDesc = "Selemium Editor New Workspace Test (Edit test)";
    static String targetWSTags = "test selenium";

    @Test
    public void step01_loginAutoTester() {
        LoginPage.testLogin("autotester", "autotester");
    }

    @Test
    public void step02_createWorkspace() {
        WorkspaceSection.testCreateWorkspace(originWSName, originWSDesc, originWSTags);
    }

    @Test
    public void step03_editCurrentWorkspace() {
        WorkspaceSection.testEditWorkspace(targetWSName, targetWSDesc);
    }

    @Test
    public void step04_downloadWorkspace() {
        WorkspaceSection.testDownloadWorkspace();
    }

    @Test
    public void step05_publishDemoWorkspace() {
        WorkspaceSection.testPublishDemoWorkspace();
    }

    @Test
    public void step06_deleteAutoTesterWorkspace() {
        WorkspaceSection.testDeleteWorkspaceButton();
    }

    @Test
    public void step07_loginDemoMaster() {
        WorkspaceSection.logout();
        LoginPage.testLogin("DemoMaster", "DemoMaster");
    }

    @Test
    public void step08_deleteDemoMasterWorkspace() {
        WorkspaceSection.testDeleteWorkspace(targetWSName);
    }
}
