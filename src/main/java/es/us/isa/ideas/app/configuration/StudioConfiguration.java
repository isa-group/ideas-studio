package es.us.isa.ideas.app.configuration;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class StudioConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_FILE_PATH="/WEB-INF/config/studio-configuration.json";

    private String workbenchName;
    
    private Map<String, String> languages;
    private Map<String, String> images;
    private Map<String, String> configurationFiles;
    private String helpURI;
    private String googleAnalyticsID;

    public StudioConfiguration() {
        super();
    }
    
    public Map<String, String> getLanguages() {
        return languages;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public static StudioConfiguration load() {
        return load(DEFAULT_FILE_PATH);
    }
    
    public static StudioConfiguration load(String configFilePath) {
        StudioConfiguration config = null;

        try {
            String json = FileUtils.readFileToString(new File(configFilePath));
            System.out.println(json);

            Gson gson = new Gson();
            config = gson.fromJson(json, StudioConfiguration.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return config;
    }

    public void setConfigurationFiles(Map<String, String> configFiles) {
        this.configurationFiles = configFiles;
    }

    public Map<String, String> getConfigurationFiles() {
        return configurationFiles;
    }

    public String getWorkbenchName() {
        return workbenchName;
    }

    public void setWorkbenchName(String workbenchName) {
        this.workbenchName = workbenchName;
    }

	public String getHelpURI() {
		return helpURI;
	}

	public void setHelpURI(String helpURI) {
		this.helpURI = helpURI;
	}
 
    public String getGoogleAnalyticsID() {
    	return googleAnalyticsID;
    }
    
    public void setGoogleAnalyticsID(String ga) {
    	this.googleAnalyticsID = ga;
    }
    
    
}
