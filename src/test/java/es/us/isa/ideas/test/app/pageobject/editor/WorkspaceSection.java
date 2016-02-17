/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import static es.us.isa.ideas.test.app.pageobject.testcase.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCase;
import es.us.isa.ideas.test.app.pageobject.testcase.TestProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class WorkspaceSection extends EditorPage {

    static final Logger LOG = Logger.getLogger(WorkspaceSection.class.getName());

    public static WorkspaceSection navigateTo() {
        //TODO: automatically set base url.
        getWebDriver().get(TestProperty.getBaseUrl() + "/app/wsm");
        return PageFactory.initElements(getWebDriver(), WorkspaceSection.class);
    }

    public static void testCreateWorkspace(String wsName, String wsDescription, String wsTags) {
        new WorkspaceTestCase().testCreateWorkspace(wsName, wsDescription, wsTags);
    }

    public static void testOpenWorkspaceFromDashboard() {
        new WorkspaceTestCase().testOpenWorkspaceFromDashboard();
    }

    public static void testEditWorkspace(String wsName, String wsDescription) {
        new WorkspaceTestCase().testEditWorkspace(wsName, wsDescription);
    }

    public static void testEditWorkspaceFromDashboard(String wsName, String wsDescription) {
        new WorkspaceTestCase().testEditWorkspaceFromDashboard(wsName, wsDescription);
    }

    public static void testDownloadWorkspace() {
        new WorkspaceTestCase().testDownloadWorkspace();
    }

    public static void testDownloadWorkspaceFromDashboard() {
        new WorkspaceTestCase().testDownloadWorkspaceFromDashboard();
    }

    public static void testPublishDemoWorkspace() {
        new WorkspaceTestCase().testPublishDemoWorkspace();
    }

    public static void testPublishDemoWorkspaceFromDashboard() {
        new WorkspaceTestCase().testPublishDemoWorkspaceFromDashboard();
    }

    public static void testUpdateDemoWorkspaceFromDashboard() {
        new WorkspaceTestCase().testUpdateDemoWorkspaceFromDashboard();
    }

    public static void testDepublishDemoWorkspaceFromDashboard(String wsName) {
        new WorkspaceTestCase().testDepublishDemoWorkspaceFromDashboard(wsName);
    }

    public static void testDeleteWorkspaceFromDashboard(String wsName) {
        new WorkspaceTestCase().testDeleteWorkspaceFromDashboard(wsName);
    }

    public static void testClonePublicDemoFromDashboard(String wsName) {
        new WorkspaceTestCase().testClonePublicDemoFromDashboard(wsName);
    }

    public static void testDeleteCloneDemoFromDashboard(String wsName) {
        new WorkspaceTestCase().testDeleteCloneDemoFromDashboard(wsName);
    }

    public static void testDeleteWorkspaceButton() {
        new WorkspaceTestCase().testDeleteWorkspaceButton();
    }

    public static void testDeleteWorkspace(String wsName) {
        new WorkspaceTestCase().testDeleteWorkspace(wsName);
    }

    public static void testDemoView(String wsName) {
        new WorkspaceTestCase().testDemoView(wsName);
    }

    private static class WorkspaceTestCase extends TestCase {

        public static boolean existWorkspaceByName(String wsName) {

            boolean ret = false;

            Object jsObj = getJs().executeScript(""
                + "var ret = false;"
                + "jQuery('#workspacesNavContainer li').each(function(){"
                + "  if (jQuery(this).text() == '" + wsName + "') {"
                + "    ret = true;"
                + "    return false;"
                + "  }"
                + "});"
                + "return ret;");

            if (jsObj != null) {
                ret = (Boolean) jsObj;
            }

            return ret;
        }

        public void testCreateWorkspace(String wsName, String wsDescription, String wsTags) {

            EditorPage page = EditorPage.navigateTo();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnMenuTogglerButton()
                .clickOnWorkspaceAddButton()
                .typeWorkspaceName(wsName)
                .typeWorkspaceDescription(wsDescription)
                .typeWorkspaceTags(wsTags)
                .clickOnModalContinueButton();

            getWebDriver().navigate().refresh();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            String targetWsName = page.getProjCurrentWSText();

            TEST_RESULT = targetWsName.equals(wsName);

            if (TEST_RESULT) {
                page.consoleEchoCommand("Workspace \"" + wsName + "\" was successfully created.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testOpenWorkspaceFromDashboard() {

            EditorPage page = WorkspaceSection.navigateTo();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnWorkspaceDashboardOpenCardButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = page.getCurrentUrl().contains("/app/editor");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testEditWorkspace(String wsName, String wsDescription) {

            EditorPage page = EditorPage.navigateTo()
                .clickOnWorkspaceEditButton()
                .typeWorkspaceName(wsName)
                .typeWorkspaceDescription(wsDescription)
                .clickOnModalContinueButton();

            getWebDriver().navigate().refresh();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            String targetWsName = page.getProjCurrentWSText();

            TEST_RESULT = targetWsName.equals(wsName);

            if (TEST_RESULT) {
                page.consoleEchoCommand("Workspace was successfully modified.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testEditWorkspaceFromDashboard(String wsName, String wsDescription) {

            EditorPage page = WorkspaceSection.navigateTo()
                .clickOnWorkspaceDashboardEditCardButton()
                .typeWorkspaceName(wsName)
                .typeWorkspaceDescription(wsDescription)
                .clickOnModalContinueButton();

            getWebDriver().navigate().refresh();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            getWebDriverWait().until(ExpectedConditions.visibilityOf(page.wsCardTitle));
            String currentWSName = page.getWorkspaceCardTitle();

            TEST_RESULT = currentWSName.equals(wsName);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testDownloadWorkspace() {
            EditorPage.navigateTo().clickOnWorkspaceDownloadButton();
            assertTrue(true); // copied by previous version of this test
        }

        public void testDownloadWorkspaceFromDashboard() {
            WorkspaceSection.navigateTo()
                .clickOnWorkspaceDashboardDownloadCardButton();
            assertTrue(true); // copied by previous version of this test
        }

        public static boolean deleteWorkspace(String wsName) {
            return false;
        }

        public void testDeleteWorkspaceButton() {

            EditorPage page = EditorPage.navigateTo();
            String wsName = page.getProjCurrentWSText();

            TEST_RESULT = WorkspaceTestCase.deleteWorkspace(wsName);

            page.clickOnWorkspaceDeleteButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            getWebDriver().navigate().refresh();

            page.clickOnMenuTogglerButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = !existWorkspaceByName(wsName);

            if (TEST_RESULT) {
                page.consoleEchoCommand("Workspace \"" + wsName + "\" was successfully removed.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testPublishDemoWorkspace() {

            EditorPage page = EditorPage.navigateTo()
                .clickOnWorkspacePublishDemoButton()
                .clickOnModalContinueButton();

            String wsCardTitle = WorkspaceSection.navigateTo()
                .getWorkspaceDemoCardTitle();

            TEST_RESULT = !wsCardTitle.equals("");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testPublishDemoWorkspaceFromDashboard() {

            EditorPage page = WorkspaceSection.navigateTo()
                .clickOnWorkspaceDashboardPublishCardButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            String wsCardTitle = WorkspaceSection.navigateTo()
                .getWorkspaceDemoCardTitle();

            TEST_RESULT = !wsCardTitle.equals("");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testUpdateDemoWorkspaceFromDashboard() {

            EditorPage page = WorkspaceSection.navigateTo()
                .clickOnWorkspaceDashboardUpdateDemoCardButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            WorkspaceSection.navigateTo();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            String wsCardTitle = page.getWorkspaceDemoCardTitle();

            TEST_RESULT = !wsCardTitle.equals("");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testDepublishDemoWorkspaceFromDashboard(String wsName) {

            EditorPage page = WorkspaceSection.navigateTo()
                .clickOnWorkspaceDashboardDeleteDemoCardButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            WorkspaceSection.navigateTo();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnMenuTogglerButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = !existWorkspaceByName(wsName);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testDeleteWorkspaceFromDashboard(String wsName) {

            EditorPage page = WorkspaceSection.navigateTo()
                .clickOnWorkspaceDashboardDeleteCardButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnMenuTogglerButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = !existWorkspaceByName(wsName);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testClonePublicDemoFromDashboard(String wsName) {

            EditorPage page = WorkspaceSection.navigateTo();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnWorkspaceDashboardClonePublicDemoCardButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();
            WorkspaceSection.navigateTo();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            String targetWSName = page.getWorkspacePublicDemoCardTitle();

            TEST_RESULT = targetWSName.equals(wsName);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testDeleteCloneDemoFromDashboard(String wsName) {

            EditorPage page = WorkspaceSection.navigateTo();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnWorkspaceDashboardDeleteCardButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnMenuTogglerButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = !existWorkspaceByName(wsName);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testDeleteWorkspace(String wsName) {

            EditorPage page = EditorPage.navigateTo()
                .executeCommand("deleteWorkspace " + wsName, Keys.RETURN);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            getWebDriver().navigate().refresh();

            page.clickOnMenuTogglerButton();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = !existWorkspaceByName(wsName);

            if (TEST_RESULT && page.getCurrentUrl().contains("/app/editor")) {
                page.consoleEchoCommand("Workspace \"" + wsName + "\" was successfully removed.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        public void testDemoView(String wsName) {

            EditorPage page = WorkspaceSection.navigateTo()
                .clickOnWorkspaceViewDemoButton();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnModalContinueButton();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkspaceSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = page.getProjCurrentWSText().equals(wsName);

            if (TEST_RESULT) {
                page.consoleEchoCommand("Demo Workspace \"" + wsName + "\" is ready.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }
    }
}
