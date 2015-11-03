package es.us.isa.ideas.test.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import es.us.isa.ideas.test.utils.ExpectedActions;
import es.us.isa.ideas.test.utils.IdeasAppActions;
import es.us.isa.ideas.test.utils.TestCase;

/**
 * Attempt to register and login an user.
 * 
 * @author feserafim
 */
public class TestCaseLogin {

	private static final Logger LOG = Logger.getLogger(TestCaseLogin.class
			.getName());

	@BeforeClass
	public static void setUp() {
		LOG.log(Level.INFO, "Starting \'TestCaseLogin\'...");
		LOG.info("Setting up \'TestCaseLogin\' configuration...");
		// none execution
		LOG.info("\'TestCaseLogin\' configuration was successfully set up");
	}

	/**
	 * Before each test, user will be logout and redirected to login page.
	 * 
	 * @throws InterruptedException
	 */
	@Before
	public void setUpTest() throws InterruptedException {
		LOG.info("Setting up test configuration...");

		TestCase.logout();
		IdeasAppActions.goLoginPage();

		LOG.info("test configuration was successful set up");
	}

	/**
	 * Close WebDriver after all test executions.
	 */
	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "Tearing down \'TestCaseLogin\'...");

		// TestConfig.getWebDriver().close();

		LOG.info("\'TestCaseLogin\' was successfully tore down");
		LOG.info("\'TestCaseLogin\' finished");
	}

	/**
	 * Check if an user can register. This test uses 'selenium.properties' file
	 * in order to know which username and password it has to register.
	 */
	@Test
	public void testRegister() {

		boolean ret = true;

		try {

			// Test user profile
			String name = TestCase.getSeleniumAutotesterUser();
			String email = TestCase.getSeleniumUserEmail();
			String phone = TestCase.getSeleniumUserPhone();
			String address = TestCase.getSeleniumUserAddress();

			LOG.info("Registering user with email \'" + email + "\'");
			ret = IdeasAppActions.registerUser(name, email, phone, address);

			if (ret) {

				// TODO: get isa gmail login parameters from resources

				TestCase.getWebDriver().get("gmail.com");

				Thread.sleep(2000);

				ExpectedActions action = TestCase.getExpectedActions();

				LOG.info("Logging to gmail ideas email");

				action.sendKeys(By.cssSelector("#Email"),
						"ideas.isa.us@gmail.com");
				action.click(By.cssSelector("#next"));
				action.sendKeys(By.cssSelector("#Passwd"),
						"ideas.isa.us%GOOGLE");
				action.click(By.cssSelector("#signIn"));

				Thread.sleep(7000);

				action.click(By
						.cssSelector("#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)")); // opening
				// email
				LOG.info("Opening \'IDEAS account confirmation\' email");

				Thread.sleep(4000);

				// get url
				String urlConfirmation = (String) TestCase
						.getJs()
						.executeScript(
								""
										+ "return document.getElementsByClassName('ads')[0].textContent.match(/http.+code=[a-zA-Z0-9\\-]+/i)[0]");

				TestCase.getWebDriver().get(urlConfirmation);

				Thread.sleep(3000);

				String modalTitle = TestCase
						.getWebDriver()
						.findElement(
								By.cssSelector("#message > div > div > div.modal-header > h4"))
						.getText();

				if ("Account validated successfully".equals(modalTitle)) {
					// Go to your email and copy your password

					TestCase.getWebDriver().get("gmail.com");

					Thread.sleep(10000);

					// opens welcome to IDEAS email
					action.click(By
							.cssSelector("#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)"));

					LOG.info("Opening \'Welcome to IDEAS\' email");

					Thread.sleep(4000);

					String password = (String) TestCase
							.getJs()
							.executeScript(
									""
											+ "var str=document.getElementsByClassName('gs')[0].textContent;"
											+ "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0]");

					LOG.info("Copying user password \'" + password + "\'");

					// TODO: verify password ok
					TestCase.loginWithParams(name, password);

					Thread.sleep(3000);

					ret = ret
							&& TestCase.getWebDriver().getCurrentUrl()
									.contains("app/editor");

					if (ret) {
						LOG.log(Level.INFO, "test ok");
					} else {
						LOG.log(Level.INFO, "test failed");
					}

				} else {
					ret = false;
					LOG.log(Level.INFO,
							"test failed. It wasn\'t possible to validate the user register");
				}

			} else {
				LOG.log(Level.INFO, "test failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		} finally {
			assertTrue(ret);
			LOG.log(Level.INFO, "testUserRegister finished");
		}

	}

	/**
	 * Check if an user can register by twitter social. This test might fail if
	 * user is already registered. In that case you should focus in
	 * 'testTwitterLogin' result.
	 */
	@Test
	public void testRegisterTwitter() {

		boolean ret = false;

		try {

			LOG.log(Level.INFO, "testUseTwitterRegister...");

			String tw_user = TestCase.getSeleniumTwitterUser();
			String tw_pass = TestCase.getSeleniumTwitterPasswd();

			ret = IdeasAppActions.registerTwitterUser(tw_user, tw_pass);

			if (ret)
				LOG.log(Level.INFO, "test ok");
			else
				LOG.log(Level.INFO, "test failed");

		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		} finally {
			LOG.log(Level.INFO, "testUseTwitterRegister finished");
			assertTrue(ret);
		}

	}

	/**
	 * Test if the user receives a '200' status code from Google OAuth. This
	 * test might fail if you are using a different application name from
	 * 'IDEAS' or a different server domain. That is because Google OAuth checks
	 * if the response base URL is https://labs.isa.us.es/IDEAS.
	 */
	@Test
	public void testRegisterGoogleOAuthResponse() {

		boolean ret = false;

		try {

			LOG.log(Level.INFO, "testRegisterGoogleOAuthResponse...");

			ExpectedActions action = TestCase.getExpectedActions();
			String go_user = TestCase.getSeleniumGoogleUser();
			String go_pass = TestCase.getSeleniumGooglePasswd();

			LOG.info("Logging google oauth with \'" + go_user + "\' and \'"
					+ go_pass + "\'");

			action.click(By.cssSelector("#go_signin > button"));

			ret = IdeasAppActions.setUpFormDataGoogleLogin(go_user, go_pass);

			if (ret)
				LOG.log(Level.INFO, "test ok");
			else
				LOG.log(Level.INFO, "test failed");

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "testRegisterGoogleOAuthResponse finished");
		}

	}

	/**
	 * Check if user is redirected to '/app/editor' when login.
	 */
	@Test
	public void testEditorPageLoadedAfterLogin() {

		boolean ret = false;

		try {

			LOG.info("testEditorLoadedProperlyAfterLogin...");

			TestCase.login();
			Thread.sleep(2000);

			ret = TestCase.getCurrentUrl().contains("app/editor");

			if (ret) {
				LOG.log(Level.INFO, "test ok");
			} else {
				LOG.log(Level.INFO, "test failed");
			}

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testEditorLoadedProperlyAfterLogin finished");
		}

	}

	/**
	 * Check if user can register using his twitter social by accessing the
	 * image link in the 'Sign up' page.
	 */
	@Test
	public void testSignUpTwitterSocial() {

		try {

			LOG.info("testSignUpTwitterSocial finished");

			ExpectedActions action = new ExpectedActions();

			LOG.log(Level.INFO, "dont remember login page");

			action.click(By
					.cssSelector("#dontRememberLogin > a:nth-child(1) > b"));
			action.getWait().until(
					ExpectedConditions.elementToBeClickable(By
							.cssSelector("#tw_signin > button > img")));

			String twOAuthRequest = TestCase.getWebDriver()
					.findElement(By.cssSelector("#tw_signin"))
					.getAttribute("action");

			LOG.log(Level.INFO, "checking ouath status code");

			if (twOAuthRequest != "") {

				String urlString = TestCase.getBaseUrl() + "/signin/twitter";
				String statusCode = TestCase.getStatusCode(urlString);

				boolean ret = statusCode.equals("200");

				if (ret) {
					LOG.log(Level.INFO, "test ok");
				} else {
					LOG.log(Level.INFO, "test failed");
				}

				assertTrue(ret);

			} else {
				LOG.log(Level.INFO,
						"test failed. No such oauth request url to check");
				assertTrue(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testSignUpTwitterSocial finished");
		}

	}

	/**
	 * Check if user can register using his facebook social by accessing the
	 * image link in the 'Sign up' page.
	 */
	@Test
	public void testSignUpFacebook() {

		try {

			LOG.info("testSignUpFacebookSocial...");

			ExpectedActions action = new ExpectedActions();

			LOG.log(Level.INFO, "dont remember login page");

			action.click(By
					.cssSelector("#dontRememberLogin > a:nth-child(1) > b"));
			action.getWait().until(
					ExpectedConditions.elementToBeClickable(By
							.cssSelector("#fb_signin > button > img")));

			String fbOAuthRequest = TestCase.getWebDriver()
					.findElement(By.cssSelector("#fb_signin"))
					.getAttribute("action");

			LOG.log(Level.INFO, "checking ouath status code");

			if (fbOAuthRequest != "") {

				String urlString = TestCase.getBaseUrl() + "/signin/facebook";
				String statusCode = TestCase.getStatusCode(urlString);

				boolean ret = statusCode.equals("200");

				if (ret) {
					LOG.log(Level.INFO, "test ok");
				} else {
					LOG.log(Level.INFO, "test failed");
				}

				assertTrue(ret);

			} else {
				LOG.log(Level.INFO,
						"test failed. No such oauth request url to check");
				assertTrue(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testSignUpFacebookSocial finished");
		}

	}

	/**
	 * Check if user can\'t insert a super (> 100 characters) string.
	 */
	@Test
	public void testSignUpMaxLength() {

		try {

			LOG.info("testSignUpMaxLength...");

			ExpectedActions action = TestCase.getExpectedActions();

			// accessing sign up page
			action.click(By
					.cssSelector("#dontRememberLogin > a:nth-child(1) > b"));

			LOG.log(Level.INFO, "Loading Sign Up page");

			action.sendKeys(
					By.id("name"),
					"1729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734");
			action.sendKeys(
					By.id("email"),
					"1729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734");
			action.sendKeys(
					By.id("phone"),
					"1729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734");
			action.sendKeys(
					By.id("address"),
					"1729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734");

			LOG.log(Level.INFO, "Inserting new big data into register form");

			action.click(By.cssSelector("#settingsSubmitChanges"));

			Thread.sleep(2000); // waits for modal

			String modalHeader = TestCase
					.getWebDriver()
					.findElement(
							By.cssSelector("#loginFailPanel > div > div > div.modal-header > h4"))
					.getText();
			String modalBody = TestCase
					.getWebDriver()
					.findElement(
							By.cssSelector("#loginFailPanel > div > div > div.modal-body > p"))
					.getText();

			boolean ret1 = modalHeader.equals("Sign up error");
			boolean ret2 = modalBody
					.equals("Has not been able to make sign up due to errors in the fields");
			boolean ret3 = "Invalid email".equals(TestCase
					.getTextSelector("#email\\.errors"));

			boolean ret = ret1 && ret2 && ret3;

			if (ret) {
				LOG.log(Level.INFO, "test ok");
			} else {
				LOG.log(Level.INFO, "test failed");
			}

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testSignUpMaxLength finished");
		}

	}

	/**
	 * Check if user can reset his password by using the corresponding form in
	 * "Don't remember password" page.
	 */
	@Test
	public void testResetPassword() {

		boolean containsException = true;

		try {

			LOG.info("testResetPassword...");

			ExpectedActions action = TestCase.getExpectedActions();
			String email = TestCase.getSeleniumUserEmail();

			LOG.log(Level.INFO, "Loading \'don\'t remember password\' page");
			action.click(By.cssSelector("#dontRememberLogin > a:nth-child(3)"));

			LOG.log(Level.INFO, "introducing email to reset: " + email);
			action.sendKeys(By.cssSelector("#email"), email);

			LOG.log(Level.INFO, "submitting form");
			action.click(By.cssSelector("#submit"));

			Thread.sleep(2000);

			containsException = TestCase
					.getWebDriver()
					.findElement(
							By.cssSelector("#pagesContent > p:nth-child(1) > code"))
					.getText().toLowerCase()
					.contains("exception".toLowerCase());

			if (!containsException)
				LOG.log(Level.INFO, "test ok");
			else
				LOG.log(Level.INFO,
						"test failed. An exception has been received when trying to reset an account password");

			assertFalse(containsException);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testResetPassword finished");
		}

	}

	/**
	 * Check if user is correctly redirect to IDEAS when he connects his user
	 * with github social.
	 */
	@Test
	public void testConnectGithubOAuthResponse() {

		boolean ret = true;

		try {

			LOG.info("testConnectGithubOAuthResponse...");

			TestCase.login();
			IdeasAppActions.goSocialPage();

			ExpectedActions action = TestCase.getExpectedActions();
			action.click(By.cssSelector("#connect-github"));
			Thread.sleep(500);
			action.click(By.cssSelector("#pagesContent > form > p > button"));

			Thread.sleep(2000);

			// login to github
			String git_user = TestCase.getSeleniumGithubUser();
			String git_pass = TestCase.getSeleniumGithubPasswd();

			if (git_user != null && git_pass != null) {

				action.sendKeys(By.cssSelector("#login_field"), git_user);
				action.sendKeys(By.cssSelector("#password"), git_pass);
				action.click(By
						.cssSelector("#login > form > div.auth-form-body > input.btn"));

				Thread.sleep(2000);

				boolean exemplarConnect = TestCase.getCurrentUrl().contains(
						"exemplar.us.es");

				ret = exemplarConnect;

			} else {
				LOG.info("The test will fail because Github account data in \'selenium.properties\' are not right\nGithub user \'"
						+ git_user + "\' and password \'" + git_pass + "\'");
			}

			if (!ret)
				LOG.info("test ok");
			else
				LOG.info("test failed");

			assertFalse(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testConnectGithubOAuthResponse finished");
		}

	}

	/**
	 * Check if user receives a correctly response from Google OAuth request.
	 * This test has been made because Google requires to specify which base url
	 * is th OAuth service responding to. This way, if user uses a different
	 * base url, for example distinct to "/IDEAS/", this tests will fail.
	 */
	@Test
	public void testConnectGoogleOAuthResponse() {

		boolean ret = false;

		try {
			LOG.info("testConnectGoogleOAuthResponse...");

			TestCase.login();
			IdeasAppActions.goSocialPage();

			ExpectedActions action = TestCase.getExpectedActions();
			action.click(By.cssSelector("#connect-google"));
			Thread.sleep(500);
			action.click(By.cssSelector("#pagesContent > form > p > button"));

			Thread.sleep(2000);

			String go_user = TestCase.getSeleniumGoogleUser();
			String go_pass = TestCase.getSeleniumGooglePasswd();

			if (go_user != null && go_pass != null) {
				ret = IdeasAppActions
						.setUpFormDataGoogleLogin(go_user, go_pass);
			} else {
				LOG.info("The test will fail because Google account data in \'selenium.properties\' are not right\nGoogle user \'"
						+ go_user + "\' and password \'" + go_pass + "\'");
			}

			if (ret)
				LOG.info("test ok");
			else
				LOG.info("test failed");

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testConnectGoogleOAuthResponse finished");
		}

	}

	/**
	 * Check if user obtains a correct response when trying to connect his
	 * account with twitter.
	 */
	@Test
	public void testConnectTwitterOAuthResponse() {

		boolean ret = false;

		try {
			LOG.info("testConnectTwitter...");

			TestCase.login();
			IdeasAppActions.goSocialPage();

			ExpectedActions action = TestCase.getExpectedActions();
			action.click(By.cssSelector("#connect-twitter"));

			Thread.sleep(2000);

			if (!TestCase.getCurrentUrl().contains("connect/twitter")) {
				action.click(By
						.cssSelector("#pagesContent > form > p > button"));

				Thread.sleep(2000);

				String tw_user = TestCase.getSeleniumTwitterUser();
				String tw_pass = TestCase.getSeleniumTwitterPasswd();

				if (tw_user != null && tw_pass != null) {

					action.sendKeys(By.cssSelector("#username_or_email"),
							tw_user);
					action.sendKeys(By.cssSelector("#password"), tw_pass);
					action.click(By.cssSelector("#allow"));

					Thread.sleep(1000);

					String msgInvalidTokenRequestError = TestCase
							.getWebDriver()
							.findElement(By.cssSelector("#bd > div > p"))
							.getText();

					boolean ret_en = msgInvalidTokenRequestError
							.contains("The request token for this page is invalid");
					boolean ret_es = msgInvalidTokenRequestError
							.contains("El token de solicitud para esta página no es válido");

					ret = !ret_en || !ret_es;

				} else {
					LOG.info("The test will fail because Twitter account data in \'selenium.properties\' are not right\nTwitter user \'"
							+ tw_user + "\' and password \'" + tw_pass + "\'");
				}

			} else {
				LOG.info("Twitter user already successfully logged");
				ret = true;
			}

			if (ret)
				LOG.info("test ok");
			else
				LOG.info("test failed");

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testConnectTwitter finished");
		}

	}

	/**
	 * Check if user obtains a correct response when trying to connect his
	 * account with facebook.
	 */
	@Test
	public void testConnectFacebookOAuthResponse() {

		boolean ret = true;

		try {

			LOG.info("testConnectFacebookOAuthResponse...");

			TestCase.login();
			IdeasAppActions.goSocialPage();

			ExpectedActions action = TestCase.getExpectedActions();
			action.click(By.cssSelector("#connect-facebook"));
			Thread.sleep(500);
			action.click(By.cssSelector("#pagesContent > form > p > button"));

			Thread.sleep(2000);

			if (!TestCase.getCurrentUrl().contains("security/login")
					&& IdeasAppActions.isAnyUserLogged()) {

				String errorMsg = TestCase.getWebDriver()
						.findElement(By.cssSelector("#u_0_0 > div > div"))
						.getText();

				boolean ret_en = errorMsg
						.contains("Given URL is not allowed by the Application configuration");

				boolean ret_es = errorMsg
						.contains("La configuración de la aplicación no permite la URL proporcionada");

				ret = ret_en || ret_es;

				if (!ret)
					LOG.info("test ok");
				else
					LOG.info("test failed");

			}

			assertFalse(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testConnectFacebookOAuthResponse finished");
		}

	}

	/**
	 * Check if user obtains a correct response when trying to connect his
	 * account with linkedin.
	 */
	@Test
	public void testConnectLinkedInOAuthResponse() {

		boolean ret = true;

		try {

			LOG.info("testConnectLinkedIn...");

			TestCase.login();
			IdeasAppActions.goSocialPage();

			ExpectedActions action = TestCase.getExpectedActions();
			action.click(By.cssSelector("#connect-linkedin"));
			Thread.sleep(500);
			action.click(By.cssSelector("#pagesContent > form > p > button"));

			Thread.sleep(2000);

			if (!TestCase.getCurrentUrl().contains("security/login")
					&& IdeasAppActions.isAnyUserLogged()) {

				String errorMsg = TestCase
						.getWebDriver()
						.findElement(
								By.cssSelector("#body > div > div.alert.error"))
						.getText();

				ret = errorMsg.contains("invalid redirect_uri");

				if (!ret)
					LOG.info("test ok");
				else
					LOG.info("test failed");

			}

			assertFalse(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testConnectLinkedIn finished");
		}

	}

	/**
	 * Cuando un usuario está conectado con Twitter, selecciona
	 * "Connected with twitter" en configuración social y después pulsa en
	 * "Go to App". Este último botón lleva al usuario a un error 404.
	 */
	@Test
	public void testUserAlreadyConnectedToTwitter() {

		boolean ret = false;

		try {

			LOG.info("testUserAlreadyConnectedToTwitter...");

			TestCase.login();
			IdeasAppActions.goSocialPage();

			String btnText = TestCase.getWebDriver()
					.findElement(By.cssSelector("#connect-twitter")).getText();

			if (btnText.equals("Connected with twitter")) {

				ExpectedActions action = TestCase.getExpectedActions();
				action.click(By.cssSelector("#connect-twitter"));
				action.click(By.cssSelector("#goToApp"));

				Thread.sleep(1000);

				String statusCode = TestCase.getStatusCode(TestCase
						.getWebDriver().getCurrentUrl());

				ret = statusCode.equals("200");

			}

			if (ret)
				LOG.info("test ok");
			else
				LOG.info("test failed");

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.info("testUserAlreadyConnectedToTwitter finished");
		}

	}

	/**
	 * Check if user can change his username.
	 */
	@Test
	public void testRenameUsername() {

		boolean ret = false;

		try {

			LOG.log(Level.INFO, "testRenameUsername...");

			TestCase.login();
			IdeasAppActions.goUserAccountPage();

			Thread.sleep(2000);

			ExpectedActions action = TestCase.getExpectedActions();
			String selectorLogInfo = "#accountTab";
			String username = "";

			// open login information tab
			action.click(By.cssSelector(selectorLogInfo));

			Thread.sleep(1000); // animation

			// getting actual user name
			username += TestCase.getWebDriver()
					.findElement(By.cssSelector("#userAccount\\2e username"))
					.getText();

			action.sendKeys(By.cssSelector("#userAccount\2e username"), "2"); // inserts
																				// "2"
																				// at
																				// the
																				// and
																				// of
																				// username

			LOG.info("New user name wrote into \'Login information\' form");

			// just modify to enable "Save changes" button.
			// action.click(By.cssSelector("#pagesContent"));

			action.click(By.cssSelector("#settingsSubmitChanges"));
			LOG.log(Level.INFO, "Submitting new user name modification");

			Thread.sleep(1000);

			action.click(By.cssSelector(selectorLogInfo));

			Thread.sleep(500); // animation

			String usernameMod = TestCase.getWebDriver()
					.findElement(By.cssSelector("#userAccount\\.username"))
					.getText();

			LOG.info("Verifying if user name has been changed");
			ret = !username.equals(usernameMod);

			if (ret)
				LOG.log(Level.INFO,
						"test ok. User name was successfully changed");
			else
				LOG.log(Level.INFO,
						"test failed. User name could\'nt be changed");

			assertTrue(ret);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "testRenameUsername finished");
		}

	}

	/**
	 * Test if user can access to '/app/editor' by logging with user and
	 * password data form.
	 */
	@Test
	public void testLogin() {

		boolean ret = false;

		try {

			LOG.log(Level.INFO, "testLogin test method initializing...");

			TestCase.logout();
			IdeasAppActions.goHomePage();

			String user = TestCase.getSeleniumAutotesterUser();
			String pass = TestCase.getSeleniumAutotesterPassword();

			TestCase.loginWithParams(user, pass);

			System.out.println(TestCase.getCurrentPageStatusCode());

			if (TestCase.getCurrentUrl().contains("app/editor")) {
				ret = true;
			}

			if (ret)
				LOG.info("test ok");
			else
				LOG.info("test failed");

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "testLogin test method finished");
		}

	}

	/**
	 * Checks if user can login into IDEAS using the twitter account defined in
	 * 'selenium.properties' file.
	 */
	@Test
	public void testLoginTwitter() {

		boolean ret = false;

		try {

			LOG.log(Level.INFO, "testLoginTwitter test method initializing...");

			String tw_user = TestCase.getSeleniumTwitterUser();
			String tw_pass = TestCase.getSeleniumTwitterPasswd();

			if (IdeasAppActions.setUpFormDataTwitterLogin(tw_user, tw_pass)) {

				boolean ret1 = TestCase.getCurrentUrl().contains("app/editor");
				// boolean ret2 = "200".equals(TestCase
				// .getCurrentPageStatusCode());

				ret = ret1;

			} else {
				LOG.info("Test couldn\'t set up the twitter login form");
			}

			assertTrue(ret);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "testLoginTwitter test method finished");
		}

	}

	/**
	 * Check which tab is activated by default we user go to "User Account"
	 * menu. The test fails when
	 */
	@Test
	public void testDefaultUserAccountTab() {

		boolean isLoginInformationTabActivated = true;

		try {

			LOG.log(Level.INFO, "testLoginInformationInsteadUserAccount...");

			Thread.sleep(2000);
			TestCase.login();

			if (IdeasAppActions.goUserAccountPage()) {

				Thread.sleep(2000);

				isLoginInformationTabActivated = TestCase
						.getWebDriver()
						.findElement(By.cssSelector("#settingTabs > li.active"))
						.getText().toLowerCase()
						.contains("Login information".toLowerCase());

				if (isLoginInformationTabActivated)
					LOG.log(Level.INFO, "test failed");
				else
					LOG.log(Level.INFO, "test ok");

				TestCase.logout();

			}

			assertFalse(isLoginInformationTabActivated);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO,
					"testLoginInformationInsteadUserAccount finished");
		}

	}

	/**
	 * Check if "Go to Login" button appears when register form has a fail.
	 */
	@Test
	public void testBadRegisterFormReturnToLoginButton() {

		try {

			LOG.log(Level.INFO, "testBadRegisterFormReturnToLoginButton...");

			boolean isRedirectToLogin = false;

			IdeasAppActions.goSignUpPage();

			ExpectedActions action = TestCase.getExpectedActions();

			action.click(By.cssSelector("#address"));
			action.sendKeys(By.cssSelector("#address"), "...");
			action.click(By.cssSelector("#pagesContent"));

			Thread.sleep(500); // btn animation

			action.click(By.cssSelector("#settingsSubmitChanges"));

			Thread.sleep(2000);

			isRedirectToLogin = TestCase
					.getWebDriver()
					.findElement(
							By.cssSelector("#loginFailPanel > div > div > div.modal-footer > button"))
					.getText().contains("Return to Login");

			if (isRedirectToLogin)
				LOG.log(Level.INFO, "test failed");
			else
				LOG.log(Level.INFO, "test ok");

			assertFalse(isRedirectToLogin);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO,
					"testBadRegisterFormReturnToLoginButton finished");
		}

	}

	/**
	 * Checks if register social images are correctly loaded.
	 */
	@Test
	public void testSignUpImages() {

		try {

			LOG.log(Level.INFO, "testSignUpImages...");

			boolean ret = false;

			IdeasAppActions.goSignUpPage();

			String srcTwImg = TestCase.getWebDriver()
					.findElement(By.cssSelector("#tw_signin > button > img"))
					.getAttribute("src");
			String srcFbImg = TestCase.getWebDriver()
					.findElement(By.cssSelector("#fb_signin > button > img"))
					.getAttribute("src");

			boolean ret1 = TestCase.getStatusCode(srcTwImg).equals("200");
			boolean ret2 = TestCase.getStatusCode(srcFbImg).equals("200");

			ret = ret1 && ret2;

			if (ret)
				LOG.log(Level.INFO, "test ok");
			else
				LOG.log(Level.INFO, "test failed");

			assertTrue(ret);

		} catch (NoSuchElementException e) {
			e.printStackTrace();
			LOG.log(Level.INFO,
					"It seems that a test element hasn\'t been found.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "testSignUpImages finished");
		}

	}

}
