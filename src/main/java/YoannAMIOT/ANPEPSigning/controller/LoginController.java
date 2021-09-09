package YoannAMIOT.ANPEPSigning.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import YoannAMIOT.ANPEPSigning.entities.User;

@Controller
public class LoginController {
	
	//GET OF THE LOGIN FORM//
	@GetMapping(path={"","/login"})
	public String showLogin(@ModelAttribute User user) {
		//Return the JSP
		return "login";
	}
	
	
	
	//GET OF THE LOGIN DISPATCHER//
	@GetMapping("/loginDispatcher")
	public String dispatch(Authentication authentication, HttpServletRequest request) {
		
		//Getting the connected by it's Authentication token user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)auth.getPrincipal();
		
		//Putting the connected user in session
		HttpSession session = request.getSession(true);
		session.setAttribute("user", user);		
		
		//Creating an empty string to put the redirect in it
		String redirection = "";
		
		//Checking the responsability of the user and adapting the redirection string to the responsability
		if (user.getResponsability() == 0) {
			redirection = "redirect:studentHome";
		} else if (user.getResponsability() == 1) {
			redirection = "redirect:teacherHome";
		} else if (user.getResponsability() == 2) {
			redirection = "redirect:headmasterHome";
		}
		
		//Redirecting the user by using the adapted redirection string
		return redirection;
	}
	
	
	
	//GET OF THE LOGIN FAILED//
	@GetMapping("/fail")
	public String fail() {
		//redirecting to the login form
		return "redirect:/login";
	}
	
	
	
	@GetMapping("/error")
	public String error() {
		//redirecting to the login form
		return "redirect:/login";
	}
}
