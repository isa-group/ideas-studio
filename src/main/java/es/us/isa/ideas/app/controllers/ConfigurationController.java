package es.us.isa.ideas.app.controllers;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.isa.ideas.app.configuration.StudioConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;

@Controller
@RequestMapping("/module")
public class ConfigurationController extends AbstractController {

    protected static final String CONFIG_PATH = "/WEB-INF/config/studio-configuration.json";
    protected static final String DEVELOP_PATH = "/WEB-INF/config/develop-configuration.json";

    public static final String MODULES_PORT = "10943";

    private static final Logger LOG = Logger.getLogger(ConfigurationController.class.getName());

    @Autowired
    StudioConfiguration studioConfiguration;

    @Autowired
    ServletContext servletContext;

    // TODO: use spring cache
    @RequestMapping(value = "/configuration", method = RequestMethod.GET)
    @ResponseBody
    public StudioConfiguration getConfiguration(HttpServletRequest request) {

        // Select configuration path by order preference
        String finalPath = "";
        List<String> paths = new ArrayList<>();
        paths.add(DEVELOP_PATH);
        paths.add(CONFIG_PATH);

        for (String path : paths) {
            try {
                if (new File(servletContext.getRealPath(path)).exists()) {
                    finalPath = path;
                    break;
                }
            } catch (Exception e) {
                LOG.severe(e.getMessage());
            }
        }

        if (!Strings.isNullOrEmpty(finalPath)) {
            try {
                Gson gson = new Gson();
                String json = FileUtils.readFileToString(new File(servletContext.getRealPath(finalPath)));
                StudioConfiguration newStudioConfiguration = gson.fromJson(json, StudioConfiguration.class);

                studioConfiguration.setConfigurationFiles(newStudioConfiguration.getConfigurationFiles());
                studioConfiguration.setGoogleAnalyticsID(newStudioConfiguration.getGoogleAnalyticsID());
                studioConfiguration.setHelpURI(newStudioConfiguration.getHelpURI());
                studioConfiguration.setHelpMode(newStudioConfiguration.getHelpMode());
                studioConfiguration.setImages(newStudioConfiguration.getImages());
                studioConfiguration.setWorkbenchName(newStudioConfiguration.getWorkbenchName());
                studioConfiguration.setAdvancedMode(newStudioConfiguration.getAdvancedMode());
                
                Properties props = new Properties();
                props.load(getClass().getResourceAsStream("/application.properties"));
                String modulesPort = ":" + request.getServerPort();
                if (Boolean.valueOf(props.getProperty("application.dockerMode"))) {
                    modulesPort = ":" + MODULES_PORT;
                }
                String scheme = request.getScheme();
                String serverName = request.getServerName();
                Map<String, String> modules = new HashMap<>();
                for (String moduleId : newStudioConfiguration.getModules().keySet()) {
                    String endpoint = scheme + "://" +  serverName + modulesPort + newStudioConfiguration.getModules().get(moduleId);
                    modules.put(moduleId, endpoint);
                }
                studioConfiguration.setModules(modules);
            } catch (IOException ex) {
                Logger.getLogger(StudioConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return studioConfiguration;
    }

}
