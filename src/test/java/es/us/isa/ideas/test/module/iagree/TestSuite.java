package es.us.isa.ideas.test.module.iagree;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.auth.AuthenticationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.us.isa.ideas.test.utils.IdeasAppActions;
import es.us.isa.ideas.test.utils.TestCase;

/**
 * iAgree TestModule execution
 * 
 * @author feserafim
 */

public class TestSuite {

	private static final Logger LOG = Logger.getLogger(TestSuite.class
			.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Starting iAgree module TestSuite...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "iAgree module TestSuite finished");
	}

	@Test
	public void testIAgreeAgreementModule() throws InterruptedException {

		LOG.log(Level.INFO, "testing iAgree Agreement Module");

		IdeasAppActions ideas = new IdeasAppActions();

		IdeasAppActions.goHomePage();

		ideas.executeCommands(TestCase.getWebDriver(),
				"testModule iagree-agreement-language");

		boolean result = IdeasAppActions.checkTestModuleOkResult(TestCase
				.getWebDriver());

		if (result) {
			LOG.log(Level.INFO, "test OK");
		} else {
			LOG.log(Level.SEVERE, "test FALSE");
		}

		assertTrue(result);

	}

	@Test
	public void testIAgreeOfferModule() throws InterruptedException {

		LOG.log(Level.INFO, "testing iAgree Offer Module");

		IdeasAppActions.goHomePage();

		new IdeasAppActions().executeCommands(TestCase.getWebDriver(),
				"testModule iagree-offer-language");

		boolean result = IdeasAppActions.checkTestModuleOkResult(TestCase
				.getWebDriver());

		if (result) {
			LOG.log(Level.INFO, "test OK");
		} else {
			LOG.log(Level.SEVERE, "test FALSE");
		}

		assertTrue(result);

	}

	@Test
	public void testIAgreeTemplateModule() throws InterruptedException {

		LOG.log(Level.INFO, "testing iAgree Template Module");

		IdeasAppActions.goHomePage();

		new IdeasAppActions().executeCommands(TestCase.getWebDriver(),
				"testModule iagree-template-language");

		boolean result = IdeasAppActions.checkTestModuleOkResult(TestCase
				.getWebDriver());

		if (result) {
			LOG.log(Level.INFO, "test OK");
		} else {
			LOG.log(Level.SEVERE, "test FALSE");
		}

		assertTrue(result);

	}

	/**
	 * Check if user can load an .at file.
	 * 
	 * @throws AuthenticationException
	 * @throws IOException
	 */
	@Test
	public void testLoadIAgreeTemplateFile() throws AuthenticationException,
			IOException {

		LOG.log(Level.INFO, "testing iAgree Template file load");
		// createIAgreeWorkspaceToTest();

		// sendPost("https://localhost:8181/ideas-studio/",)
		// URL url = new URL("https://localhost:8181/ideas-studio/getWorkspaces/");
		InputStream response = new URL(
				"https://localhost:8181/ideas-studio/getWorkspaces/").openStream();

		System.out.println(response.toString());

		assertTrue(false);

	}

	public static String sendPost(String url, String content) throws Exception {

		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("localhost")) {
							return true;
						}
						return false;
					}
				});

		URL obj = new URL(url);
		// HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.addRequestProperty("Content-Type", "application/" + "G");

		// add reuqest header
		con.setRequestMethod("GET");

		// String data = URLEncoder.encode(content.replaceAll("\\+",
		// "%2B"),"UTF-8");
		String data = URLEncoder.encode(content, "UTF-8");
		con.setRequestProperty("Content-Length",
				Integer.toString(data.length()));

		// Send post request
		con.setDoOutput(true);
		try {
			OutputStream os = con.getOutputStream();
			os.write(data.getBytes());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		StringBuilder response = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response.toString();
	}

}
