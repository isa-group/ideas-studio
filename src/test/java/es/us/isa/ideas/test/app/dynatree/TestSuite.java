package es.us.isa.ideas.test.app.dynatree;

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
    TC01_Login.class,
    TC02_CreateWorkspace.class,
    TC03_CreateProject.class,
    TC04_CreateFile.class,
    TC05_CreateDirectory.class,
    TC06_CreateDirectory2L.class,
    TC07_CreateFile.class,
    TC08_RenameFile.class,
    TC09_EditFile2L.class,
    TC10_RenameFile2L.class,
    TC99_RemoveCurrentWorkspace.class
})
public class TestSuite extends es.us.isa.ideas.test.utils.TestCase {

    private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());

    @BeforeClass
    public static void setUp() {
        LOG.log(Level.INFO, "#### Starting Dynatree TestSuite...");
    }

    @AfterClass
    public static void tearDown() {
        LOG.log(Level.INFO, "#### Dynatree TestSuite finished");
    }

    protected static String getTestProperty(String key) {
        return getSeleniumGeneralProperties().getProperty(key);
    }

    protected static String getWorkspace() {
        return getTestProperty("test.app.dynatree.workspace");
    }

    protected static String getProject() {
        return getTestProperty("test.app.dynatree.project");
    }
    
    protected static String getFileName1() {
        return getTestProperty("test.app.dynatree.file1.name");
    }
    
    protected static String getFileExt1() {
        return getTestProperty("test.app.dynatree.file1.extension");
    }
    
    protected static String getFileName2() {
        return getTestProperty("test.app.dynatree.file2.name");
    }
    
    protected static String getFileExt2() {
        return getTestProperty("test.app.dynatree.file2.extension");
    }
    
    protected static String getDir1() {
        return getTestProperty("test.app.dynatree.dir1");
    }
    
    protected static String getDir2() {
        return getTestProperty("test.app.dynatree.dir2");
    }

}