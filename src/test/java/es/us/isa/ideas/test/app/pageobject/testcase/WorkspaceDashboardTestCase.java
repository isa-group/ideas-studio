package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceManagerPage;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    static String originWSName = "NewWorkspaceCardFelipe";
    static String originWSDesc = "Selemium Test";
    static String originWSTags = "test selenium";

    static String targetWSName = "NewWorkspaceCardmodifiedFlipe";
    static String targetWSDesc = "Selemium Test Description (Edit test)";
    static String targetWSTags = "test selenium";
    
    static String demoWSName = "DemoWorkspaceFelipe";

    @Test
    public void step01_createAndPublishGuestWorkspace() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Login guest
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("guest", "guest");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Create and publish workspace
        WorkspaceManagerPage.testCreateWorkspace(demoWSName, "", "");
        WorkspaceManagerPage.testPublishDemoWorkspace();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void step02_createAutoTesterWorkspace() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Login autotester
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("autotester", "autotester");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testCreateWorkspace(originWSName, originWSDesc, originWSTags);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void step03_allDashboardOperationsWithAutoTesterWorkspace() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testOpenWorkspaceFromDashboard();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testDownloadWorkspaceFromDashboard();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testEditWorkspaceFromDashboard(targetWSName, targetWSDesc);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        WorkspaceManagerPage.testPublishDemoWorkspaceFromDashboard();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testUpdateDemoWorkspaceFromDashboard();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testDepublishDemoWorkspaceFromDashboard(demoWSName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        WorkspaceManagerPage.testDeleteWorkspaceFromDashboard(targetWSName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testClonePublicDemoFromDashboard(demoWSName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testDeleteCloneDemoFromDashboard(targetWSName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void step04_deleteTestWorkspaces() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Deleting guest workspace
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("guest", "guest");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testDeleteCloneDemoFromDashboard(demoWSName);
        
        // Deleting DemoMaster workspace
        WorkspaceManagerPage.logout();
        LoginPage.testLogin("DemoMaster", "DemoMaster");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testDeleteCloneDemoFromDashboard(demoWSName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
