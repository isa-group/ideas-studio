package es.us.isa.ideas.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 * 
 * This class contains some utilities to run integration tests.
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class TestCase {

    // CSS Selectors
    protected static final String SELECTOR_ADD_BUTTON = ".addWorkspace";
    protected static final String SELECTOR_MODAL_INPUT = "#modalCreationField > input";
    protected static final String SELECTOR_WS_LIST = "#appMainContentBlocker";
    protected static final String SELECTOR_WS_CURRENT = "#editorSidePanelHeaderWorkspaceInfo";
    protected static final String SELECTOR_WS_TOGGLER = "#menuToggler";
    protected static final String SELECTOR_WS_MANAGER = "#appLeftMenu";
    protected static final String SELECTOR_DYNATREE = "#projectsTree";
    protected static final String SELECTOR_PROJECT = "#projectsTree > ul > li:nth-child(1) > span > a";
    protected static final String SELECTOR_EDIT_NODE_INPUT = "input#editNode";
    protected static final String SELECTOR_TAB_ACTIVE = "div#editorMainPanel ul#editorTabs > li.active";
    
    protected static final String SELECTOR_NEW_WS_MODAL_INPUT_NAME = "#modalCreationField > input";
    protected static final String SELECTOR_NEW_WS_MODAL_INPUT_DESCRIPTION = "#descriptionInput > textarea";
    protected static final String SELECTOR_NEW_WS_MODAL_INPUT_TAGS = "#tagsInput > textarea";
    
    protected static final String SELECTOR_MODAL_CONTINUE = "#appGenericModal > div > div > div.modal-footer > a.btn.btn-primary.continue";
    
    protected static final String SELECTOR_WORKSPACE_FORM_INPUT_NAME = "#modalCreationField > input";
    protected static final String SELECTOR_WORKSPACE_FORM_INPUT_DESCRIPTION = "#descriptionInput > textarea";
     
    protected static final String SELECTOR_DASHBOARD_WORKSPACE_CARD_EDIT_BUTTON = "#editWS";
    protected static final String SELECTOR_DASHBOARD_WORKSPACE_CARD_PUBLISH_BUTTON = "#publishWS";
    protected static final String SELECTOR_DASHBOARD_WORKSPACE_CARD_DELETE_BUTTON = "#deleteWS";
    protected static final String SELECTOR_DASHBOARD_DEMO_CARD_UPDATE_BUTTON = "#updateDemo";
    protected static final String SELECTOR_DASHBOARD_DEMO_CARD_DELETE_BUTTON = "#deleteDemo";
    protected static final String SELECTOR_DASHBOARD_DEMO_VIEW_BUTTON = "#viewDemo";
    protected static final String SELECTOR_DASHBOARD_PUBLIC_DEMO_CLONE_BUTTON = "#cloneDemo";
    
    private static final String PROPERTY_APPLICATION_FILE_NAME = "application.properties";
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());
    
    public static Properties getApplicationProperties() {
        Properties prop = null;
        
        try {
            prop = new Properties();
            InputStream input = TestCase.class.getResourceAsStream("/" + PROPERTY_APPLICATION_FILE_NAME);
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return prop;
    }
    
    public static String getSeleniumPropFile() {
        return SeleniumBuilder.getInstance().getSeleniumFileName();
    }

    /**
     * Obtain the absolute path from a relative path.
     *
     * @param path
     * @return the absolute path string
     */
    public static String getUrlAbsolute(String path) {
        String url = "";
        
        if (path.startsWith("/")) {
            url += SeleniumBuilder.getBaseUrl() + path;
        } else {
            url += SeleniumBuilder.getBaseUrl() + "/" + path;
        }
        
        return url;
    }

    /**
     * Log to IDEAS using user and password properties defined in
     * 'selenium.properties' file.
     *
     * @return true if everything went well and a user has been logged
     * @throws InterruptedException
     */
    public static boolean login() throws InterruptedException {
        
        boolean ret = false;
        
        if (!IdeasStudioActions.isAnyUserLogged() && IdeasStudioActions.goLoginPage()) {
            
            waitForVisibleSelector("#username");
            
            SeleniumBuilder.getExpectedActions().sendKeys(By.id("username"), getAutotesterUser());
            SeleniumBuilder.getExpectedActions().sendKeys(By.id("password"), getAutotesterPassword());
            SeleniumBuilder.getExpectedActions().click(By.id("loginButton"));
            
            Thread.sleep(2000);
            
            ret = true; // TODO: make sure user really logged by calling
            // isAnyUserLogged again

        }
        
        return ret;
        
    }

    /**
     * Logs into IDEAS application using a username and password.
     *
     * @param username
     * @param password
     * @return true if user was redirect to editor page 'app/editor'
     * @throws InterruptedException
     */
    public static boolean loginWithParams(String username, String password) throws InterruptedException {
        
        boolean ret = false;
        
        TestCase.logout();
        
        if (IdeasStudioActions.goLoginPage()) {
            
            waitForVisibleSelector("#username");
            
            SeleniumBuilder.getExpectedActions().sendKeys(By.id("username"), username);
            SeleniumBuilder.getExpectedActions().sendKeys(By.id("password"), password);
            SeleniumBuilder.getExpectedActions().click(By.id("loginButton"));
            
            Thread.sleep(2000);
            
            ret = getCurrentUrl().contains("app/editor");
            
        }
        
        return ret;
        
    }

    /**
     * Logout user by calling 'goLogoutPage()'.
     *
     * @return true if everything gone fine and there is no user logged.
     * @throws InterruptedException
     */
    public static boolean logout() throws InterruptedException {
        
        boolean ret = IdeasStudioActions.goLogoutPage();
        // TODO: make sure user really logout by calling isAnyUserLogger()
        Thread.sleep(2000);
        
        return ret;
        
    }

    /**
     * Get current selenium local or remote properties.
     *
     * @return
     */
    public static Properties getSeleniumProperties() {
        
        Properties prop = new Properties();
        InputStream input = null;
        
        try {
            String propFile = getSeleniumPropFile();
            input = TestCase.class.getResourceAsStream("/" + propFile);
            if (input != null) {
                prop.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return prop;
    }
    
    public static Properties getSeleniumGeneralProperties() {
        
        Properties prop = new Properties();
        InputStream input = null;
        
        try {
            input = TestCase.class.getResourceAsStream("/selenium-general.properties");
            if (input != null) {
                prop.load(input);
            }
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
        
        return prop;
    }
    
    public static String getAutotesterUser() {
        return getSeleniumGeneralProperties().getProperty("autotester.user");
    }
    
    public static String getAutotesterPassword() {
        return getSeleniumGeneralProperties().getProperty("autotester.pass");
    }
    
    public static void setAutotesterPassword(String pass) {
        try {
            java.net.URL resource = TestCase.class.getResource("/selenium-general.properties");
            final File propsFile = new File(resource.toURI());
            
            FileInputStream fileName = new FileInputStream(propsFile);
            Properties props = getSeleniumGeneralProperties();
            props.load(fileName);
            props.setProperty("autotester.pass", pass);
            fileName.close();
            
            FileOutputStream outFileName = new FileOutputStream(propsFile);
            props.store(outFileName, "");
            outFileName.close();
//            getSeleniumGeneralProperties().setProperty("autotester.pass", pass);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }
    }
    
    public static String getAutotesterName() {
        return getSeleniumGeneralProperties().getProperty("autotester.name");
    }
    
    public static String getAutotesterEmail() {
        return getSeleniumGeneralProperties().getProperty("autotester.email");
    }
    
    public static String getAutotesterEmailPassword() {
        return getSeleniumGeneralProperties().getProperty("autotester.email.pass");
    }
    
    public static String getAutotesterPhone() {
        return getSeleniumGeneralProperties().getProperty("autotester.phone");
    }
    
    public static String getAutotesterAddress() {
        return getSeleniumGeneralProperties().getProperty("autotester.address");
    }
    
    public static String getSeleniumTwitterUser() {
        return getSeleniumGeneralProperties().getProperty("test.tw.user");
    }
    
    public static String getSeleniumTwitterPassword() {
        return getSeleniumGeneralProperties().getProperty("test.tw.pass");
    }
    
    public static String getSeleniumGoogleUser() {
        return getSeleniumGeneralProperties().getProperty("test.go.user");
    }
    
    public static String getSeleniumGooglePassword() {
        return getSeleniumGeneralProperties().getProperty("test.go.pass");
    }
    
    public static String getSeleniumGithubUser() {
        return getSeleniumGeneralProperties().getProperty("test.gi.user");
    }
    
    public static String getSeleniumGithubPassword() {
        return getSeleniumGeneralProperties().getProperty("test.gi.pass");
    }
    
    public static String getCurrentUrl() {
        return SeleniumBuilder.getWebDriver().getCurrentUrl();
    }
    
    public static String getStatusCode(String url) throws InterruptedException {
        
        String ret = "";
        
        if (loadSeleniumJQuery()) {
            
            Object statusCode = (Object) getJs().executeScript("var res=null;" + "jQuery.ajax({" + "url: '" + url + "',"
                    + "data: {}," + "async: false," + "complete: function(xhr, statusText){" + "res = xhr.status;" + "}"
                    + "});" + "return res;");
            
            if (statusCode != null) {
                ret += statusCode.toString();
            }
            
        }
        
        return ret;
        
    }
    
    public static String getCurrentPageStatusCode() throws IOException, InterruptedException {
        return getStatusCode(getCurrentUrl());
    }

    /**
     * Loads /js/vendor/jquery.js file into selenium webdriver.
     *
     * @return true if jquery was successfully loaded into webdriver.
     * @throws InterruptedException
     */
    public static boolean loadSeleniumJQuery() throws InterruptedException {
        
        boolean ret = false;

        // load jquery.js
        getJs().executeScript("var s=window.document.createElement('script');" + "s.src='"
                + getUrlAbsolute("js/vendor/jquery.js") + "';" + "window.document.head.appendChild(s);");
        
        Thread.sleep(150); // time necessary to reload DOM

        Object o = (Object) SeleniumBuilder.getJs().executeScript("$ ? true:false;");
        
        if (o != null) {
            ret = (Boolean) o;
            
            if (!ret) {
                LOG.severe("Couldn\'t load jquery.js into Selenium WebDriver");
            }
            
        }
        
        return ret;
        
    }
    
    public static String getSeleniumServerBaseURL() {
        String url = getSeleniumProperties().getProperty("test.server.baseURL");
        
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        
        return url;
    }
    
    public static String getSeleniumServerPort() {
        return getSeleniumProperties().getProperty("test.server.port");
    }
    
    public static String getSeleniumAppName() {
        return getSeleniumProperties().getProperty("test.app.name");
    }
    
    public static String getTextFromSelector(String cssSelector) {
        return SeleniumBuilder.getWebDriver().findElement(By.cssSelector(cssSelector)).getText();
    }
    
    public static String getInputValueSelector(String cssSelector) {
        return getWebDriver().findElement(By.cssSelector(cssSelector)).getAttribute("value");
    }
    
    public static String getBaseUrl() {
        return SeleniumBuilder.getBaseUrl();
    }
    
    public static WebDriver getWebDriver() {
        return SeleniumBuilder.getWebDriver();
    }
    
    public static ExpectedActions getExpectedActions() {
        return SeleniumBuilder.getExpectedActions();
    }
    
    public static JavascriptExecutor getJs() {
        return SeleniumBuilder.getJs();
    }
    
    public static ExpectedActions waitForVisibleSelector(String selector) {
        SeleniumBuilder.getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
        return getExpectedActions();
    }
    
    public static ExpectedActions waitForVisibleXpath(String xpath) {
        SeleniumBuilder.getWait().until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        return getExpectedActions();
    }
    
    public static boolean validatePropertyValues(String... propValues) {
        boolean ret = true;
        
        for (String value : propValues) {
            ret = ret && value != null && !"".equals(value);
            if (!ret) {
                break;
            }
        }
        
        return ret;
    }
    
    public static boolean isCurrentUrlContains(String s) throws InterruptedException {
        Thread.sleep(1000);
        return getCurrentUrl().contains(s);
    }
    
    public static void echoCommandApi(String msg) {
        
        try {
            TestCase.getJs().executeScript(
                    "" + "if (CommandApi.echo) {" + "CommandApi.echo('" + msg + "');" + "}");
            
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static int getSelectorLength(String selector) {
        
        Integer ret = 0;
        
        try {
            
            Object jsObj = TestCase.getJs().executeScript("return jQuery('" + selector + "').length;");
            Long content = 0L;
            if (jsObj != null) {
                content = (Long) jsObj;
                ret = content.intValue();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ret;
    }
    
    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }

    /**
     * This method uses jQuery to find the selector param.
     *
     * @param cssSelector
     * @return
     */
    public static boolean existDOMElement(String cssSelector) {
        boolean ret = false;
        try {
            Object obj = (Object) getJs().executeScript("return jQuery('" + cssSelector + "').length > 0;");
            if (obj != null) {
                ret = (Boolean) obj;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
        return ret;
    }
    
    public static void deleteCurrentGmailEmail() {
        SeleniumBuilder.getExpectedActions()
                .click(By.xpath("//*[@id=\":5\"]/div[2]/div[1]/div/div[2]/div[3]/div/div"));
    }
    
}
