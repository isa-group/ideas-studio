package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.testcase.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class RegisterSocialGooglePage extends PageObject<RegisterSocialGooglePage> {

    @FindBy(id = "username_or_email")
    WebElement usernameField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "allow")
    WebElement loginButton;

    static final Logger LOG = Logger.getLogger(RegisterSocialGooglePage.class.getName());

    public RegisterSocialGooglePage clickOnLogin() {
        loginButton.click();
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage typeUsername(String twUser) {
        usernameField.sendKeys(twUser);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage typePassword(String twPass) {
        passwordField.sendKeys(twPass);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public EditorPage login(String twUser, String twPass) {
        this.typeUsername(twUser)
            .typePassword(twPass)
            .clickOnLogin();

        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

}
