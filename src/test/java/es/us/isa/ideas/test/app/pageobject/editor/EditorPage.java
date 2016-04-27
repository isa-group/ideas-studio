package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.pageobject.login.RegisterPage;
import es.us.isa.ideas.test.app.utils.FileType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
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

    @FindBy(css = "#wsmWorkspaces > div.col-xs-12.cards > div > div > div.card__meta > div > p.card__meta-content-title")
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
    @FindBy(css = "#appGenericModal div.modal-footer > a.btn.btn-primary.continue")
    WebElement modalContinueButton;

    @FindBy(css = "#appGenericModal div.modal-footer > a.btn.dismiss")
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

    // FILES
    @FindBy(linkText = "Create Text file")
    WebElement projCreateTxtFileAnchor;

    @FindBy(linkText = "Create Angular file")
    WebElement projCreateAngFileAnchor;

    @FindBy(linkText = "Create Angular Ctl file")
    WebElement projCreateAngCtlFileAnchor;

    @FindBy(linkText = "Create JSON file")
    WebElement projCreateJsonFileAnchor;

    @FindBy(linkText = "Create YAML file")
    WebElement projCreateYamlFileAnchor;

    @FindBy(linkText = "Create iAgreeTemplate file")
    WebElement projCreateIAgreeTemplateFileAnchor;

    @FindBy(linkText = "Create iAgreeOffer file")
    WebElement projCreateIAgreeOfferFileAnchor;

    @FindBy(linkText = "Create iAgreeAgreement file")
    WebElement projCreateIAgreeAgreementFileAnchor;

    @FindBy(linkText = "Upload File")
    WebElement projUploadFileAnchor;

    @FindBy(linkText = "Create Directory")
    WebElement projCreateDirectoryAnchor;

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

    // EDITOR TAB
    @FindBy(css = "#editorTabs")
    WebElement editorTabContainer;

    @FindBy(css = "#editorTabs > li.active")
    WebElement tabActivatedElement;

    static final Logger LOG = Logger.getLogger(EditorPage.class.getName());

    public static EditorPage navigateTo() {
        PageObject.navigateTo("/app/editor");
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
        clickOnClickableElement(wsPublishDemoButton);
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        clickOnClickableElement(wsAddButton);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        clickOnNotClickableElement(modalContinueButton);

        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnModalCloseButton() {
        clickOnClickableElement(modalCloseButton);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnMenuTogglerButton() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        clickOnNotClickableElement(wsMenuTogglerButton);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public EditorPage clickOnCreateFile(FileType fileType) {

        EditorPage page = PageFactory.initElements(getWebDriver(), EditorPage.class);

        // Create a new switch case for future file types.
        switch (fileType) {
            case ANGULAR:
                page = clickOnCreateAngFileAnchor();
                break;

            case ANGULAR_CONTROLLER:
                page = clickOnCreateAngCtlFileAnchor();
                break;

            case PLAINTEXT:
                page = clickOnCreateTxtFileAnchor();
                break;

            case JSON:
                page = clickOnCreateJsonFileAnchor();
                break;

            case YAML:
                page = clickOnCreateYamlFileAnchor();
                break;

            case IAGREE_TEMPLATE:
                page = clickOnCreateIAgreeTemplateFileAnchor();
                break;

            case IAGREE_OFFER:
                page = clickOnCreateIAgreeOfferFileAnchor();
                break;

            case IAGREE_AGREEMENT:
                page = clickOnCreateIAgreeAgreementFileAnchor();
                break;
        }

        return page;
    }

    public EditorPage clickOnCreateTxtFileAnchor() {
        clickOnClickableElement(projCreateTxtFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateAngFileAnchor() {
        clickOnClickableElement(projCreateAngFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateAngCtlFileAnchor() {
        clickOnClickableElement(projCreateAngCtlFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateJsonFileAnchor() {
        clickOnClickableElement(projCreateJsonFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateYamlFileAnchor() {
        clickOnClickableElement(projCreateYamlFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateIAgreeTemplateFileAnchor() {
        clickOnClickableElement(projCreateIAgreeTemplateFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateIAgreeOfferFileAnchor() {
        clickOnClickableElement(projCreateIAgreeOfferFileAnchor);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage clickOnCreateIAgreeAgreementFileAnchor() {
        clickOnClickableElement(projCreateIAgreeAgreementFileAnchor);
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
        modalWSNameField.clear();
        modalWSNameField.sendKeys(wsName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeWorkspaceDescription(CharSequence... wsDescription) {
        modalWSDescriptionField.clear();
        modalWSDescriptionField.sendKeys(wsDescription);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeWorkspaceTags(CharSequence... wsTags) {
        modalWSTagsField.clear();
        modalWSTagsField.sendKeys(wsTags);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeProjectName(CharSequence... projName) {
        modalProjOrDirNameField.clear();
        modalProjOrDirNameField.sendKeys(projName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeDirectoryName(CharSequence... dirName) {
        modalProjOrDirNameField.clear();
        modalProjOrDirNameField.sendKeys(dirName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeFileName(CharSequence... fileName) {
        modalFileNameField.clear();
        modalFileNameField.sendKeys(fileName);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage typeContextMenuEditField(CharSequence... name) {
        contextMenuEditField.clear();
        contextMenuEditField.sendKeys(name);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public EditorPage executeCommand(CharSequence... cmds) {
        console.clear();
        console.sendKeys(cmds);
        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    // others
    public String getProjCurrentWSText() {
        return projCurrentWSText.getText();
    }

    public String getWorkspaceDemoCardTitle() {
        return wsDemoCardTitle.getText();
    }

    public String getWorkspacePublicDemoCardTitle() {
        return wsPublicDemoCardTitle.getText();
    }

    public String getWorkspaceCardTitle() {
        return wsCardTitle.getText();
    }

    public EditorPage consoleEchoCommand(String msg) {

        EditorPage result = null;
        WebElement lastElement = PageObject.getWebDriver().findElement(By.id("appFooter"));

        if (lastElement != null) {
            getJs().executeScript("CommandApi.echo('" + msg + "');");
            result = PageFactory.initElements(getWebDriver(), EditorPage.class);
        }

        try {
            Thread.sleep(1000); // animation
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;

    }

    public EditorPage expandAllDynatreeNodes() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        EditorPage result = null;
        WebElement lastElement = PageObject.getWebDriver().findElement(By.id("appFooter"));

        if (lastElement != null) {
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
                Thread.sleep(1000); // animation
            } catch (InterruptedException ex) {
                Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            result = PageFactory.initElements(getWebDriver(), EditorPage.class);
        }

        return result;

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

        EditorPage result = null;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (PageObject.getWebDriver().findElement(By.id("editor")) != null) {
            getJs().executeScript(""
                + "var ret = false;"
                + "if (document.editor) {"
                + "  document.editor.session.setValue('" + content.replaceAll("\n", "\\\\n").replaceAll("\"", "\\\"") + "');"
                + "  ret = true;"
                + "}"
                + "return ret;");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            result = PageFactory.initElements(getWebDriver(), EditorPage.class);
        }

        return result;

    }

    /**
     * This method searches the file locator text and compares if it is equals
     * to the current activated file tab.
     *
     * @param element
     * @return
     */
    public Boolean isFileTabActivatedByLocator(WebElement element) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        String fileName = element.getText();

        return tabActivatedElement.getText().equals(fileName);

    }

    public void closeFileByName(String fileName) {
        
        WebElement element = PageObject.getWebDriver().findElement(By.cssSelector("#editorTabsContainer > ul > li"));
        
        if (element != null) {
            // Closes all files with the fileName and not activated
            PageObject.getJs().executeScript(
                "jQuery('#editorTabsContainer > ul > li').each(function() {"
                + "if(jQuery(this).text().indexOf('" + fileName + "') !== -1) "
                + "jQuery(this).not('.active').find('span').click()})"
            );
        }
        
    }

}
