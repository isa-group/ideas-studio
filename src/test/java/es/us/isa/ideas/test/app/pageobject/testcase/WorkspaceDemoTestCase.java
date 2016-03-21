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
public class WorkspaceDemoTestCase {
    
    static String demoWSName = "DemoWork";

    @Test
    public void step01_createAndPublishGuestWorkspace() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.logout();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        LoginPage.testLogin("guest", "guest");
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testCreateWorkspace(demoWSName, "", "");
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testPublishDemoWorkspace();
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Test
    public void step02_viewDemo() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.logout();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WorkspaceManagerPage.testDemoView(demoWSName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void step03_deleteTestWorkspace() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceDashboardTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        LoginPage.testLogin("guest", "guest");
        
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
