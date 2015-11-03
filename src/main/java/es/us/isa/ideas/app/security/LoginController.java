package es.us.isa.ideas.app.security;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.us.isa.ideas.app.controllers.AbstractController;
import es.us.isa.ideas.app.mail.CustomMailer;

@Controller
@Transactional
@RequestMapping("/security")
public class LoginController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	LoginService service;

	private RequestCache requestCache = new HttpSessionRequestCache();

	// Constructors -----------------------------------------------------------

	public LoginController() {
		super();
	}

	// Login ------------------------------------------------------------------

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request,
			@Valid @ModelAttribute Credentials credentials,
			BindingResult bindingResult,
			@RequestParam(required = false) boolean showError) {

		Assert.notNull(credentials);
		Assert.notNull(bindingResult);

		ModelAndView result;

		DefaultSavedRequest originalRequest = (DefaultSavedRequest) request
				.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
		
		String originalUrl = null;
		try{
			originalUrl = originalRequest.getRedirectUrl();
			System.out.println(originalUrl.toString());
			System.out.println("https://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/security/login");
		}catch(Exception e){
			System.out.println("[ERROR] class: LoginController.login - redirecting to login ");
			originalUrl = "https://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/security/login";
		}
		
		result = new ModelAndView("security/login");

		result.addObject("credentials", credentials);
		result.addObject("showError", showError);
		result.addObject("originalRequestUrl", originalUrl);

		return result;
	}

	// @RequestMapping("/login_inview")
	// public ModelAndView loginInview(
	// @Valid @ModelAttribute Credentials credentials,
	// BindingResult bindingResult,
	// @RequestParam(required = false) boolean showError,
	// String originalUrl) {
	// Assert.notNull(credentials);
	// Assert.notNull(bindingResult);
	//
	// ModelAndView result;
	//
	// result = new ModelAndView("security/login_inview");
	// result.addObject("credentials", credentials);
	// result.addObject("showError", showError);
	// result.addObject("originalRequestUrl", originalUrl);
	//
	//
	//
	// return result;
	// }

	// LoginFailure -----------------------------------------------------------

	@RequestMapping("/loginFailure")
	public ModelAndView failure(@Valid @ModelAttribute Credentials credentials,
			BindingResult bindingResult, String originalUrl) {
		Assert.notNull(credentials);
		Assert.notNull(bindingResult);
		
		ModelAndView result;

		result = new ModelAndView("security/login_inview");
		result.addObject("credentials", credentials);
		result.addObject("showError", true);
		result.addObject("inview", true);
		result.addObject("originalRequestUrl", originalUrl);

		return result;
	}

}