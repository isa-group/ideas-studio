package es.us.isa.ideas.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.us.isa.ideas.app.mail.CustomMailer;

@Controller
@RequestMapping("/app")
public class AppController extends AbstractController {

	@Autowired
	CustomMailer mailer;

	public AppController() {
		super();
	}

	@RequestMapping("/editor")
	public ModelAndView editor() {
		ModelAndView result;
		result = new ModelAndView("app/editor");
		return result;
	}

	@RequestMapping("/social")
	public ModelAndView social() {
		ModelAndView result;
		result = new ModelAndView("app/social");
		return result;
	}

	@RequestMapping("/settings")
	public ModelAndView settings() {
		ModelAndView result;
		result = new ModelAndView("app/settings");
		return result;
	}

	@RequestMapping("/administration")
	public ModelAndView administration() {
		ModelAndView result;
		result = new ModelAndView("app/administration");
		return result;
	}

	@RequestMapping("/help")
	public ModelAndView help() {
		ModelAndView result;
		result = new ModelAndView("app/help");
		return result;
	}

	// Content for AJAX requests:

	@RequestMapping("/app_editor_content")
	public ModelAndView app_editor_content() {
		ModelAndView result;
		result = new ModelAndView("app/editor");
		return result;
	}

	@RequestMapping("/app_social_content")
	public ModelAndView app_social_content() {
		ModelAndView result;
		result = new ModelAndView("app/social");
		return result;
	}

	@RequestMapping("/app_settings_content")
	public ModelAndView app_settings_content() {
		ModelAndView result;
		result = new ModelAndView("app/settings");
		return result;
	}

	@RequestMapping("/app_administration_content")
	public ModelAndView app_administration_content() {
		ModelAndView result;
		result = new ModelAndView("app/administration");
		return result;
	}

	@RequestMapping("/app_help_content")
	public ModelAndView app_help_content() {
		ModelAndView result;
		result = new ModelAndView("app/help");
		return result;
	}

	// Settings AJAX requests:
	@RequestMapping("/app_settings_content_user")
	public ModelAndView app_settings_content_user() {

		// ModelAndView result = null;
		// String resultURL=controlAccess(researcher);
		// if (binding.hasErrors()) {
		// result = createModelAndView(researcher, "researcher.commit.error");
		// result.addObject("url", "researcher/edit");
		// } else {
		// try {
		// beginTransaction();
		// researcherService.save(researcher);
		// commitTransaction(); =
		// result = new ModelAndView(resultURL);
		// } catch (Throwable oops) {
		// rollbackTransaction();
		// result = createModelAndView(researcher, "researcher.commit.error");
		// }
		// }
		// return result;
		ModelAndView result;
		result = new ModelAndView("app/settings");
		return result;
	}

	@RequestMapping("/modalWindows/{window}")
	public ModelAndView modalWindow(@PathVariable String window, @RequestParam Map<String, String> allRequestParams) {
		ModelAndView result = new ModelAndView("app/modalWindows/" + window);
		for (String key : allRequestParams.keySet())
			result.addObject(key, allRequestParams.get(key));
		return result;
	}

	@RequestMapping("/share")
	public void share(@RequestParam String to, @RequestParam String content) {
		try {
			mailer.sendMail(to, "[IDEAS] A document was shared with you", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
