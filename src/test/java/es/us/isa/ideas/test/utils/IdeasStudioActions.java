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
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class IdeasStudioActions {

    private static final Logger LOG = Logger.getLogger(IdeasStudioActions.class.getName());

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
    public static boolean registerUser(String name, String email, String phone, String address) {

        boolean ret = false;

        try {

            TestCase.logout();
            IdeasStudioActions.goSignUpPage();
            ExpectedActions action = TestCase.getExpectedActions();

            action.sendKeys(By.cssSelector("#name"), name);
            action.sendKeys(By.cssSelector("#email"), email);
            action.sendKeys(By.cssSelector("#phone"), phone);
            action.sendKeys(By.cssSelector("#address"), address);
            action.click(By.cssSelector("#settingsSubmitChanges"));

            Thread.sleep(1000); // modal animation

            String modalHeader = TestCase.getWebDriver()
                    .findElement(By.cssSelector("#loginOKPanel > div > div > div.modal-header > h4")).getText();

            String msgEmailAlreadyTaken = TestCase.getWebDriver().findElement(By.cssSelector("#statusPanel")).getText();

            boolean ret1 = "Account created successfully".equals(modalHeader);
            boolean ret2 = "The email address you entered is already in use".equals(msgEmailAlreadyTaken);

            if (!ret1) {
                LOG.log(Level.INFO,
                        "It appears that no successful modal window has been showed. Quitting register test.");
            }

            if (ret2) {
                LOG.log(Level.INFO, "{0}. Quitting register test", msgEmailAlreadyTaken);
            }

            ret = ret1 && !ret2;

        } catch (NoSuchElementException | InterruptedException e) {
            LOG.severe(e.getMessage());
        }

        return ret;

    }

    public static boolean setUpFormDataTwitterLogin(String tw_user, String tw_pass) {

        boolean ret = false;

        try {

            TestCase.logout();
            IdeasStudioActions.goLoginPage();

            ExpectedActions action = TestCase.getExpectedActions();

            action.click(By.cssSelector("#tw_signin > button"));
            action.sendKeys(By.cssSelector("#username_or_email"), tw_user);
            action.sendKeys(By.cssSelector("#password"), tw_pass);
            action.click(By.cssSelector("#allow"));

            Thread.sleep(2000);
            ret = TestCase.getCurrentUrl().contains("app/editor");

            if (!ret) {

                String selectorMsgHeader = "#pagesContent > h3:nth-child(2)";
                TestCase.waitForVisibleSelector(selectorMsgHeader);
                String msgHeader = TestCase.getWebDriver().findElement(By.cssSelector(selectorMsgHeader)).getText();

                boolean ret_connectedTwitter = msgHeader.toLowerCase().contains("connected to twitter");

                // ret = !ret_en || !ret_es || ret_connectedTwitter;
                ret = ret_connectedTwitter;

            }

        } catch (NoSuchElementException e) {
            LOG.info(
                    "Test may not be well performed because a element from DOM wasn\'t found. Please, contact realease manager to review test code execution.");
            LOG.severe(e.getMessage());
        } catch (Exception e) {
            ret = false;
            LOG.severe(e.getMessage());
        }

        return ret;

    }

    public static boolean setUpFormDataGoogleLogin(String go_user, String go_pass) {

        boolean ret = false;

        try {

            ExpectedActions action = TestCase.getExpectedActions();
            action.sendKeys(By.cssSelector("#Email"), go_user);
            action.click(By.cssSelector("#next"));
            action.sendKeys(By.cssSelector("#Passwd"), go_pass);
            action.click(By.cssSelector("#signIn"));

            Thread.sleep(1000);

            String msgStatusCode = TestCase.getWebDriver()
                    .findElement(By.cssSelector("#af-error-container > p:nth-child(2) > b")).getText();

            ret = !"400.".equals(msgStatusCode);

            if (!ret) {
                LOG.info("Test will fail because webdriver received a Google 400 error page");
            }

        } catch (NoSuchElementException e) {
            LOG.info(
                    "Test may not be well performed because a element from DOM wasn\'t found. Please, contact realease manager to review test code execution.");
            LOG.severe(e.getMessage());
        } catch (Exception e) {
            LOG.severe(e.getMessage());
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
     * @throws InterruptedException
     */
    public static boolean registerTwitterUser(String tw_user, String tw_pass) throws InterruptedException {

        boolean ret = false;

        try {

            if (IdeasStudioActions.setUpFormDataTwitterLogin(tw_user, tw_pass)) {

                Thread.sleep(2000);

                String currentUrl = TestCase.getWebDriver().getCurrentUrl();
                boolean ret1 = false;
                boolean ret2 = currentUrl.contains("app/editor");

                if (ret2) {
                    LOG.log(Level.INFO, "It seems ''{0}'' user was already registered to IDEAS. So this test will fail because it does not fulfill its purpose", tw_user);
                } else {
                    String msgConnectedToTwitter = TestCase.getWebDriver()
                            .findElement(By.cssSelector("#pagesContent > h3:nth-child(2)")).getText();

                    ret1 = "Connected to Twitter".equals(msgConnectedToTwitter);
                }
                ret = ret1 || ret2;

            }

        } catch (NoSuchElementException e) {
            LOG.severe(e.getMessage());
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
    public static boolean checkGoogleRegisterOAuth(String go_user, String go_pass) {

        boolean ret = false;

        try {

            ExpectedActions action = TestCase.getExpectedActions();

            LOG.log(Level.INFO, "Logging google oauth with ''{0}'' and ''{1}''", new Object[]{go_user, go_pass});

            action.click(By.cssSelector("#go_signin > button"));

            action.sendKeys(By.cssSelector("#Email"), go_user);
            action.click(By.cssSelector("#next"));

            action.sendKeys(By.cssSelector("#Passwd"), go_pass);
            action.click(By.cssSelector("#signIn"));

            Thread.sleep(1000);

            String msgStatusCode = TestCase.getWebDriver()
                    .findElement(By.cssSelector("#af-error-container > p:nth-child(2) > b")).getText();

            ret = !"400.".equals(msgStatusCode);

        } catch (NoSuchElementException e) {
            LOG.severe(e.getMessage());
            LOG.log(Level.INFO,
                    "Test wasn\'t well performed. Please, contact realease manager to review test code execution.");
            ret = false;
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
            ret = false;
        }

        return ret;

    }

    /**
     * Selecciona un workspace dado su nombre.
     *
     * @precondition Es necesario que el usuario esté logueado y que el WS
     * exista.
     * @param workspaceName
     * @throws InterruptedException
     */
    public static void selectWorkspace(String workspaceName) throws InterruptedException {

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

        return TestCase.getWebDriver().getCurrentUrl().toLowerCase().contains(path.toLowerCase());

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

            IdeasStudioActions.goEditorPage();

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

            TestCase.getWebDriver().get(TestCase.getBaseUrl() + relativePath);
            try {

                // TODO: better solution waiting for a URL modification
                Thread.sleep(1000); // changing url

                boolean ret1 = TestCase.getCurrentUrl().contains(relativePath);
                ret = ret1;

            } catch (InterruptedException e) {
                LOG.severe(e.getMessage());
                ret = false;
            }

        } else {
            ret = true;
        }

        return ret;

    }

    /**
     * Go to IDEAS user sign up.
     *
     * @return
     */
    public static boolean goSignUpPage() {
        LOG.info("Loading sign up page...");
        return IdeasStudioActions.goRelativePath("/settings/user");
    }

    /**
     * Go to IDEAS home webpage.
     *
     * @return
     */
    public static boolean goHomePage() {
        LOG.info("Loading home page...");
        return IdeasStudioActions.goRelativePath("/");
    }

    /**
     * Go to IDEAS editor webpage.
     *
     * @return
     */
    public static boolean goEditorPage() {
        LOG.info("Loading editor page...");
        return IdeasStudioActions.goRelativePath("/app/editor");
    }
    
    /**
     * Go to IDEAS workspace manager.
     */
    public static boolean goWSMPage() {
        LOG.info("Loading dashboard page...");
        return IdeasStudioActions.goRelativePath("/app/wsm");
    }   
    /**
     * Go to IDEAS help webpage.
     */
    public static boolean goHelpPage() {
        LOG.info("Loading help page...");
        return IdeasStudioActions.goRelativePath("/app/help");
    }

    /**
     * Go to IDEAS login webpage.
     *
     * @return
     */
    public static boolean goLoginPage() {
        LOG.info("Loading login page...");
        return IdeasStudioActions.goRelativePath("/security/login");
    }

    /**
     * Go to IDEAS logout webpage.
     *
     * @return
     */
    public static boolean goLogoutPage() {
        LOG.info("Loading logout page...");
        return IdeasStudioActions.goRelativePath("/j_spring_security_logout");
    }

    /**
     * Go to IDEAS user social tab that can be found in "User Account" page.
     *
     * @return true if webdriver could access "Social" tab
     */
    public static boolean goSocialPage() {
        LOG.info("Loading social page...");
        return IdeasStudioActions.goRelativePath("/settings/user#social");
    }

    /**
     * Ejecuta el comando echo de la línea de comandos de ideas-studio indicando
     * el mensaje a escribirse en la consola.
     *
     * @param driver
     * @param msg
     * @throws InterruptedException
     */
    public static void echoCommand(WebDriver driver, String msg) throws InterruptedException {

        goEditorPage();

        ((JavascriptExecutor) driver)
                .executeScript("" + "if (CommandApi.echo) {" + "CommandApi.echo('" + msg + "');" + "}");

    }

    /**
     * Ejecuta una lista de comandos pasados por parámetro. Cada ejecución
     * difiere en 500 milisengundos.
     *
     * @param cmds
     * @return
     */
    public static boolean executeCommands(String... cmds) {

        boolean ret = false;
        String inputSelector = "input.gcli-in-input";

        try {

            if (goEditorPage()) {
                ret = true;
                for (String cmd : cmds) {

                    TestCase.getJs().executeScript("jQuery('" + inputSelector + "').focus();");    // avoid chromedriver not clickable element
                    Thread.sleep(2000);
                    TestCase.getExpectedActions().sendKeys(By.cssSelector(inputSelector), cmd);
                    Thread.sleep(1000);
                    TestCase.getExpectedActions().sendKeys(By.cssSelector(inputSelector), Keys.RETURN);

                }
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(IdeasStudioActions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;

    }

    /**
     * Obtiene el resultado (en booleano) de los tests aplicado a partir de
     * TestModule dado false.
     *
     * @param driver
     * @return
     * @throws InterruptedException
     */
    public static boolean checkTestModuleOkResult(final WebDriver driver) throws InterruptedException {

        goEditorPage();

        Thread.sleep(500); // tiempo de carga necesario para asegurar la carga
        // de la página.

        // Método de comprobación implícito
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver arg0) {
                return driver.findElements(By.cssSelector("#gcli-root div.gcli-row-out")).size() > 1; // original
                // size
                // is
                // 1
                // on
                // page
                // load
            }
        });

        Object result = (Object) TestCase.getJs()
                .executeScript("return document.getElementById('gcli-root').textContent.search(/false/i);");

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
     * @return
     * @throws InterruptedException
     */
    public static boolean isAnyUserLogged() throws InterruptedException {

        boolean ret = false;
        String url = TestCase.getUrlAbsolute("researcher/principaluser/");
        ret = "200".equals(TestCase.getStatusCode(url));
        return ret;

    }

    /**
     * Check if the current content of IDEAS editor is empty.
     *
     * @return true if editor exists and it is empty.
     */
    public static boolean isEditorContentEmpty() {

        boolean ret = false;

        try {
            if (TestCase.isCurrentUrlContains("app/editor")) {
                Object jsObj = TestCase.getJs()
                        .executeScript("return document.editor ? document.editor.getValue() : null;");
                String editorContent = "";
                if (jsObj != null) {
                    editorContent = (String) jsObj;
                    ret = editorContent.equals("");
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(IdeasStudioActions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;

    }

    /**
     * Open dynatree context menu by node title.
     *
     * @param fileName
     * @return
     */
    public static boolean activateDynatreeContextMenuByFileName(String fileName) {
        boolean ret = false;
        String contextMenuSelector = "#myMenu";
        try {
            Object obj = (Object) TestCase.getJs().executeScript(""
                    + "var ret = false;"
                    + "jQuery('span a.dynatree-title').filter(function(){"
                    + "  if (jQuery(this).text() == '" + fileName + "') {"
                    + "    jQuery(this).trigger({type:'mousedown',button:2}).trigger({type:'mouseup',button:2});"
                    + "    ret = true;"
                    + "  }"
                    + "});"
                    + "return ret;");

            if (obj != null) {
                TestCase.waitForVisibleSelector(contextMenuSelector);
                ret = TestCase.getWebDriver().findElement(By.cssSelector(contextMenuSelector)).isDisplayed();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return ret;
    }

    /**
     *
     * @param wsName
     * @return
     */
    public static boolean existWorkspaceByName(String wsName) {
        boolean ret = false;
        try {
            Object obj = (Object) TestCase.getJs().executeScript(""
                    + "var ret = false;"
                    + "jQuery('#workspacesNavContainer li').filter(function(){"
                    + "  if (jQuery(this).text() == '" + wsName + "') {"
                    + "    ret = true;"
                    + "  }"
                    + "});"
                    + "return ret;");

            if (obj != null) {
                ret = (Boolean) obj;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return ret;
    }

    public static boolean expandAllDynatreeNodes() {
        boolean ret = false;

        try {
            TestCase.waitForVisibleSelector("#projectsTree");

            Object obj = (Object) TestCase.getJs().executeScript(""
                    + "var ret=false;"
                    + "try {"
                    + "  $('#projectsTree').dynatree('getRoot').visit(function(node){"
                    + "    node.expand(true);"
                    + "  });"
                    + "  ret = true;"
                    + "} catch (err) {"
                    + "  ret=false;"
                    + "}"
                    + "return ret;");
            if (obj != null) {
                ret = (Boolean) obj;
            }

            Thread.sleep(1000); // animation
        } catch (InterruptedException ex) {
            Logger.getLogger(IdeasStudioActions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ret = false;
            Logger.getLogger(IdeasStudioActions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    public static boolean expandAddMenu() {
        boolean ret = false;

        try {
            String addMenuSelector = "div#editorSidePanelHeaderAddProject div.dropdown-toggle";
            if (TestCase.existDOMElement(addMenuSelector)) { // still not open
//                TestCase.getExpectedActions().click(By.cssSelector(addMenuSelector));
                TestCase.getJs().executeScript("jQuery('" + addMenuSelector + "').click();");
                ret = true;
            }
            Thread.sleep(1000); // animation
        } catch (InterruptedException ex) {
            Logger.getLogger(IdeasStudioActions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ret = false;
            Logger.getLogger(IdeasStudioActions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    public static boolean setCurrentEditorContent(String content) {
        boolean ret = false;
        Object obj = (Object) TestCase.getJs().executeScript(""
                + "var ret = false;"
                + "if (document.editor) {"
                + "  document.editor.session.setValue('" + content + "');"
                + "  ret = true;"
                + "}"
                + "return ret;");
        if (obj != null) {
            ret = (Boolean) obj;
        }
        return ret;
    }

    public static String getActiveNodeName() {
        String ret = "";
        String selectorDynaTree = "#projectsTree";

        TestCase.waitForVisibleSelector(selectorDynaTree);
        Object obj = (Object) TestCase.getJs().executeScript(""
                + "var tree = jQuery('" + selectorDynaTree + "').dynatree('getTree');"
                + "return tree.getActiveNode().data.title;");

        if (obj != null) {
            ret = (String) obj;
        }

        return ret;
    }

}
