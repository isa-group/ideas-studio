package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceManagerPage;
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
public class WorkspaceDashboardTestCase {

    static String originWSName = "NewWorkspaceCard";
    static String originWSDesc = "Selemium Test";
    static String originWSTags = "test selenium";

    static String targetWSName = "NewWorkspaceCardmodified";
    static String targetWSDesc = "Selemium Test Description (Edit test)";
    static String targetWSTags = "test selenium";
    
    static String demoWSName = "DemoWorkspace";

    @Test
    public void step01_loginGuest() {
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("guest", "guest");
    }

    @Test
    public void step02_createGuestWorkspace() {
        WorkspaceManagerPage.testCreateWorkspace(demoWSName, "", "");
    }

    @Test
    public void step03_publishDemoWorkspace() {
        WorkspaceManagerPage.testPublishDemoWorkspace();
    }

    @Test
    public void step04_loginAutoTester() {
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("autotester", "autotester");
    }
    
    @Test
    public void step05_createAutoTesterWorkspace() {
        WorkspaceManagerPage.testCreateWorkspace(originWSName, originWSDesc, originWSTags);
    }
    
    @Test
    public void step06_openAutoTesterWorkspaceFromDashboard() {
        WorkspaceManagerPage.testOpenWorkspaceFromDashboard();
    }
    
    @Test
    public void step07_downloadWorkspaceFromDashboard() {
        WorkspaceManagerPage.testDownloadWorkspaceFromDashboard();
    }
    
    @Test
    public void step08_editWorkspaceFromDashboard() {
        WorkspaceManagerPage.testEditWorkspaceFromDashboard(targetWSName, targetWSDesc);
    }
    
    @Test
    public void step09_publishDemoWorkspaceFromDashboard() {
        WorkspaceManagerPage.testPublishDemoWorkspaceFromDashboard();
    }
    
    @Test
    public void step10_updateDemoWorkspaceFromDashboard() {
        WorkspaceManagerPage.testUpdateDemoWorkspaceFromDashboard();
    }
    
    @Test
    public void step11_depublishDemoWorkspaceFromDashboard() {
        WorkspaceManagerPage.testDepublishDemoWorkspaceFromDashboard(demoWSName);
    }
    
    @Test
    public void step12_deleteWorkspaceFromDashboard() {
        WorkspaceManagerPage.testDeleteWorkspaceFromDashboard(targetWSName);
    }
    
    @Test
    public void step13_clonePublicDemoFromDashboard() {
        WorkspaceManagerPage.testClonePublicDemoFromDashboard(demoWSName);
    }
    
    @Test
    public void step14_deleteCloneDemoFromDashboard() {
        WorkspaceManagerPage.testDeleteCloneDemoFromDashboard(targetWSName);
    }
    
    @Test
    public void step15_loginGuest() {
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("guest", "guest");
    }
    
    @Test
    public void step16_deleteGuestPublicDemoFromDashboard() {
        WorkspaceManagerPage.testDeleteCloneDemoFromDashboard(demoWSName);
    }
    
    @Test
    public void step17_loginDemoMaster() {
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("DemoMaster", "DemoMaster");
    }
    
    @Test
    public void step18_deleteDemoMasterWorkspace() {
        WorkspaceManagerPage.testDeleteCloneDemoFromDashboard(demoWSName);
    }
}
