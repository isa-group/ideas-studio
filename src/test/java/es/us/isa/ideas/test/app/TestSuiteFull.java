package es.us.isa.ideas.test.app;

import es.us.isa.ideas.test.utils.TestCase;
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
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    es.us.isa.ideas.test.app.dynatree.TestSuite.class,
	es.us.isa.ideas.test.app.login.TestSuite.class,
	es.us.isa.ideas.test.module.TestSuite.class
})
public class TestSuiteFull {

    private static final Logger LOG = Logger.getLogger(TestSuiteFull.class
            .getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "Starting TestSuiteFull...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "TestSuiteFull finished");
		TestCase.getWebDriver().close();
    }

}
