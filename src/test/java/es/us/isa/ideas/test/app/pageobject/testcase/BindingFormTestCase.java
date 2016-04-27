package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.SectionBindingForm;
import es.us.isa.ideas.test.app.pageobject.editor.SectionEditorFormatTab;
import es.us.isa.ideas.test.app.pageobject.editor.SectionFile;
import es.us.isa.ideas.test.app.pageobject.editor.SectionInspector;
import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceManagerPage;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import es.us.isa.ideas.test.app.utils.FileType;
import es.us.isa.ideas.test.app.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain.
 *
 * This test case needs HTML, JSON, YAML, IAGREE-TEMPLATE and ANGULAR modules
 * deployed.
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BindingFormTestCase {

    // Login params
    static String user = "autotester";
    static String pass = "autotester";

    // Workspace params
    static String wsName = "BindingTest";
    static String wsDesc = "This is binding workspace test";
    static String wsTags = "binding";

    static String projName = "MyProject";

    // JSON example params
    static String jsonFileName = "myJson";
    static FileType jsonFileType = FileType.JSON;
    static String jsonFileEmpty = "{\"id\": []}";

    // IAgree example params
    static String fileName = "multiPlan";

    static String atFile = Util.loadFile("src/test/resources/repository/multiPlan.at");
    static String angFile = Util.loadFile("src/test/resources/repository/multiPlan.ang");
    static String ctlFile = Util.loadFile("src/test/resources/repository/multiPlan.ctl");
    static String jsonAtFormat = Util.loadFile("src/test/resources/repository/multiPlan-json_format.json");

    @BeforeClass
    public static void before() {
        PageObject.logout();
    }

    // Creates initial workspace, project and json file
    @Test
    public void step01_createJsonFile() {

        // Login
        LoginPage.testLogin(user, pass);

        // Workspace, project and file creation
        WorkspaceManagerPage.testCreateWorkspace(wsName, wsDesc, wsTags);
        SectionFile.testCreateProject(projName);
        By parentLocator = By.linkText(projName);
        SectionFile.testCreateFile(jsonFileName, jsonFileType, parentLocator);
        By fileLocator = By.linkText(jsonFileName + jsonFileType.toString());
        SectionFile.testEditFile(fileLocator, jsonFileEmpty);

    }

    // Create by clicking on inspector creator button
    @Test
    public void step02_createJsonFormFile() {
        SectionInspector.testOpenInspector();
        By fileLocator = By.linkText(jsonFileName + jsonFileType.toString());
        SectionFile.testOpenFile(PageObject.getWebDriver().findElement(fileLocator));
        SectionInspector.testBuildExampleFormFromFilename(jsonFileName);
    }

    // Open JSON file and check if formatView
    @Test
    public void step03_testJsonFormatView() {
        By fileLocator = By.linkText(jsonFileName + jsonFileType.toString());
        SectionFile.testOpenFile(PageObject.getWebDriver().findElement(fileLocator));
        SectionEditorFormatTab.testIsEditorFormatActivated("FORM");
    }

    /**
     * Open inspector while formatView is activated. It should show formatView
     * content in the inspector.
     */
    @Test
    public void step04_openInspectorWithSampleForm() {
        String sampleContent = "This is a sample content for a FORM file.";
        SectionInspector.testOpenInspector();
        SectionInspector.testInspectorFormTabContentContains(sampleContent);
        SectionEditorFormatTab.testIsEditorFormatActivated("JSON");
    }

    @Test
    public void step05_createFiles() {
        By parentLocator = By.linkText(projName);

        // Create iAgree template file
        SectionFile.testCreateFile(fileName, FileType.IAGREE_TEMPLATE, parentLocator);
        By fileLocator = By.linkText(fileName + FileType.IAGREE_TEMPLATE.toString());
        SectionFile.testEditFile(fileLocator, atFile);

        // Create angular file
        SectionFile.testCreateFile(fileName, FileType.ANGULAR, parentLocator);
        fileLocator = By.linkText(fileName + FileType.ANGULAR.toString());
        SectionFile.testEditFile(fileLocator, angFile);

        // Create angular controller file
        SectionFile.testCreateFile(fileName, FileType.ANGULAR_CONTROLLER, parentLocator);
        fileLocator = By.linkText(fileName + FileType.ANGULAR_CONTROLLER.toString());
        SectionFile.testEditFile(fileLocator, ctlFile);
    }

    @Test
    public void step08_testViewFormFormat() {
        By fileLocator = By.linkText(fileName + FileType.IAGREE_TEMPLATE.toString());
        SectionFile.testOpenFile(PageObject.getWebDriver().findElement(fileLocator));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BindingFormTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }

        SectionEditorFormatTab.testIsEditorFormatActivated("FORM");
    }

    // Check if user loses FORM tab
    @Test
    public void step09_testFormViewWontDisappear() {

        // Open any other file
        By fileLocator = By.linkText(fileName + FileType.ANGULAR_CONTROLLER.toString());
        SectionFile.testOpenFile(PageObject.getWebDriver().findElement(fileLocator));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BindingFormTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }

        fileLocator = By.linkText(fileName + FileType.IAGREE_TEMPLATE.toString());
        WebElement element = PageObject.getWebDriver().findElement(fileLocator);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BindingFormTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        SectionFile.testOpenFile(element);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BindingFormTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        SectionFile.testCloseFile(element);

        SectionEditorFormatTab.testIsEditorFormatActivated("FORM");

    }

    // Check if you press addSlaButton on formatView, it creates a new creation constraint. 
    @Test
    public void step10_testAddCreationConstraintFromFormatView() {
        SectionBindingForm.testAddCreationConstraintFromFormatView();
        SectionBindingForm.testNumberOfCreationConstraints(5);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BindingFormTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }

        SectionEditorFormatTab.testActivateFormatTab("JSON");

    }

    @Test
    public void step11_testUpdateModelFromIAgreeFormat() {
        SectionInspector.testOpenInspector();
        SectionEditorFormatTab.testActivateFormatTab("IAGREE");

        String iAgreeContentFile;
        iAgreeContentFile = ""
            + "Template multiPlan version 1.0\\n"
            + "	Provider \"Papamoscas SL\" as Responder;\\n"
            + "	Metrics:\\n"
            + "		price: integer[0..500];\\n"
            + "		int: integer[0..10000];\\n"
            + "		ms: integer[0..10000];\\n"
            + "		type: enum{pro, medium, basic, free};\\n"
            + "		period: enum{d, mo, ye};\\n"
            + "\\n"
            + "AgreementTerms\\n"
            + "	Service BirdAPI availableAt \"http://papamoscas.showcase.governify.io/api/v5/birds\"\\n"
            + "		GlobalDescription\\n"
            + "			MaxResponseTime: ms;\\n"
            + "			MaxRequests: int;\\n"
            + "			BillingPeriod: period = \"mo\";\\n"
            + "			PlanPrice: price;\\n"
            + "			PlanType: type;\\n"
            + "\\n"
            + "	MonitorableProperties\\n"
            + "		global:\\n"
            + "			AVGResponseTime: int;\\n"
            + "			Requests: int;\\n"
            + "\\n"
            + "	GuaranteeTerms\\n"
            + "		RequestTerm: Consumer guarantees Requests <= MaxRequests;\\n"
            + "\\n"
            + "		ResponseTimeTerm: Provider guarantees AVGResponseTime <= MaxResponseTime;\\n"
            + "\\n"
            + "CreationConstraints\\n"
            + "	C3: (MaxRequests == 1000 AND MaxResponseTime == 100 AND PlanPrice == 100);\\n"
            + "		onlyIf(PlanType == \"pro\");\\n"
            + "\\n"
            + "	C1: (MaxRequests == 5 AND MaxResponseTime == 1000 AND PlanPrice == 10);\\n"
            + "		onlyIf(PlanType == \"free\");\\n"
            + "	\\n"
            + "	C2: (MaxRequests == 100 AND MaxResponseTime == 250 AND PlanPrice == 50);\\n"
            + "		onlyIf(PlanType == \"medium\");\\n"
            + "\\n"
            + "\\n"
            + "EndTemplate";

        // Edit iAgree template file content
        By fileLocator = By.linkText(fileName + FileType.IAGREE_TEMPLATE.toString());
        SectionFile.testEditFile(fileLocator, iAgreeContentFile);

        try {
            Thread.sleep(2000); // checking file content
        } catch (InterruptedException ex) {
            Logger.getLogger(BindingFormTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }

        SectionBindingForm.testNumberInspectorConstraintCards(3);
    }

    @Test
    public void step12_deleteWorkspace() {
        WorkspaceManagerPage.testDeleteWorkspace(wsName);
        PageObject.logout();
    }
}
