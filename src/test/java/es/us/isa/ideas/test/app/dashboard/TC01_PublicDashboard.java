package es.us.isa.ideas.test.app.dashboard;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.logout;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Daniel Francisco Alonso Jiménez <dalonso1@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC01_PublicDashboard extends TestCase{
    
    protected static final String SELECTOR_DASHBOARD_PUBLIC_DEMO_VIEW = "#viewDemo";
    
    private static boolean testResult = false;
    private static final Logger LOG = Logger.getLogger(TestCase.class.getName());
    
    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "## Init TC01_PublicDashboard...");
        try {
            logout();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "## TC01_PublicDashboard finished");
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, "testResult: {0}", testResult);
        testResult = false;
    }
    
    @Test
    public void step01_goWSMPage() {
        testResult = IdeasStudioActions.goWSMPage();
        assertTrue(testResult);
    }
    
    @Test
    public void step02_viewDemo() {
        
        waitForVisibleSelector(SELECTOR_DASHBOARD_PUBLIC_DEMO_VIEW);
        getJs().executeScript("jQuery('" + SELECTOR_DASHBOARD_PUBLIC_DEMO_VIEW + "').click();");
        
        testResult = true; 
        assertTrue(testResult);
    }
}