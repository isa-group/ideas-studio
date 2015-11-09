package es.us.isa.ideas.test.module.plaintext;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC05_EditFile extends es.us.isa.ideas.test.utils.TestCase {

	private static String fileName = "file";

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC05_EditFile...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC05_EditFile finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goEditorPage() {
		testResult = IdeasStudioActions.goEditorPage();
		assertTrue(testResult);
	}

	@Test
	public void step02_editFile() throws InterruptedException {

		LOG.info("testEditFile :: Editing a file...");

		TestCase.getExpectedActions().click(By.cssSelector(".dynatree-ico-c"));

		LOG.info("\t :: Introduciendo contenido al fichero");

		Thread.sleep(2000);

		TestCase.getJs()
				.executeScript("document.editor.session.setValue('" + "Template EC2 version 1.0\\n"
						+ "\\tProvider \"Amazon\" as Responder;\\n" + "\\n" + "\\tMetrics:\\n"
						+ "\\t\\tpercent: integer[0..100]; \\n" + "\\t\\tfloatPercent: float[0..100]; \\n"
						+ "\\t\\tsize: integer[0..500];\\n" + "\\n" + "AgreementTerms\\n" + "\\n"
						+ "\\tService AWS-S3PremiumSupport availableAt \"http://aws.amazon.com/s3/\"\\n"
						+ "\\t\\tGlobalDescription\\n" + "\\t\\t\\tRegionUnits: size;\\n" + "\\t\\t\\tSCP: percent;\\n"
						+ "\\n" + "\\tMonitorableProperties\\n" + "\\t\\tglobal:\\n" + "\\t\\t\\tMUP: floatPercent;\\n"
						+ "\\n" + "\\tGuaranteeTerms\\n" + "\\t\\tG1: Provider guarantees MUP >= 99.95; \\n"
						+ "\\t\\t\\tonlyIf (RegionUnits > 2); \\n" + "\\t\\t\\twith monthly penalty \\n"
						+ "\\t\\t\\t\\tof SCP = 10 if MUP <= 99.0 AND MUP < 99.95; \\n"
						+ "\\t\\t\\t\\tof SCP = 30 if MUP < 99.0;\\n" + "\\t\\t\\tend\\n" + "\\n"
						+ "CreationConstraints\\n" + "\\tC1: none;\\n" + "\\n" + "EndTemplate');");

		Thread.sleep(1000);
		
		// Check if file was modified

		boolean ret = !IdeasStudioActions.isEditorContentEmpty();

		String msg = "";
		if (ret) {
			msg = "File \"" + fileName + ".txt\" was successfully edited.";
			LOG.info("\t :: File \"" + fileName + ".txt\" was successfully edited.");
			echoCommandApi("File \"" + fileName + ".txt\" was successfully edited.");
		} else {
			msg += "Unable to change file";
		}
		LOG.info(msg);
		echoCommandApi(msg);

		assertTrue(ret);

	}

}
