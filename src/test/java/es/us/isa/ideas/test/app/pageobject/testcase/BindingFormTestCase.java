package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.SectionBindingForm;
import es.us.isa.ideas.test.app.pageobject.editor.SectionEditorFormatTab;
import es.us.isa.ideas.test.app.pageobject.editor.SectionFile;
import es.us.isa.ideas.test.app.pageobject.editor.SectionInspector;
import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceManagerPage;
import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import es.us.isa.ideas.test.app.utils.FileType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain.
 * 
 * This test case needs HTML, JSON, YAML, IAGREE-TEMPLATE and ANGULAR 
 * modules deployed.
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
    static String jsonFileName = "portal-config";
    static FileType jsonFileType = FileType.JSON;
    static String jsonFileContent = "{\"id\": []}";
    
    // IAgree example params
    static String templateFileName = "multiPlan";
    static FileType templateFileType = FileType.IAGREE_TEMPLATE;
    static String templateFileContent = "" +
        "Template multiPlan version 1.0\\n" +
        "	Provider \"Papamoscas SL\" as Responder;\\n" +
        "	Metrics:\\n" +
        "		price: integer[0..500];\\n" +
        "		int: integer[0..10000];\\n" +
        "		ms: integer[0..10000];\\n" +
        "		type: enum{pro, medium, basic, free};\\n" +
        "		period: enum{d, mo, ye};\\n" +
        "\\n" +
        "AgreementTerms\\n" +
        "	Service BirdAPI availableAt \"http://papamoscas.showcase.governify.io/api/v5/birds\"\\n" +
        "		GlobalDescription\\n" +
        "			MaxResponseTime: ms;\\n" +
        "			MaxRequests: int;\\n" +
        "			BillingPeriod: period = \"mo\";\\n" +
        "			PlanPrice: price;\\n" +
        "			PlanType: type;\\n" +
        "\\n" +
        "	MonitorableProperties\\n" +
        "		global:\\n" +
        "			AVGResponseTime: int;\\n" +
        "			Requests: int;\\n" +
        "\\n" +
        "	GuaranteeTerms\\n" +
        "		RequestTerm: Consumer guarantees Requests <= MaxRequests;\\n" +
        "\\n" +
        "		ResponseTimeTerm: Provider guarantees AVGResponseTime <= MaxResponseTime;\\n" +
        "\\n" +
        "CreationConstraints\\n" +
        "	C3: (MaxRequests == 1000 AND MaxResponseTime == 100 AND PlanPrice == 10);\\n" +
        "		onlyIf(PlanType == \"pro\");\\n" +
        "\\n" +
        "\\n" +
        "EndTemplate";
    
    static String templateFormContent = "" +
        "<div ng-repeat=\"key in model.creationConstraints\" class=\"ag-template\">\\n" +
        "	<ul>\\n" +
        "	    <li>\\n" +
        "	        <select ng-model=\"key.qc.condition.properties.exp2.properties.value\">\\n" +
        "	            <option ng-repeat=\"planType in model.agreementTerms.service.cps.PlanType.metric.domain.values\">\"{{ planType }}\"</option>\\n" +
        "	        </select>\\n" +
        "	    </li>\\n" +
        "		<li>\\n" +
        "			<h4>\\n" +
        "				<input ng-bind=\"key.slo.expression.properties.exp.properties.exp2.properties.exp2.properties.value\"\\n" +
        "				       ng-model=\"key.slo.expression.properties.exp.properties.exp2.properties.exp2.properties.value\" /> € / mo</h4>\\n" +
        "			</h4>\\n" +
        "		</li>\\n" +
        "		<li>\\n" +
        "			<input ng-model=\"key.slo.expression.properties.exp.properties.exp1.properties.exp1.properties.exp2.properties.value\" /> requests</em> allowed</li>\\n" +
        "		</li>\\n" +
        "		<li>\\n" +
        "			<input ng-model=\"key.slo.expression.properties.exp.properties.exp1.properties.exp2.properties.exp2.properties.value\" /> ms</em> guaranteed response time </li>\\n" +
        "		</li>\\n" +
        "		\\n" +
        "		<li>\\n" +
        "		    <a ng-click=\"removeModel(key.id)\">&times;</a>\\n" +
        "		</li>\\n" +
        "	</ul>\\n" +
        "</div>";
    
    // Keep this one line, when the content is parsed to a json object, newlines will generated.
    static String templateModelFileContent = "{\"creationConstraints\":{\"C4\":{\"slo\":{\"expression\":{\"_type\":\"ParenthesisExpression\",\"properties\":{\"exp\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxRequests\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"1000\"}},\"operator\":\"EQ\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxResponseTime\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"200\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanPrice\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"100\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}}}}},\"qc\":{\"condition\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanType\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"\\\"pro\\\"\"}},\"operator\":\"EQ\"}}},\"id\":\"C4\"},\"C3\":{\"slo\":{\"expression\":{\"_type\":\"ParenthesisExpression\",\"properties\":{\"exp\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxRequests\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"500\"}},\"operator\":\"EQ\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxResponseTime\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"500\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanPrice\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"50\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}}}}},\"qc\":{\"condition\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanType\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"\\\"medium\\\"\"}},\"operator\":\"EQ\"}}},\"id\":\"C3\"},\"C1\":{\"slo\":{\"expression\":{\"_type\":\"ParenthesisExpression\",\"properties\":{\"exp\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxRequests\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"5\"}},\"operator\":\"EQ\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxResponseTime\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"2000\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanPrice\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"0\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}}}}},\"qc\":{\"condition\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanType\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"\\\"free\\\"\"}},\"operator\":\"EQ\"}}},\"id\":\"C1\"},\"C2\":{\"slo\":{\"expression\":{\"_type\":\"ParenthesisExpression\",\"properties\":{\"exp\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"LogicalExpression\",\"properties\":{\"exp1\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxRequests\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"10\"}},\"operator\":\"EQ\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxResponseTime\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"1000\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}},\"exp2\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanPrice\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"10\"}},\"operator\":\"EQ\"}},\"operator\":\"AND\"}}}}},\"qc\":{\"condition\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"PlanType\"}},\"exp2\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"\\\"basic\\\"\"}},\"operator\":\"EQ\"}}},\"id\":\"C2\"}},\"id\":\"multiPlan\",\"version\":1.0,\"context\":{\"responder\":{\"id\":\"\\\"Papamoscas SL\\\"\",\"role\":\"Provider\",\"roleType\":\"Responder\"},\"metrics\":{\"price\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":500},\"id\":\"price\"},\"int\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":10000},\"id\":\"int\"},\"ms\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":10000},\"id\":\"ms\"},\"boolean\":{\"type\":\"Boolean\",\"domain\":{\"values\":[true,false]},\"id\":\"boolean\"},\"period\":{\"type\":\"enum\",\"domain\":{\"values\":[\"d\",\"mo\",\"ye\"]},\"id\":\"period\"},\"type\":{\"type\":\"enum\",\"domain\":{\"values\":[\"pro\",\"medium\",\"basic\",\"free\"]},\"id\":\"type\"}}},\"agreementTerms\":{\"service\":{\"serviceName\":\"BirdAPI\",\"endpointReference\":\"http://papamoscas.showcase.governify.io/api/v5/birds\",\"features\":{},\"cps\":{\"MaxResponseTime\":{\"metric\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":10000},\"id\":\"ms\"},\"scope\":\"Global\",\"id\":\"MaxResponseTime\"},\"MaxRequests\":{\"metric\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":10000},\"id\":\"int\"},\"scope\":\"Global\",\"id\":\"MaxRequests\"},\"BillingPeriod\":{\"metric\":{\"type\":\"enum\",\"domain\":{\"values\":[\"d\",\"mo\",\"ye\"]},\"id\":\"period\"},\"expr\":{\"_type\":\"Atomic\",\"properties\":{\"value\":\"\\\"mo\\\"\"}},\"scope\":\"Global\",\"id\":\"BillingPeriod\"},\"PlanPrice\":{\"metric\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":500},\"id\":\"price\"},\"scope\":\"Global\",\"id\":\"PlanPrice\"},\"PlanType\":{\"metric\":{\"type\":\"enum\",\"domain\":{\"values\":[\"pro\",\"medium\",\"basic\",\"free\"]},\"id\":\"type\"},\"scope\":\"Global\",\"id\":\"PlanType\"}}},\"mps\":{\"Requests\":{\"metric\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":10000},\"id\":\"int\"},\"scope\":\"Global\",\"id\":\"Requests\"},\"AVGResponseTime\":{\"metric\":{\"type\":\"integer\",\"domain\":{\"min\":0,\"max\":10000},\"id\":\"int\"},\"scope\":\"Global\",\"id\":\"AVGResponseTime\"}},\"gts\":{\"RequestTerm\":{\"role\":\"Consumer\",\"slo\":{\"expression\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"Requests\"}},\"exp2\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxRequests\"}},\"operator\":\"LTE\"}}},\"compensations\":[],\"id\":\"RequestTerm\"},\"ResponseTimeTerm\":{\"role\":\"Provider\",\"slo\":{\"expression\":{\"_type\":\"RelationalExpression\",\"properties\":{\"exp1\":{\"_type\":\"Var\",\"properties\":{\"id\":\"AVGResponseTime\"}},\"exp2\":{\"_type\":\"Var\",\"properties\":{\"id\":\"MaxResponseTime\"}},\"operator\":\"LTE\"}}},\"compensations\":[],\"id\":\"ResponseTimeTerm\"}}},\"docType\":\"TEMPLATE\"}";

    @BeforeClass
    public static void before() {
        PageObject.logout();
    }
    
    /**
     * Creates initial workspace, project and json file.
     */
    @Test
    public void step01_createJsonEnvironment() {
        
        // Login
        LoginPage.testLogin(user, pass);
        
        // Workspace, project and file creation
        WorkspaceManagerPage.testCreateWorkspace(wsName, wsDesc, wsTags);
        SectionFile.testCreateProject(projName);
        By parentLocator = By.linkText(projName);
        SectionFile.testCreateFile(jsonFileName, jsonFileType, parentLocator);
        By fileLocator = By.linkText(jsonFileName + jsonFileType.toString());
        SectionFile.testEditFile(fileLocator, jsonFileContent);
        
    }
    
    /**
     * Create by pressing inspector creator button.
     */
    @Test
    public void step02_testBuildExampleForm() {
        SectionInspector.testOpenInspector();
        By fileLocator = By.linkText(jsonFileName + jsonFileType.toString());
        SectionFile.testOpenFile(fileLocator);
        SectionInspector.testBuildExampleFormFromFilename(jsonFileName);
    }
    
    /**
     * Open JSON file and check if formatView.
     */
    @Test
    public void step03_testFormatViewForm() {
        By fileLocator = By.linkText(jsonFileName + jsonFileType.toString());
        SectionFile.testOpenFile(fileLocator);
        SectionEditorFormatTab.testIsEditorFormatActivated("FORM");
    }
    
    /**
     * Open inspector while formatView is activated.
     * It should show formatView content in the inspector.
     */
    @Test
    public void step04_openInspectorWithSampleForm() {
        SectionInspector.testOpenInspector();
        String sampleContent = "This is a sample content for a FORM file.";
        SectionInspector.testInspectorFormTabContentContains(sampleContent);
        SectionEditorFormatTab.testIsEditorFormatActivated("JSON");
    }
    
    @Test
    public void step05_createIAgreeEnvironment() {
        By parentLocator = By.linkText(projName);
        SectionFile.testCreateFile(templateFileName, templateFileType, parentLocator);
        By fileLocator = By.linkText(templateFileName + templateFileType.toString());
        SectionFile.testEditFile(fileLocator, templateFileContent);
    }
    
    /**
     * Create a form file from scratch using IDEAS add menu.
     * This test will be ignored until it is possible to create the same file
     * with different extension. #133
     */
    @Ignore
    public void step06_testBuildFormFromScratch() {
        By parentLocator = By.linkText(projName);
        SectionFile.testCreateFile(templateFileName, FileType.ANGULAR, parentLocator);
        By fileLocator = By.linkText(templateFileName + FileType.ANGULAR.toString());
        SectionFile.testEditFile(fileLocator, templateFormContent);
    }
    
    @Test
    public void step06_testBuildExampleForm() {
        
        // Create form file from inspector
        SectionInspector.testOpenInspector();
        SectionInspector.testBuildExampleFormFromFilename(templateFileName);
        
        By fileLocator = By.linkText(templateFileName + FileType.ANGULAR.toString());
        
        // Edit file content
        SectionFile.testEditFile(fileLocator, templateFormContent);
        
        // Format view
        fileLocator = By.linkText(templateFileName + templateFileType.toString());
        SectionFile.testOpenFile(fileLocator);
        SectionEditorFormatTab.testIsEditorFormatActivated("FORM");
        
    }
    
    /**
     * Check if you press addSlaButton on formatView, it creates a new creation
     * constraint. 
     */
    @Test
    public void step07_testFormatViewAddSlaButton() {
        SectionBindingForm.testFormatViewAddSlaButton();
        SectionBindingForm.testNumberOfCreationConstraints(2);
        SectionEditorFormatTab.testActivateFormatTab("JSON");
    }
    
    @Test
    public void step08_testUpdateModelFromIAgreeFormat() {
        SectionEditorFormatTab.testActivateFormatTab("IAGREE");
        SectionInspector.testOpenInspector();
        
        String iAgreeContentFile;
        iAgreeContentFile = "" +
            "Template multiPlan version 1.0\\n" +
            "	Provider \"Papamoscas SL\" as Responder;\\n" +
            "	Metrics:\\n" +
            "		price: integer[0..500];\\n" +
            "		int: integer[0..10000];\\n" +
            "		ms: integer[0..10000];\\n" +
            "		type: enum{pro, medium, basic, free};\\n" +
            "		period: enum{d, mo, ye};\\n" +
            "\\n" +
            "AgreementTerms\\n" +
            "	Service BirdAPI availableAt \"http://papamoscas.showcase.governify.io/api/v5/birds\"\\n" +
            "		GlobalDescription\\n" +
            "			MaxResponseTime: ms;\\n" +
            "			MaxRequests: int;\\n" +
            "			BillingPeriod: period = \"mo\";\\n" +
            "			PlanPrice: price;\\n" +
            "			PlanType: type;\\n" +
            "\\n" +
            "	MonitorableProperties\\n" +
            "		global:\\n" +
            "			AVGResponseTime: int;\\n" +
            "			Requests: int;\\n" +
            "\\n" +
            "	GuaranteeTerms\\n" +
            "		RequestTerm: Consumer guarantees Requests <= MaxRequests;\\n" +
            "\\n" +
            "		ResponseTimeTerm: Provider guarantees AVGResponseTime <= MaxResponseTime;\\n" +
            "\\n" +
            "CreationConstraints\\n" +
            "	C3: (MaxRequests == 1000 AND MaxResponseTime == 100 AND PlanPrice == 10);\\n" +
            "		onlyIf(PlanType == \"pro\");\\n" +
            "\\n" +
            "	C1: (MaxRequests == 1000 AND MaxResponseTime == 100 AND PlanPrice == 10);\\n" +
            "		onlyIf(PlanType == \"pro\");\\n" +
            "	\\n" +
            "	C2: (MaxRequests == 1000 AND MaxResponseTime == 100 AND PlanPrice == 10);\\n" +
            "		onlyIf(PlanType == \"pro\");\\n" +
            "\\n" +
            "\\n" +
            "EndTemplate";
        
        // Edit iAgree file content
        By fileLocator = By.linkText(templateFileName + templateFileType.toString());
        SectionFile.testEditFile(fileLocator, iAgreeContentFile);
        
        try {
            Thread.sleep(2000); // checking file content
        } catch (InterruptedException ex) {
            Logger.getLogger(BindingFormTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SectionBindingForm.testNumberInspectorConstraintCards(3);
    }

    @Test
    public void step09_deleteWorkspace() {
        WorkspaceManagerPage.testDeleteWorkspace(wsName);
    }
}
