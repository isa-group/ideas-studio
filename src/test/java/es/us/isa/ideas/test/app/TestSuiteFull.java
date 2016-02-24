package es.us.isa.ideas.test.app;

import es.us.isa.ideas.test.app.pageobject.testcase.PageObject;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCaseDynatree;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCaseRegister;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCaseWorkspaceDashboard;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCaseWorkspaceDemo;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCaseWorkspaceEditor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * This test suite should be run only by following Release Manager policy.
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestCaseRegister.class,
    TestCaseDynatree.class,
    TestCaseWorkspaceEditor.class,
    TestCaseWorkspaceDashboard.class,
    TestCaseWorkspaceDemo.class
})
public class TestSuiteFull {

    private static final Logger LOG = Logger.getLogger(TestSuiteFull.class
        .getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "#### Starting TestSuiteFull...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "#### TestSuiteFull finished");
        PageObject.getWebDriver().close();
    }

}
