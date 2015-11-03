package es.us.isa.ideas.test.app;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import es.us.isa.ideas.test.utils.ExpectedActions;
import es.us.isa.ideas.test.utils.IdeasAppActions;
import es.us.isa.ideas.test.utils.TestCase;

/**
 * Basic tests of IDEAS functionalities. This test case was thought to apply in
 * .at files (iAgree Template Language). So, before running this test make sure
 * that you have .at files working right.
 * 
 * @author feserafim
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCaseDefault {

	private static String fileName = "fileAT";
	private static String projectName = "ProjectAT";
	private static String workspaceName = "WorkspaceAT";
	private static String valueBinding = "99.2";
	private static String valueMultiBinding = "99.0";
	private static Integer screenshotIndex = 1;

	private static final Logger LOG = Logger.getLogger(TestCase.class
			.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {

		LOG.log(Level.INFO, "Starting TestCaseDefault...");

		TestCase.logout();
		TestCase.login();

	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TestCaseDefault finished");
	}

	/**
	 * IDT-1:Creating a workspace.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test01CreateWorkspace() throws InterruptedException {

		// TODO: check if there is a workspace named like workspaceName variable

		IdeasAppActions.goEditorPage();

		TestCase
				.getExpectedActions()
				.getWait()
				.until(ExpectedConditions.visibilityOfElementLocated(By
						.id("menuToggler")));

		System.out.println("testCreateWorkspace :: Creating a workspace...");

		TestCase.getExpectedActions().click(By.id("menuToggler"));
		TestCase.getExpectedActions().click(By.className("addWorkspace"));

		System.out.println("\t :: Inserting name \"" + workspaceName
				+ "\" for workspace.");

		TestCase.getExpectedActions().sendKeys(
				By.cssSelector("input.form-control.focusedInput"),
				workspaceName);
		TestCase.getExpectedActions().click(By.linkText("Create"));

		Thread.sleep(500);

		TestCase.getWebDriver().findElement(By.id("appMainContentBlocker"))
				.click();

		System.out.println("\t :: Workspace \"" + workspaceName
				+ "\" was successfully created.");
		echoCommandApi("Workspace \"" + workspaceName
				+ "\" was successfully created.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-2:Removing a workspace.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test14RemoveWorkspace() throws InterruptedException {

		// TODO: check if workspace already exists.

		IdeasAppActions.goEditorPage();

		System.out.println("testRemoveWorkspace :: Removing workspace...");

		TestCase.getExpectedActions().sendKeys(
				By.cssSelector("input.gcli-in-input"),
				"deleteCurrentWorkspace", Keys.RETURN);

		TestCase.getWebDriver()
				.get(TestCase.getWebDriver().getCurrentUrl()); // refreshing

		TestCase.getExpectedActions().click(By.id("menuToggler"));

		System.out.println("\t :: Workspace \"" + workspaceName
				+ "\" was successfully removed.");
		echoCommandApi("Workspace \"" + workspaceName
				+ "\" was successfully removed.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-3:Creating a file.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test03CreateFile() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out.println("testCreateFile :: Creating a file...");

		TestCase.getExpectedActions().click(
				By.cssSelector("a.dynatree-title"));
		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("div#editorSidePanelHeaderAddProject div.dropdown-toggle"));
		TestCase.getExpectedActions().click(
				By.linkText("Create iAgreeTemplate file"));

		System.out
				.println("\t :: Inserting \"" + fileName + "\" as file name.");

		TestCase.getExpectedActions().sendKeys(
				By.cssSelector("input.form-control.focusedInput"), fileName);
		TestCase.getExpectedActions().click(By.linkText("Create"));

		TestCase.getExpectedActions().click(
				By.cssSelector("span.dynatree-expander"));

		System.out.println("\t :: File \"" + fileName
				+ ".at\" was successfully created.");
		echoCommandApi("File \"" + fileName + ".at\" was successfully created.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-4:Remove a file.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test11RemoveFile() throws InterruptedException {

		ExpectedActions action = TestCase.getExpectedActions();

		IdeasAppActions.goEditorPage();

		System.out.println("testRemoveFile :: Removing a file...");

		TestCase.getExpectedActions().click(By.linkText(fileName + ".html"));

		Thread.sleep(500);

		action.contextNodeMenu(By.linkText(fileName + ".html"));

		Thread.sleep(500);

		TestCase.getExpectedActions().click(
				By.cssSelector("div#projectsTreeContainer > ul > li.delete a"));

		// TODO: check if the file has been removed successfully. (assert)

		System.out.println("\t :: File \"" + fileName
				+ ".html\" was successfully removed.");
		echoCommandApi("File \"" + fileName
				+ ".html\" was successfully removed.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-5:Renaming a file.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test12RenameFile() throws InterruptedException {

		ExpectedActions action = TestCase.getExpectedActions();

		IdeasAppActions.goEditorPage();

		System.out.println("testRenameFile :: Renaming a file...");

		TestCase.getExpectedActions().click(By.linkText(fileName + ".at"));

		Thread.sleep(500);
		action.contextNodeMenu(By.linkText(fileName + ".at"));

		TestCase.getExpectedActions().click(By.linkText("Edit"));
		TestCase.getExpectedActions().sendKeys(
				By.cssSelector("input#editNode"), "mod_", Keys.RETURN);

		// TODO: check if the file has been successfully renamed. (assert)

		System.out.println("\t :: File \"" + fileName
				+ ".at\" was successfully renamed to mod_" + fileName + ".at.");
		echoCommandApi("File \"" + fileName
				+ ".at\" was successfully renamed to mod_" + fileName + ".at."); // saving
																					// file
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-11:Creating a project.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test02CreateProject() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out.println("testCreateProject :: Creating a project...");

		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("div#editorSidePanelHeaderAddProject div.dropdown-toggle"));
		TestCase.getExpectedActions().click(By.linkText("Create Project"));

		System.out.println("\t :: Inserting \"" + projectName
				+ "\" as name project.");

		TestCase.getExpectedActions().sendKeys(
				By.cssSelector("input.form-control.focusedInput"), projectName);
		TestCase.getExpectedActions().click(By.linkText("Create"));

		System.out.println("\t :: Project \"" + projectName
				+ "\" was successfully created.");
		echoCommandApi("Project \"" + projectName
				+ "\" was successfully created.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-12:Remove a project.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test13RemoveProject() throws InterruptedException {

		ExpectedActions action = TestCase.getExpectedActions();

		IdeasAppActions.goEditorPage();

		System.out.println("testRemoveProject :: Removing a project...");

		TestCase.getExpectedActions().click(By.linkText(projectName));

		Thread.sleep(500);

		action.contextNodeMenu(By.linkText(projectName));

		Thread.sleep(500);

		TestCase.getExpectedActions().click(
				By.cssSelector("div#projectsTreeContainer > ul > li.delete a"));

		// TODO: comprobar que realmente se ha modificado éxitosamente. (assert)

		System.out.println("\t :: Project \"" + projectName
				+ "\" was successfully removed.");
		echoCommandApi("Project \"" + projectName
				+ "\" was successfully removed.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-14:Creating a Binding.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test06CreateBinding() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out.println("testCreateBinding :: Creating a Binding...");

		TestCase.getExpectedActions().click(By.linkText(fileName + ".at"));
		TestCase.getExpectedActions().click(By.id("editorToggleInspector"));
		TestCase.getExpectedActions().click(
				By.cssSelector("li.descriptionInspectorTab.active"));
		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("div.inspectorButton.inspectorButtonAdd div.dropdown-toggle"));

		// TODO: comprobar si el inspector está abierto o cerrado.

		System.out.println("\t :: Selecting an editor fragment text.");

		Thread.sleep(1000);

		// Select editor fragment text
		selectEditorFragment(TestCase.getWebDriver(), 23, 26, 23, 30);
		Thread.sleep(500);
		// ((JavascriptExecutor)
		// TestConfig.getWebDriver()).executeScript("var range=document.editor.getSelectionRange().clone();range.end.column=11;range.end.row=23;range.start.column=9;range.start.row=23;document.editor.selection.setRange(range);");
		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("#descriptionInspectorHeaderAdd button.proceedAction"));

		// Select inspector fragment text
		System.out.println("\t :: Selecting an inspector fragment text.");
		((JavascriptExecutor) TestCase.getWebDriver())
				.executeScript("var selection = window.getSelection();var range = document.createRange();var textNode = document.getElementById('IDTbindingAT1');range.setStart(textNode, 0);range.setEnd(textNode, 1);selection.removeAllRanges();selection.addRange(range);$('#inspectorContent').click();");
		// .executeScript("var selection = window.getSelection();var range = document.createRange();range.selectNodeContents(document.getElementById('IDTbindingAT1'));selection.removeAllRanges();selection.addRange(range);$('#inspectorContent').click();");
		// works on chrome
		Thread.sleep(500);
		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("#descriptionInspectorHeaderAdd button.proceedAction"));

		// Done
		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("#descriptionInspectorHeaderAdd button.proceedAction"));

		System.out.println("\t :: Binding was successfully created.");
		echoCommandApi("Binding was successfully created.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-15:Create a description file.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test05CreateDescriptionFile() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out
				.println("testCreateDescriptionFile :: Creating a description file...");

		TestCase.getExpectedActions().click(By.className("dynatree-ico-c"));
		TestCase.getExpectedActions().click(By.id("editorToggleInspector"));
		TestCase.getExpectedActions().click(
				By.linkText("Create a description file"));

		Thread.sleep(1000); // animations

		TestCase.getJs().executeScript(
				"document.editor.selectAll();document.editor.removeLines();"); // erasing
		// previous
		// content
		// for
		// a
		// better
		// visualization.

		TestCase
				.getJs()
				.executeScript(
						"document.editor.session.setValue('"
								+ "<table>\\n"
								+ "\\t<tr>\\n"
								+ "\\t\\t<td><b>Monthly Uptime Percentage</b></td>\\n"
								+ "\\t\\t<td><b>Service Credit Percentage</b></td>\\n"
								+ "\\t</tr>\\n"
								+ "\\t<tr>\\n"
								+ "\\t\\t<td>Less than <span>99.95</span>% but equal to or greater than <span>99.0</span>%</td>\\n"
								+ "\\t\\t<td><span>10%</span></td>\\n"
								+ "\\t</tr>\\n"
								+ "\\t<tr>\\n"
								+ "\\t\\t<td>Less than <span id=\"IDTbindingAT1\">99.0</span>%</td>\\n"
								+ "\\t\\t<td><span>30%</span></td>\\n"
								+ "\\t</tr>\\n" + "</table>');"); // sets a new
																	// content
																	// for
																	// editor

		System.out.println("\t :: Description file " + fileName
				+ ".html was successfully created.");
		echoCommandApi("Description file " + fileName
				+ ".html was successfully created.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-16:Create MultiBinding.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test08CreateMultiBinding() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out
				.println("testCreateMultiBinding :: Creating a MultiBinding...");

		TestCase.getExpectedActions().click(By.linkText(fileName + ".at"));
		TestCase.getExpectedActions().click(By.id("editorToggleInspector"));

		Thread.sleep(1000);

		// selectEditorFragment(TestConfig.getWebDriver(), 24, 25, 24, 29);
		TestCase
				.getJs()
				.executeScript(
						""
								+ "document.editor.selection.clearSelection();"
								+ "document.editor.selection.setSelectionRange({start:{row:24,column:25},end:{row:24,column:29}});"
								+ "$('#IDTbindingAT1').contextmenu();$('#editorInspectorLoader ul li.multiBinding > a').click();");

		Thread.sleep(1000);

		// TODO: comprobar que se ha creado correctamente.

		System.out.println("\t :: MultiBinding was successfully created.");
		echoCommandApi("MultiBinding was successfully created.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-17:Update MultiBinding value.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test09UpdateMultiBindingValue() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out
				.println("testUpdateMultiBindingValue :: Modifying a MultiBinding value...");

		TestCase.getExpectedActions().click(By.linkText(fileName + ".at"));

		// TODO: comprobar si el inspector está abierto o cerrado.

		TestCase.getExpectedActions().click(By.id("editorToggleInspector"));
		Thread.sleep(1000);
		TestCase.getExpectedActions().doubleClick(By.id("ideasBinding_1"));

		TestCase.getJs().executeScript(
				"$('#ideasBindingValueToModify').val(" + valueMultiBinding
						+ ");");

		TestCase
				.getWebDriver()
				.findElement(
						By.cssSelector("#appGenericModal div.modal-footer a.continue"))
				.click();

		// TODO: comprobar que se ha modificado el valor tanto en el inspector
		// como en el editor.

		System.out
				.println("\t :: MultiBinding value was successfully updated.");
		echoCommandApi("MultiBinding value was successfully updated."); // saving
																		// file
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-18:Update Binding value.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test07UpdateBindingValue() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out
				.println("testUpdateBindingValue :: Modifying the Binding value...");

		TestCase.getExpectedActions().click(By.linkText(fileName + ".at"));

		// TODO: comprobar si el inspector está abierto o cerrado.

		TestCase.getExpectedActions().click(By.id("editorToggleInspector"));
		Thread.sleep(1000);
		TestCase.getExpectedActions().doubleClick(By.id("ideasBinding_1"));

		TestCase.getJs().executeScript(
				"$('#ideasBindingValueToModify').val(" + valueBinding + ");");

		TestCase
				.getWebDriver()
				.findElement(
						By.cssSelector("#appGenericModal div.modal-footer a.continue"))
				.click();

		// TODO: comprobar que se ha modificado el valor tanto en el inspector
		// como en el editor.

		System.out.println("\t :: Binding value was successfully updated.");
		echoCommandApi("Binding value was successfully updated."); // saving
																	// file
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-19:Remove a MultiBinding.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test10RemoveMultiBinding() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out
				.println("testRemoveMultiBinding :: Removing MultiBinding...");

		TestCase.getExpectedActions().click(By.linkText(fileName + ".at"));
		TestCase.getExpectedActions().click(By.id("editorToggleInspector"));
		TestCase.getExpectedActions().click(
				By.cssSelector("li.descriptionInspectorTab.active"));
		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("div.inspectorButton.inspectorButtonRemove div.dropdown-toggle"));

		Thread.sleep(1000);

		TestCase.getWebDriver().findElement(By.id("ideasBinding_1")).click();

		TestCase
				.getExpectedActions()
				.click(By
						.cssSelector("div#appGenericModal div.modal-footer a.continue"));

		// TODO: comprobar que se ha eliminado el MultiBinding
		// satisfactoriamente.

		Thread.sleep(1000);

		System.out.println("\t :: MultiBinding was successfully removed.");
		echoCommandApi("MultiBinding was successfully removed.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	/**
	 * IDT-22:Edit a file.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test04EditFile() throws InterruptedException {

		IdeasAppActions.goEditorPage();

		System.out.println("testEditFile :: Editing a file...");

		TestCase.getExpectedActions()
				.click(By.cssSelector(".dynatree-ico-c"));

		System.out.println("\t :: Introduciendo contenido al fichero");

		Thread.sleep(2000);

		TestCase
				.getJs()
				.executeScript(
						"document.editor.session.setValue('"
								+ "Template EC2 version 1.0\\n"
								+ "\\tProvider \"Amazon\" as Responder;\\n"
								+ "\\n"
								+ "\\tMetrics:\\n"
								+ "\\t\\tpercent: integer[0..100]; \\n"
								+ "\\t\\tfloatPercent: float[0..100]; \\n"
								+ "\\t\\tsize: integer[0..500];\\n"
								+ "\\n"
								+ "AgreementTerms\\n"
								+ "\\n"
								+ "\\tService AWS-S3PremiumSupport availableAt \"http://aws.amazon.com/s3/\"\\n"
								+ "\\t\\tGlobalDescription\\n"
								+ "\\t\\t\\tRegionUnits: size;\\n"
								+ "\\t\\t\\tSCP: percent;\\n"
								+ "\\n"
								+ "\\tMonitorableProperties\\n"
								+ "\\t\\tglobal:\\n"
								+ "\\t\\t\\tMUP: floatPercent;\\n"
								+ "\\n"
								+ "\\tGuaranteeTerms\\n"
								+ "\\t\\tG1: Provider guarantees MUP >= 99.95; \\n"
								+ "\\t\\t\\tonlyIf (RegionUnits > 2); \\n"
								+ "\\t\\t\\twith monthly penalty \\n"
								+ "\\t\\t\\t\\tof SCP = 10 if MUP <= 99.0 AND MUP < 99.95; \\n"
								+ "\\t\\t\\t\\tof SCP = 30 if MUP < 99.0;\\n"
								+ "\\t\\t\\tend\\n" + "\\n"
								+ "CreationConstraints\\n" + "\\tC1: none;\\n"
								+ "\\n" + "EndTemplate');");

		Thread.sleep(1000);

		// TODO: comprobar que el contenido se ha almacenado correctamente en el
		// servidor.

		System.out.println("\t :: File \"" + fileName
				+ ".at\" was successfully edited.");
		echoCommandApi("File \"" + fileName + ".at\" was successfully edited.");
		screenshot(TestCase.getWebDriver(), screenshotIndex.toString()
				+ ".png");

	}

	// Utilities

	public static void echoCommandApi(String msg) {

		try {
			TestCase.getJs().executeScript(
					"" + "if (CommandApi.echo) {"
							+ "CommandApi.echo('IDT-console: " + msg + "');"
							+ "}");

			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Take a screenshot of the WebDriver.
	 * 
	 * @param driver
	 * @param path
	 */
	public static void screenshot(WebDriver driver, String path) {

		if (path instanceof String && path != "" && driver instanceof WebDriver
				&& screenshotIndex > 0) {

			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);

			try {
				FileUtils.copyFile(scrFile, new File("screenshots/" + path));
				screenshotIndex++;
			} catch (IOException e) {
				System.out.println("Unable to copy screenshot to path");
				e.printStackTrace();
			}

		}
	}

	/**
	 * Select a text fragment of IDEAS' editor.
	 * 
	 * @param driver
	 * @param xFrom
	 * @param yFrom
	 * @param xTo
	 * @param yTo
	 */
	public static void selectEditorFragment(WebDriver driver, Integer xFrom,
			Integer yFrom, Integer xTo, Integer yTo) {

		TestCase.getJs().executeScript(
				"document.editor.selection.setSelectionRange({" + "start:{row:"
						+ xFrom.toString() + ",column:" + yFrom.toString()
						+ "}," + "end:{row:" + xTo.toString() + ",column:"
						+ yTo.toString() + "}});"
						+ "$('div.ace_content').click();");

	}

	/**
	 * Click an element from the WebDriver and drag to an offset position.
	 * 
	 * @param driver
	 * @param locator
	 * @param xFrom
	 * @param yFrom
	 * @param xTo
	 * @param yTo
	 */
	public static void clickAndDrag(WebDriver driver, By locator, int xFrom,
			int yFrom, int xTo, int yTo) {

		WebElement element = driver.findElement(locator);
		Actions builder = new Actions(driver);

		builder.moveToElement(element, xFrom, yFrom).clickAndHold()
				.moveByOffset(xTo, yTo).release().perform();

		System.out.println("\"clickAndDragged\"");

	}

	/**
	 * Login with TestConfig.getUser() and password established as an attribute
	 * of this class.
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public static void login(WebDriver driver) {

		IdeasAppActions.goLoginPage();

		System.out.println("Opening session");

		TestCase.getExpectedActions().sendKeys(By.id("username"),
				TestCase.getSeleniumAutotesterUser());
		TestCase.getExpectedActions().sendKeys(By.id("password"),
				TestCase.getSeleniumAutotesterPassword());
		TestCase.getExpectedActions().click(By.id("loginButton"));

		screenshot(driver, screenshotIndex.toString() + ".png");

		// assertEquals(getUrlAbsolute("app/editor"), driver.getCurrentUrl());
	}

	/**
	 * Logout TestConfig.getUser().
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public static void logout(WebDriver driver) throws InterruptedException {
		TestCase.logout();
		screenshot(driver, screenshotIndex.toString() + ".png");
	}

}
