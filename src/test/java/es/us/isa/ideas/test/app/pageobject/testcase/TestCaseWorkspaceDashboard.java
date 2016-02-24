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
public class TestCaseWorkspaceDashboard extends TestCase {

    static String originWSName = "NewWorkspaceCard";
    static String originWSDesc = "Selemium Test";
    static String originWSTags = "test selenium";

    static String targetWSName = "NewWorkspaceCardmodified";
    static String targetWSDesc = "Selemium Test Description (Edit test)";
    static String targetWSTags = "test selenium";
    
    static String demoWSName = "DemoWorkspace";

    @Test
    public void step01_loginGuest() {
        WorkspaceSection.logout();
        LoginPage.testLogin("guest", "guest");
    }

    @Test
    public void step02_createGuestWorkspace() {
        WorkspaceSection.testCreateWorkspace(demoWSName, "", "");
    }

    @Test
    public void step03_publishDemoWorkspace() {
        WorkspaceSection.testPublishDemoWorkspace();
    }

    @Test
    public void step04_loginAutoTester() {
        WorkspaceSection.logout();
        LoginPage.testLogin("autotester", "autotester");
    }
    
    @Test
    public void step05_createAutoTesterWorkspace() {
        WorkspaceSection.testCreateWorkspace(originWSName, originWSDesc, originWSTags);
    }
    
    @Test
    public void step06_openAutoTesterWorkspaceFromDashboard() {
        WorkspaceSection.testOpenWorkspaceFromDashboard();
    }
    
    @Test
    public void step07_downloadWorkspaceFromDashboard() {
        WorkspaceSection.testDownloadWorkspaceFromDashboard();
    }
    
    @Test
    public void step08_editWorkspaceFromDashboard() {
        WorkspaceSection.testEditWorkspaceFromDashboard(targetWSName, targetWSDesc);
    }
    
    @Test
    public void step09_publishDemoWorkspaceFromDashboard() {
        WorkspaceSection.testPublishDemoWorkspaceFromDashboard();
    }
    
    @Test
    public void step10_updateDemoWorkspaceFromDashboard() {
        WorkspaceSection.testUpdateDemoWorkspaceFromDashboard();
    }
    
    @Test
    public void step11_depublishDemoWorkspaceFromDashboard() {
        WorkspaceSection.testDepublishDemoWorkspaceFromDashboard(demoWSName);
    }
    
    @Test
    public void step12_deleteWorkspaceFromDashboard() {
        WorkspaceSection.testDeleteWorkspaceFromDashboard(targetWSName);
    }
    
    @Test
    public void step13_clonePublicDemoFromDashboard() {
        WorkspaceSection.testClonePublicDemoFromDashboard(demoWSName);
    }
    
    @Test
    public void step14_deleteCloneDemoFromDashboard() {
        WorkspaceSection.testDeleteCloneDemoFromDashboard(targetWSName);
    }
    
    @Test
    public void step15_loginGuest() {
        WorkspaceSection.logout();
        LoginPage.testLogin("guest", "guest");
    }
    
    @Test
    public void step16_deleteGuestPublicDemoFromDashboard() {
        WorkspaceSection.testDeleteCloneDemoFromDashboard(demoWSName);
    }
    
    @Test
    public void step17_loginDemoMaster() {
        WorkspaceSection.logout();
        LoginPage.testLogin("DemoMaster", "DemoMaster");
    }
    
    @Test
    public void step18_deleteDemoMasterWorkspace() {
        WorkspaceSection.testDeleteCloneDemoFromDashboard(demoWSName);
    }
}
