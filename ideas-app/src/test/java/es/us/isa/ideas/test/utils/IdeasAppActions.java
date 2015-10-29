package es.us.isa.ideas.test.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Resume some of the normal actions you can do with ideas-app.
 * 
 * @author feserafim
 */

public class IdeasAppActions {

	private static final Logger LOG = Logger.getLogger(IdeasAppActions.class
			.getName());

	/**
	 * Register an user giving all profile information. Return true if
	 * successful message was received.
	 * 
	 * @param name
	 * @param email
	 * @param phone
	 * @param address
	 * @return true if the has actually registered
	 */
	public static boolean registerUser(String name, String email, String phone,
			String address) {

		boolean ret = false;

		try {

			TestCase.logout();
			IdeasAppActions.goSignUpPage();
			ExpectedActions action = TestCase.getExpectedActions();

			action.sendKeys(By.cssSelector("#name"), name);
			action.sendKeys(By.cssSelector("#email"), email);
			action.sendKeys(By.cssSelector("#phone"), phone);
			action.sendKeys(By.cssSelector("#address"), address);
			action.click(By.cssSelector("#settingsSubmitChanges"));

			Thread.sleep(1000); // modal animation

			String modalHeader = TestCase
					.getWebDriver()
					.findElement(
							By.cssSelector("#loginOKPanel > div > div > div.modal-header > h4"))
					.getText();

			String msgEmailAlreadyTaken = TestCase.getWebDriver()
					.findElement(By.cssSelector("#statusPanel")).getText();

			boolean ret1 = "Account created successfully".equals(modalHeader);
			boolean ret2 = "The email address you entered is already in use"
					.equals(msgEmailAlreadyTaken);

			if (!ret1)
				LOG.log(Level.INFO,
						"It appears that no successful modal window has been showed. Quitting register test.");

			if (ret2)
				LOG.log(Level.INFO, msgEmailAlreadyTaken
						+ ". Quitting register test");

			ret = ret1 && !ret2;

		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return ret;

	}

	public static boolean setUpFormDataTwitterLogin(String tw_user,
			String tw_pass) {

		boolean ret = false;

		try {

			TestCase.logout();
			IdeasAppActions.goLoginPage();

			ExpectedActions action = TestCase.getExpectedActions();

			action.click(By.cssSelector("#tw_signin > button"));
			action.sendKeys(
					By.cssSelector("#oauth_form > fieldset.sign-in > div.row.user > label"),
					tw_user);
			action.sendKeys(
					By.cssSelector("#oauth_form > fieldset.sign-in > div.row.password > label"),
					tw_pass);
			action.click(By.cssSelector("#allow"));

			Thread.sleep(2000);
			ret = TestCase.getCurrentUrl().contains("app/editor");

			if (!ret) {

				String selectorMsgHeader = "#pagesContent > h3:nth-child(2)";
				TestCase.waitForVisibleSelector(selectorMsgHeader);
				String msgHeader = TestCase.getWebDriver()
						.findElement(By.cssSelector(selectorMsgHeader))
						.getText();

				boolean ret_connectedTwitter = msgHeader.toLowerCase()
						.contains("connected to twitter");

				// ret = !ret_en || !ret_es || ret_connectedTwitter;
				ret = ret_connectedTwitter;

			}

		} catch (NoSuchElementException e) {
			LOG.info("Test may not be well performed because a element from DOM wasn\'t found. Please, contact realease manager to review test code execution.");
			e.printStackTrace();
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		}

		return ret;

	}

	public static boolean setUpFormDataGoogleLogin(String go_user,
			String go_pass) {

		boolean ret = false;

		try {

			ExpectedActions action = TestCase.getExpectedActions();
			action.sendKeys(By.cssSelector("#Email"), go_user);
			action.click(By.cssSelector("#next"));
			action.sendKeys(By.cssSelector("#Passwd"), go_pass);
			action.click(By.cssSelector("#signIn"));

			Thread.sleep(1000);

			String msgStatusCode = TestCase
					.getWebDriver()
					.findElement(
							By.cssSelector("#af-error-container > p:nth-child(2) > b"))
					.getText();

			ret = !"400.".equals(msgStatusCode);

			if (!ret)
				LOG.info("Test will fail because webdriver received a Google 400 error page");

		} catch (NoSuchElementException e) {
			LOG.info("Test may not be well performed because a element from DOM wasn\'t found. Please, contact realease manager to review test code execution.");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * Register an user giving all profile information. Return true if user
	 * wasn't registered yet and successful message was received.
	 * 
	 * @param tw_user
	 * @param tw_pass
	 * @return
	 */
	public static boolean registerTwitterUser(String tw_user, String tw_pass) {

		boolean ret = false;

		try {

			if (IdeasAppActions.setUpFormDataTwitterLogin(tw_user, tw_pass)) {

				String currentUrl = TestCase.getWebDriver().getCurrentUrl();
				boolean ret1 = false;
				boolean ret2 = currentUrl.contains("app/editor");

				if (ret2)
					LOG.log(Level.INFO,
							"It seems \'"
									+ tw_user
									+ "\' user was already registered to IDEAS. So this test will fail because it does not fulfill its purpose");
				else {
					String msgConnectedToTwitter = TestCase
							.getWebDriver()
							.findElement(
									By.cssSelector("#pagesContent > h3:nth-child(2)"))
							.getText();

					ret1 = "Connected to Twitter".equals(msgConnectedToTwitter);
				}
				ret = ret1 && !ret2;

			}

		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * Register an user giving all profile information. Return true if no google
	 * returns a 200 page status code.
	 * 
	 * @param go_user
	 * @param go_pass
	 * @return
	 */
	public static boolean checkGoogleRegisterOAuth(String go_user,
			String go_pass) {

		boolean ret = false;

		try {

			ExpectedActions action = TestCase.getExpectedActions();

			LOG.info("Logging google oauth with \'" + go_user + "\' and \'"
					+ go_pass + "\'");

			action.click(By.cssSelector("#go_signin > button"));

			action.sendKeys(By.cssSelector("#Email"), go_user);
			action.click(By.cssSelector("#next"));

			action.sendKeys(By.cssSelector("#Passwd"), go_pass);
			action.click(By.cssSelector("#signIn"));

			Thread.sleep(1000);

			String msgStatusCode = TestCase
					.getWebDriver()
					.findElement(
							By.cssSelector("#af-error-container > p:nth-child(2) > b"))
					.getText();

			ret = !"400.".equals(msgStatusCode);

		} catch (NoSuchElementException e) {
			e.printStackTrace();
			LOG.log(Level.INFO,
					"Test wasn\'t well performed. Please, contact realease manager to review test code execution.");
			ret = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;

	}

	/**
	 * Selecciona un workspace dado su nombre.
	 * 
	 * @precondition Es necesario que el usuario esté logueado y que el WS
	 *               exista.
	 * @param workspaceName
	 * @throws InterruptedException
	 */
	public static void selectWorkspace(String workspaceName)
			throws InterruptedException {

		ExpectedActions action = TestCase.getExpectedActions();

		// WS list
		action.click(By.cssSelector("#menuToggler"));
		Thread.sleep(1000); // animation
		action.click(By.cssSelector("#workspacesNavContainer > li.active > a"));
		action.click(By.cssSelector("#appMainContentBlocker"));

	}

	public static void expandMenu(String projectName) {

		// String cssSelector = "#projectsTree > ul > li > span";

	}

	/**
	 * Check if the current page contains a path.
	 * 
	 * @param path
	 * @return
	 */
	public static boolean containsCurrentPage(String path) {

		return TestCase.getWebDriver().getCurrentUrl().toLowerCase()
				.contains(path.toLowerCase());

	}

	/**
	 * Go to IDEAS user account configuration from the application editor page.
	 * 
	 * @return true if webdriver could access "User Account" page.
	 */
	public static boolean goUserAccountPage() {

		boolean ret = false;
		String path = "/settings/user#profile";

		if (!TestCase.getCurrentUrl().contains(path)) {

			IdeasAppActions.goEditorPage();

			ExpectedActions action = TestCase.getExpectedActions();

			// open settings menu
			String selectorSettingsMenu = "#userTabHandler";
			TestCase.waitForVisibleSelector(selectorSettingsMenu);
			action.click(By.cssSelector(selectorSettingsMenu));

			// Thread.sleep(500); // animation

			// selects "User Account" menu link
			String selectorUserAccountMenu = "ul.dropdown-menu:nth-child(4) > li:nth-child(1) > a:nth-child(1)";
			TestCase.waitForVisibleSelector(selectorUserAccountMenu);
			action.click(By.cssSelector(selectorUserAccountMenu));

			LOG.info("Loading user account page...");
			TestCase.getWebDriver().get(TestCase.getUrlAbsolute(path));

			ret = TestCase.getCurrentUrl().contains("/settings/user#profile");

		} else {
			ret = true;
		}

		return ret;
	}

	public static boolean goRelativePath(String relativePath) {

		boolean ret = false;

		if (!TestCase.getWebDriver().getCurrentUrl().contains(relativePath)) {

			if ("/".equals(relativePath)) {
				relativePath = "";
			}

			System.out.println("accessing: "+TestCase.getBaseUrl() + relativePath);
			TestCase.getWebDriver().get(TestCase.getBaseUrl() + relativePath);
			try {
				
				//TODO: better solution waiting for a URL modification
				Thread.sleep(1000); // changing url
				
				System.out.println("currentUrl: "+TestCase.getCurrentUrl());
				boolean ret1 = TestCase.getCurrentUrl().contains(relativePath);
				// boolean ret2 = "200".equals(TestCase.getStatusCode(TestCase
				// .getUrlAbsolute(relativePath)));
				ret = ret1;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				ret = false;
			}

		} else {
			ret = true;
		}

		return ret;

	}

	/**
	 * Go to IDEAS user sign up.
	 */
	public static boolean goSignUpPage() {
		LOG.info("Loading sign up page...");
		return IdeasAppActions.goRelativePath("/settings/user");
	}

	/**
	 * Go to IDEAS home webpage.
	 */
	public static boolean goHomePage() {
		LOG.info("Loading home page...");
		return IdeasAppActions.goRelativePath("/");
	}

	/**
	 * Go to IDEAS editor webpage.
	 */
	public static boolean goEditorPage() {
		LOG.info("Loading editor page...");
		return IdeasAppActions.goRelativePath("/app/editor");
	}

	/**
	 * Go to IDEAS login webpage.
	 */
	public static boolean goLoginPage() {
		LOG.info("Loading login page...");
		return IdeasAppActions.goRelativePath("/security/login");
	}

	/**
	 * Go to IDEAS logout webpage.
	 */
	public static boolean goLogoutPage() {
		LOG.info("Loading logout page...");
		return IdeasAppActions.goRelativePath("/j_spring_security_logout");
	}

	/**
	 * Go to IDEAS user social tab that can be found in "User Account" page.
	 * 
	 * @return true if webdriver could access "Social" tab
	 */
	public static boolean goSocialPage() {
		LOG.info("Loading social page...");
		return IdeasAppActions.goRelativePath("/settings/user#social");
	}

	/**
	 * Ejecuta el comando echo de la línea de comandos de ideas-app indicando el
	 * mensaje a escribirse en la consola.
	 * 
	 * @param driver
	 * @param msg
	 * @throws InterruptedException
	 */
	public static void echoCommand(WebDriver driver, String msg)
			throws InterruptedException {

		goEditorPage();

		((JavascriptExecutor) driver).executeScript(""
				+ "if (CommandApi.echo) {" + "CommandApi.echo('IDT-console: "
				+ msg + "');" + "}");

	}

	/**
	 * Ejecuta una lista de comandos pasados por parámetro. Cada ejecución
	 * difiere en 500 milisengundos.
	 * 
	 * @param driver
	 * @param cmd
	 * @throws InterruptedException
	 */
	public void executeCommands(WebDriver driver, String... cmds)
			throws InterruptedException {

		goEditorPage();

		for (String cmd : cmds) {
			new ExpectedActions().sendKeys(
					By.cssSelector("input.gcli-in-input"), cmd, Keys.RETURN);

			Thread.sleep(500);
		}

	}

	/**
	 * Obtiene el resultado (en booleano) de los tests aplicado a partir de
	 * TestModule dado false.
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public static boolean checkTestModuleOkResult(final WebDriver driver)
			throws InterruptedException {

		goEditorPage();

		Thread.sleep(500); // tiempo de carga necesario para asegurar la carga
							// de la página.

		// Método de comprobación implícito
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver arg0) {
				return driver.findElements(
						By.cssSelector("#gcli-root div.gcli-row-out")).size() > 1; // original
																					// size
																					// is
																					// 1
																					// on
																					// page
																					// load
			}
		});

		Object result = (Object) TestCase
				.getJs()
				.executeScript(
						"return document.getElementById('gcli-root').textContent.search(/false/i);");

		if (result != null && result instanceof Long) {
			result = (Long) result;
		} else {
			result = ""; // hace que falle el test porque "result" no tiene un
							// tipo de valor esperado
		}

		return result.toString().equals("-1");
	}

	/**
	 * Check if '/researcher/principaluser/' retrieves a '200' status code.
	 * 
	 * @throws InterruptedException
	 */
	public static boolean isAnyUserLogged() throws InterruptedException {

		boolean ret = false;
		String url = TestCase.getUrlAbsolute("researcher/principaluser/");
		ret = "200".equals(TestCase.getStatusCode(url));

		return ret;

	}

}
