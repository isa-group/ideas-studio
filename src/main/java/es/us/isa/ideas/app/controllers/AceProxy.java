package es.us.isa.ideas.app.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.isa.ideas.app.configuration.StudioConfiguration;
import com.google.gson.Gson;

import es.us.isa.ideas.module.model.Format;
import es.us.isa.ideas.module.model.Language;

@Controller
@RequestMapping("/js")
public class AceProxy extends AbstractController {
	
    private static final Logger LOGGER = Logger.getLogger(AceProxy.class
            .getName());

	private final String PARENT_PATH = "/js/ace/";
	private final String ACE_LIB = "ace";
	private final String JS_EXT = ".js";
	private final String MODE_PREFIX = "mode-";
	private final String THEME_PREFIX = "theme-";
	private final String LANGUAGE_ENDPOINT = "/language";
	private final String FORMAT_ENDPOINT = "/format";
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private ConfigurationController moduleController;
	
	private Map<String,String> modeUriCache = new HashMap<String,String>();

	
	@RequestMapping(value = "/ace/{file}", method = RequestMethod.GET)
	@ResponseBody
	public String getAceproxyContent(@PathVariable String file, HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		
		if (file.equals(ACE_LIB)) {
			return getAceFile();
		} else if (file.startsWith(MODE_PREFIX)) {
			return getRemoteAceContent(file, request);
		} else if (file.startsWith(THEME_PREFIX)) {
			return getRemoteAceContent(file, request);
		} else {
			return "Unexpected file: " + file;
		}
		
	}

	private String getAceFile() {
		String content = "";
		
		try {

			InputStream input = servletContext.getResourceAsStream(PARENT_PATH + ACE_LIB + JS_EXT);
			
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			content = writer.toString();
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		return content;
	}
	
	private String requestContent(String uri, HttpServletRequest request) {
		
		String result = "";
		
	
		try {
                        String baseRequestUrl =null;
                        if(!"".equals(request.getContextPath()))
                             baseRequestUrl=(request.getRequestURL().toString()).split(request.getContextPath())[0];
                        else{
                            String []parts=(request.getRequestURL().toString()).split("/");
                            baseRequestUrl=parts[0]+"//"+parts[2]+"/";
                        }
                        
                        if(baseRequestUrl.endsWith("/"))
                            baseRequestUrl=baseRequestUrl.substring(0,baseRequestUrl.length()-1);
			URL url = new URL(baseRequestUrl + uri);
			
			// Workaround for SSL problem doing the module servlets requests
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
			    public X509Certificate[] getAcceptedIssuers(){return null;}
			    public void checkClientTrusted(X509Certificate[] certs, String authType){}
			    public void checkServerTrusted(X509Certificate[] certs, String authType){}
			}};
			
			SSLContext sc = SSLContext.getInstance("TLS");
		    sc.init(null, trustAllCerts, new SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		    
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
			conn.setHostnameVerifier(new HostnameVerifier() {        
			    public boolean verify(String hostname, SSLSession session)  {  
			    	return true;
			    }
			});
            LOGGER.log(Level.INFO, "Getting content from: " + url);
			conn.connect();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	        	result += inputLine + System.getProperty("line.separator");
	        in.close();
			

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String getRemoteAceContent(String fileName, HttpServletRequest request) {
		
		if (modeUriCache.containsKey(fileName)) {
            LOGGER.log(Level.INFO, "Getting from cache for '" + fileName
                    + "' (uri=" + modeUriCache.get(fileName) + ")");
			return requestContent(modeUriCache.get(fileName), request);
		} else {
			String result = "";
			StudioConfiguration studioConfiguration = moduleController.getConfiguration(request);
			
			_search:
				for (String languageKey : studioConfiguration.getLanguages().keySet()) {
					String languageModuleUri = studioConfiguration.getLanguages().get(languageKey);
					
					String languageString = requestContent(languageModuleUri + LANGUAGE_ENDPOINT, request);
					Gson gson = new Gson();
			        Language languageModel = gson.fromJson(languageString, Language.class);
			        
					for (Format format : languageModel.getFormats()) {
						if (fileName.startsWith(MODE_PREFIX)) {
							if (format.get_editorModeURI() != null && format.get_editorModeURI().equals(fileName + JS_EXT)) {
								
								String uri = languageModuleUri + LANGUAGE_ENDPOINT+ FORMAT_ENDPOINT + "/" + format.getFormat() + "/mode";
								System.out.println("Loading mode from: "+uri);
								result = requestContent(uri, request);
								modeUriCache.put(fileName, uri);
								break _search;
							}
						} else if (fileName.startsWith(THEME_PREFIX)) {
                                                        System.out.println("Is "+format.get_editorThemeURI()+" equal to "+fileName + JS_EXT+"?");
							if (format.get_editorThemeURI() != null && format.get_editorThemeURI().equals(fileName + JS_EXT)) {
 								
								String uri = languageModuleUri + LANGUAGE_ENDPOINT+ FORMAT_ENDPOINT + "/" + format.getFormat() + "/theme";
                                                                System.out.println("Loading theme from: "+uri);
								result = requestContent(uri, request);
								modeUriCache.put(fileName, uri);
								break _search;
							}
						}
					}
				}
			
			return result;
		}
		
	}
}
