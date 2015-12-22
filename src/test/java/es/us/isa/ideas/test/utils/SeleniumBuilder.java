package es.us.isa.ideas.test.utils;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class SeleniumBuilder {

	private WebDriver driver = null;
	private ExpectedActions expActions = null;
	private JavascriptExecutor js = null;
	private String baseURL = null;
	private String seleniumFileName = "";

	private static SeleniumBuilder INSTANCE = null;
	private static final Logger LOG = Logger.getLogger(SeleniumBuilder.class.getName());

	@BeforeClass
	public synchronized static void setUp() {

		LOG.log(Level.INFO, "opening WebDriver");
		createInstance(); // creating Singleton

	}

	@AfterClass
	public static void tearDown() throws InterruptedException {

		LOG.log(Level.INFO, "logging out user " + TestCase.getSeleniumAutotesterUser());

		TestCase.logout();

		LOG.log(Level.INFO, "quitting browser");

		getWebDriver().quit();

		getInstance().driver = null;

	}

	/**
	 * Creates a single instance of selenium webdriver.
	 */
	private synchronized static void createInstance() {
		if (INSTANCE == null) {

			INSTANCE = new SeleniumBuilder();

			INSTANCE.seleniumFileName = askSeleniumFileName();

//			INSTANCE.driver = new FirefoxDriver();
            INSTANCE.driver = new ChromeDriver();

			INSTANCE.driver.manage().timeouts().pageLoadTimeout(2, TimeUnit.MINUTES);
			INSTANCE.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			INSTANCE.driver.manage().window().maximize();

			INSTANCE.expActions = new ExpectedActions();
			INSTANCE.js = (JavascriptExecutor) getInstance().driver;

			Properties prop = TestCase.getSeleniumProperties();
			INSTANCE.baseURL = prop.getProperty("test.server.baseURL") + ":" + prop.getProperty("test.server.port")
					+ "/" + prop.getProperty("test.app.name");

		}
	}

	/**
	 * Initializes the baseUrl attribute of class for testing requests.
	 * 
	 * @return
	 */
	public static String getBaseUrl() {
		return getInstance().baseURL;
	}

	/**
	 * Returns the only webdriver instance.
	 * 
	 * @return Selenium webdriver
	 */
	public static SeleniumBuilder getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	public static WebDriver getWebDriver() {
		return getInstance().driver;
	}

	public static ExpectedActions getExpectedActions() {
		return getInstance().expActions;
	}

	public static JavascriptExecutor getJs() {
		return getInstance().js;
	}

	public static WebDriverWait getWait() {
		return SeleniumBuilder.getExpectedActions().getWait();
	}

	public String getSeleniumFileName() {
		return this.seleniumFileName;
	}

	public static String askSeleniumFileName() {

		String ret = "";

		if (INSTANCE == null || (INSTANCE != null && INSTANCE.getSeleniumFileName() == "")) {
            
            Properties prop = TestCase.getSeleniumGeneralProperties();

			if (prop != null && 
                    TestCase.getSeleniumGeneralProperties().getProperty("test.environment").equals("REMOTE")) { // Local
                ret += SeleniumTargetType.REMOTE_FILE.toString();
            } else { // Remote
                ret += SeleniumTargetType.LOCAL_FILE.toString();
            }
			
			System.out.println("Using " + ret);
			
		}

		return ret;

	}

}
