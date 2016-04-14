package es.us.isa.ideas.app.controllers;

import es.us.isa.ideas.app.security.LoginService;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController extends AbstractController {

       
	// Constructors -----------------------------------------------------------
	
	public WelcomeController() {
		super();
	}
		
	// Index ------------------------------------------------------------------		

        @RequestMapping(value = "/", method = RequestMethod.GET)
        public ModelAndView home(){            
            if(LoginService.isAuthenticated())
                return new ModelAndView("app/editor");
            else
                return index();
        }
        
        
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());
				
		result = new ModelAndView("welcome");		
//		result = new ModelAndView("redirect:/app/editor");	

		return result;
	}
}