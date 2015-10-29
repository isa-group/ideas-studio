package es.us.isa.ideas.app.controllers;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import es.us.isa.ideas.app.configuration.StudioConfiguration;

@Controller
@RequestMapping("/module")
public class ConfigurationController extends AbstractController {
	
	@Autowired
	private ServletContext servletContext;
	
	protected final String CONFIG_PATH = "/WEB-INF/config/studio-configuration.json";
	protected final String DEVELOP_PATH = "/WEB-INF/config/develop-configuration.json";
	
	// TODO: use spring cache
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	@ResponseBody
	public StudioConfiguration getConfiguration(HttpServletRequest request) {
		StudioConfiguration config = null;		
		
		File f = new File(servletContext.getRealPath(DEVELOP_PATH));
		if(f.exists()){
			config = loadConfiguration(DEVELOP_PATH);
		}else{
			config = loadConfiguration(CONFIG_PATH);
		}
//		
//		if(!request.getRequestURL().toString().startsWith("https://localhost")){
//			config = loadConfiguration(CONFIG_PATH);
//		}else{
//			config = loadConfiguration(DEVELOP_PATH);
//		}
		return config;
	}
	
	
	// -----
	
	// Aux
	public StudioConfiguration loadConfiguration(String path) {
		StudioConfiguration config = null;
		InputStream input = null;
		
		try {
			
			input = servletContext.getResourceAsStream(path);

			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			String json = writer.toString();
			
			System.out.println(json);
	        
	        Gson gson = new Gson();
	        config = gson.fromJson(json, StudioConfiguration.class);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		return config;
	}

}
