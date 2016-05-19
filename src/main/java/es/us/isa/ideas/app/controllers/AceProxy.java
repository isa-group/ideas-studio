package es.us.isa.ideas.app.controllers;

import com.google.common.base.Strings;
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
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private StudioConfiguration studioConfiguration;

    private final Map<String, String> modeUriCache = new HashMap<>();

    @RequestMapping(value = "/ace/{file}", method = RequestMethod.GET)
    @ResponseBody
    public String getAceproxyContent(@PathVariable String file,
            HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/javascript");
        response.setCharacterEncoding("UTF-8");

        if (file.equals(ACE_LIB)) {
            return getAceFile();
        } else if (file.startsWith(MODE_PREFIX)) {
            return getRemoteAceContent(file);
        } else if (file.startsWith(THEME_PREFIX)) {
            return getRemoteAceContent(file);
        } else {
            return "Unexpected file: " + file;
        }

    }

    private String getAceFile() {
        String content = "";

        try {

            InputStream input = servletContext.getResourceAsStream(PARENT_PATH
                    + ACE_LIB + JS_EXT);

            StringWriter writer = new StringWriter();
            IOUtils.copy(input, writer);

            content = writer.toString();

        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

        return content;
    }

    private String requestContent(String stringUrl) {

        String result = "";

        try {
            URL url = new URL(stringUrl);

            // Workaround for SSL problem doing the module servlets requests
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs,
                        String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            LOGGER.log(Level.INFO, "Getting content from: " + url);
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result += inputLine + System.getProperty("line.separator");
            }
            in.close();

        } catch (MalformedURLException e) {
            LOGGER.severe(e.getMessage());
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        } catch (KeyManagementException e) {
            LOGGER.severe(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.severe(e.getMessage());
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

        return result;
    }

    private String getRemoteAceContent(String fileName) {

        String result = "";

        if (modeUriCache.containsKey(fileName)) {
            LOGGER.log(Level.INFO, "Getting from cache for '" + fileName + "' (uri=" + modeUriCache.get(fileName) + ")");
            return requestContent(modeUriCache.get(fileName));
        } else {

            for (String moduleEndpoint : studioConfiguration.getModules().values()) {
                try {
                    String languageModuleUri;

                    Properties props = new Properties();
                    props.load(getClass().getResourceAsStream("/application.properties"));
                    if (Boolean.valueOf(props.getProperty("application.dockerMode"))) {
                        languageModuleUri = "https://modules:" + ConfigurationController.MODULES_PORT + moduleEndpoint;
                    } else {
                        languageModuleUri = "https://localhost:8181" + moduleEndpoint;
                    }

                    String languageString = requestContent(languageModuleUri + LANGUAGE_ENDPOINT);
                    if (!Strings.isNullOrEmpty(languageString)) {
                        JSONObject json;
                        try {
                            json = new JSONObject(languageString);
                            JSONArray formats = json.getJSONArray("formats");
                            for (int i = 0; i < formats.length(); i++) {
                                JSONObject format = formats.getJSONObject(i);
                                String formatId = format.getString("format");

                                if (!format.isNull("_editorModeURI")) {
                                    String editorModeURI = format.getString("_editorModeURI");

                                    if (fileName.startsWith(MODE_PREFIX) && editorModeURI != null && editorModeURI.equals(fileName + JS_EXT)) {
                                        String uri = languageModuleUri + LANGUAGE_ENDPOINT + FORMAT_ENDPOINT + "/" + formatId + "/mode";
                                        LOGGER.log(Level.INFO, "Loading mode from: "
                                                + uri);
                                        result = requestContent(uri);
                                        modeUriCache.put(fileName, uri);
                                        return result;
                                    }
                                }

                                if (!format.isNull("_editorThemeURI")) {
                                    String editorThemeURI = format.getString("_editorThemeURI");

                                    if (fileName.startsWith(THEME_PREFIX)) {
                                        LOGGER.log(Level.INFO, "Is " + editorThemeURI + " equal to " + fileName + JS_EXT + "?");
                                        if (editorThemeURI != null && editorThemeURI.equals(fileName + JS_EXT)) {
                                            String uri = languageModuleUri + LANGUAGE_ENDPOINT + FORMAT_ENDPOINT + "/" + formatId + "/theme";
                                            LOGGER.log(Level.INFO, "Loading theme from: " + uri);
                                            result = requestContent(uri);
                                            modeUriCache.put(fileName, uri);
                                            return result;
                                        }
                                    }
                                }
                            }
                        } catch (JSONException ex) {
                            Logger.getLogger(AceProxy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        Logger.getLogger(AceProxy.class.getName()).severe("Failed loading language manifest from " + languageModuleUri);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(AceProxy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }
}
