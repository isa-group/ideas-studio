package es.us.isa.ideas.app.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.us.isa.ideas.app.security.Credentials;
import es.us.isa.ideas.app.security.UserAccount;
import es.us.isa.ideas.app.security.UserAccountService;
import es.us.isa.ideas.app.services.ResearcherService;

@Controller
public class DemoController extends AbstractController{
	@Autowired
	UserAccountService userAccountService;
//	@Autowired
//	UserAccountRepository userRepository;
//	@Autowired
//	ResearcherRepository researcherRepository;
	@Autowired
	ResearcherService researcherService;
	
	public DemoController(){}
	
	@RequestMapping("/demo/*")
	public ModelAndView generateDemoUser(HttpServletRequest request){
		
		Collection<UserAccount> allUsers;
		Integer actual = 0;
		Integer number = 0;
		System.out.println("[INFO] Demo controller start.");
		System.out.println("[INFO] Initiating demo user generation.");

		System.out.println("[INFO] Browsing users.");
		allUsers = userAccountService.findAll();
		for(UserAccount usr : allUsers){
			if(usr!=null){				
				if(usr.getUsername()!=null){
					if( usr.getUsername().startsWith("demo")){
						System.out.println("[INFO] username: "+usr.getUsername());
						String sNumber = usr.getUsername().split("demo")[1];
						System.out.println("[INFO] userNumber: "+sNumber);
						
						try{
							number = new Integer(sNumber);
						}catch(NumberFormatException e){
							number=0;
						}
						
						System.out.println("[INFO] Number: "+number);
						if(number>actual){
							actual = number;
							System.out.println("[INFO] actual: "+actual);
						}
					}
				}else{
					System.out.println("[INFO] User name is null, ID:"+usr.getId());
				}
			
			}else{
				System.out.println("[INFO] NULL found:"+usr);
			}
		}
		String pass = "demo"+(actual+1);
		String user = "demo"+(actual+1);
		
		System.out.println("[INFO] PASSWORD: "+pass);
		userAccountService.create(null, user, pass, "");	
		
//		FSWorkspace demoWS = new FSWorkspace(request.getPathTranslated(), "DemoMaster");
//        FSWorkspace newWS = new FSWorkspace(targetWorkspaceName, username);
		
		Credentials credentials = new Credentials();
		credentials.setJ_username(user);
		credentials.setPassword(pass);
		System.out.println("[INFO] END");
		
		ModelAndView result = new ModelAndView("security/login");

		result.addObject("credentials", credentials);
		result.addObject("showError", false);
		String originalUrl = "https://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/app/editor";
		result.addObject("originalRequestUrl", originalUrl);

		return result;
	}
	
	
}
