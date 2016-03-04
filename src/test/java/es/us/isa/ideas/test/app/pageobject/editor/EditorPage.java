package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.utils.TestProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class EditorPage extends PageObject<EditorPage> {

    // WORKSPACES
    @FindBy(id = "menuToggler")
    WebElement wsMenuTogglerButton;
    
    @FindBy(id = "workspacesNavContainer")
    WebElement wsMenuListContainer;

    @FindBy(css = "#appLeftMenuContentHeader > div")
    WebElement wsAddButton;

    @FindBy(id = "edit-ws")
    WebElement wsEditButton;

    @FindBy(id = "download-ws")
    WebElement wsDownloadButton;

    @FindBy(id = "delete-ws")
    WebElement wsDeleteButton;

    @FindBy(id = "demo-ws")
    WebElement wsPublishDemoButton;

    @FindBy(css = ".demoworkspace .card__meta-content-title")
    WebElement wsDemoCardTitle;

    @FindBy(css = ".publicdemo  .card__meta-content-title")
    WebElement wsPublicDemoCardTitle;

    @FindBy(css = ".workspace .card__meta-content-title")
    WebElement wsCardTitle;

    @FindBy(css = "#wsForm > button")
    WebElement wsFormSaveButton;

    // WORKSPACE DASHBOARD
    @FindBy(id = "viewDemo")
    WebElement wsViewDemoButton;

    @FindBy(id = "openWS")
    WebElement wsDashboardOpenCardButton;

    @FindBy(id = "editWS")
    WebElement wsDashboardEditCardButton;

    @FindBy(id = "downloadWS")
    WebElement wsDashboardDownloadCardButton;

    @FindBy(id = "publishWS")
    WebElement wsDashboardPublishCardButton;

    @FindBy(id = "updateDemo")
    WebElement wsDashboardUpdateDemoCardButton;

    @FindBy(id = "cloneDemo")
    WebElement wsDashboardClonePublicDemoCardButton;

    @FindBy(id = "deleteDemo")
    WebElement wsDashboardDeleteDemoCardButton;

    @FindBy(id = "deleteWS")
    WebElement wsDashboardDeleteCardButton;

    // MODAL
    @FindBy(css = "#appGenericModal > div > div > div.modal-footer > a.btn.btn-primary.continue")
    WebElement modalContinueButton;

    @FindBy(css = "#appGenericModal > div > div > div.modal-footer > a.btn.dismiss")
    WebElement modalCloseButton;

    @FindBy(css = "#modalCreationField > input")
    WebElement modalWSNameField;

    @FindBy(css = "#descriptionInput > textarea")
    WebElement modalWSDescriptionField;

    @FindBy(css = "#tagsInput > textarea")
    WebElement modalWSTagsField;

    @FindBy(css = "#appGenericModal > div > div > div.modal-body > div:nth-child(6) > div.checkbox > label > input")
    WebElement modalWSInitZipCheckbox;

    @FindBy(css = "#modalCreationField > input")
    WebElement modalProjOrDirNameField;

    @FindBy(id = "filename")
    WebElement modalFileNameField;

    // PROJECTS
    @FindBy(id = "editorSidePanelHeaderWorkspaceInfo")
    WebElement projCurrentWSText;

    @FindBy(css = "#editorSidePanelHeaderAddProject > div > div")
    WebElement projAddButton;

    @FindBy(linkText = "Create Project")
    WebElement projCreateProjectAnchor;

    @FindBy(linkText = "Create Text file")
    WebElement projCreateTxtFileAnchor;

    @FindBy(linkText = "Create Directory")
    WebElement projCreateDirectoryAnchor;

    @FindBy(linkText = "Upload File")
    WebElement projUploadFileAnchor;

    // CONTEXT MENU (DYNATREE)
    @FindBy(id = "myMenu")
    WebElement contextMenuWrapper;

    @FindBy(linkText = "Edit")
    WebElement contextMenuEditAnchor;

    @FindBy(css = "input#editNode")
    WebElement contextMenuEditField;

    // CONSOLE
    @FindBy(css = "#gcli-root input.gcli-in-input")
    WebElement console;

    static final Logger LOG = Logger.getLogger(EditorPage.class.getName());
    static final String URL = TestProperty.getBaseUrl() + "/app/editor";

    public static EditorPage navigateTo() {
        getWebDriver().get(URL);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    // click - workspace
    public EditorPage clickOnWorkspaceEditButton() {
        clickOnNotClickableElement(wsEditButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDownloadButton() {
        clickOnNotClickableElement(wsDownloadButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDeleteButton() {
        clickOnNotClickableElement(wsDeleteButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspacePublishDemoButton() {
        clickOnNotClickableElement(wsPublishDemoButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceFormSaveButton() {
        clickOnClickableElement(wsFormSaveButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceViewDemoButton() {
        clickOnNotClickableElement(wsViewDemoButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceAddButton() {
        clickOnClickableElement(wsAddButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    // click - workspace dashboard
    public EditorPage clickOnWorkspaceDashboardOpenCardButton() {
        clickOnNotClickableElement(wsDashboardOpenCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDashboardEditCardButton() {
        clickOnNotClickableElement(wsDashboardEditCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDashboardDownloadCardButton() {
        clickOnNotClickableElement(wsDashboardDownloadCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDashboardPublishCardButton() {
        clickOnNotClickableElement(wsDashboardPublishCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDashboardUpdateDemoCardButton() {
        clickOnNotClickableElement(wsDashboardUpdateDemoCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDashboardClonePublicDemoCardButton() {
        clickOnNotClickableElement(wsDashboardClonePublicDemoCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDashboardDeleteDemoCardButton() {
        clickOnNotClickableElement(wsDashboardDeleteDemoCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnWorkspaceDashboardDeleteCardButton() {
        clickOnNotClickableElement(wsDashboardDeleteCardButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    // click - modal
    public EditorPage clickOnModalContinueButton() {
        clickOnClickableElement(modalContinueButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnModalCloseButton() {
        clickOnClickableElement(modalCloseButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnMenuTogglerButton() {
        clickOnNotClickableElement(wsMenuTogglerButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnProjectAddButton() {
        clickOnNotClickableElement(projAddButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateProjectAnchor() {
        clickOnClickableElement(projCreateProjectAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateTxtFileAnchor() {
        clickOnClickableElement(projCreateTxtFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateDirectoryAnchor() {
        clickOnClickableElement(projCreateDirectoryAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnUploadFileAnchor() {
        clickOnClickableElement(projUploadFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnContextMenuEditAnchor() {
        clickOnClickableElement(contextMenuEditAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    // sendKeys - workspace
    public EditorPage typeWorkspaceName(CharSequence... wsName) {
        sendKeysWithWait(modalWSNameField, wsName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeWorkspaceDescription(CharSequence... wsDescription) {
        modalWSDescriptionField.sendKeys(wsDescription);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeWorkspaceTags(CharSequence... wsTags) {
        modalWSTagsField.sendKeys(wsTags);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeProjectName(CharSequence... projName) {
        sendKeysWithWait(modalProjOrDirNameField, projName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeDirectoryName(CharSequence... dirName) {
        sendKeysWithWait(modalProjOrDirNameField, dirName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeFileName(CharSequence... fileName) {
        sendKeysWithWait(modalFileNameField, fileName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeContextMenuEditField(CharSequence... name) {
        sendKeysWithWait(contextMenuEditField, name);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage executeCommand(CharSequence... cmds) {
        sendKeysWithWait(console, cmds);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    // others
    public String getProjCurrentWSText() {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(projCurrentWSText));
        return projCurrentWSText.getText();
    }

    public String getWorkspaceDemoCardTitle() {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(wsDemoCardTitle));
        return wsDemoCardTitle.getText();
    }

    public String getWorkspacePublicDemoCardTitle() {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(wsPublicDemoCardTitle));
        return wsPublicDemoCardTitle.getText();
    }

    public String getWorkspaceCardTitle() {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(wsCardTitle));
        return wsCardTitle.getText();
    }

    public EditorPage consoleEchoCommand(String msg) {
        getJs().executeScript("CommandApi.echo('" + msg + "');");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return PageFactory.initElements(getWebDriver(), EditorPage.class);

    }

    public EditorPage expandAllDynatreeNodes() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        getJs().executeScript(""
            + "var ret=false;"
            + "try {"
            + "  jQuery('#projectsTree').dynatree('getRoot').visit(function(node){"
            + "    node.expand(true);"
            + "  });"
            + "  ret = true;"
            + "} catch (err) {"
            + "  ret=false;"
            + "}"
            + "return ret;");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return PageFactory.initElements(getWebDriver(), EditorPage.class);

    }

    public EditorPage activateDynatreeContextMenuByNodeTitle(String nodeTitle) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        getJs().executeScript(""
            + "var ret = false;"
            + "jQuery('span a.dynatree-title').filter(function(){"
            + "  if (jQuery(this).text() == '" + nodeTitle + "') {"
            + "    jQuery(this).trigger({type:'mousedown',button:2}).trigger({type:'mouseup',button:2});"
            + "    ret = true;"
            + "  }"
            + "});"
            + "return ret;");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return PageFactory.initElements(getWebDriver(), EditorPage.class);

    }

    public EditorPage aceEditorContent(String content) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        getJs().executeScript(""
            + "var ret = false;"
            + "if (document.editor) {"
            + "  document.editor.session.setValue('" + content + "');"
            + "  ret = true;"
            + "}"
            + "return ret;");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return PageFactory.initElements(getWebDriver(), EditorPage.class);

    }

}
