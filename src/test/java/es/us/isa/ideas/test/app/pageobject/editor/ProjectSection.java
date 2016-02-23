/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.editor;

import es.us.isa.ideas.test.app.pageobject.testcase.TestCase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
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
public class ProjectSection extends EditorPage {

    @FindBy(css = "#projectsTree > ul > li:nth-child(1) > span > a")
    WebElement projectElement;

    public static void testCreateProject(String projName) {
        new ProjectTestCase().testCreateProject(projName);
    }

    public WebElement getProjectElement() {
        return PageFactory.initElements(getWebDriver(), ProjectSection.class).projectElement;
    }

    private static class ProjectTestCase extends TestCase {

        public void testCreateProject(String projName) {

            EditorPage page = EditorPage.navigateTo()
                .clickOnProjectAddButton()
                .clickOnCreateProjectAnchor()
                .typeProjectName(projName)
                .clickOnModalContinueButton();

            getWebDriver().navigate().refresh();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectSection.class.getName()).log(Level.SEVERE, null, ex);
            }

            WebElement element = getWebDriverWait().until(ExpectedConditions.visibilityOf(new ProjectSection().getProjectElement()));

            TEST_RESULT = element.getText().equals(projName);

            if (TEST_RESULT) {
                page.consoleEchoCommand("Project \"" + projName + "\" was successfully created.");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }
    }
}
