package es.us.isa.ideas.test.app.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TC04_Register.class})
public class TestSuite extends es.us.isa.ideas.test.utils.TestCase {

    private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "Starting Login TestSuite...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "Login TestSuite finished");
    }

    protected static String getUsername() {
        return getSeleniumAutotesterUser();
    }

    protected static String getEmail() {
        return getSeleniumUserEmail();
    }

    protected static String getPhone() {
        return getSeleniumUserPhone();
    }

    protected static String getAddress() {
        return getSeleniumUserAddress();
    }

}
