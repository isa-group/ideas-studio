package es.us.isa.ideas.app.controllers;

import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author danyal
 */
@Controller
@RequestMapping("/help")
public class HelpController {
    private static final Logger LOGGER = Logger.getLogger(HelpController.class.getName());

    public HelpController() {
	super();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("redirect:/app/help");
    }

    @RequestMapping(value = "/{moduleName}", method = RequestMethod.GET)
    public ModelAndView getModuleHelp(@PathVariable("moduleName") String moduleName) {
        return new ModelAndView("redirect:/app/editor");
    }
}