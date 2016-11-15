package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		
		//get paramryrts from request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		//validate parameters(username, password, verify)
		if (User.isValidUsername(username) == false){
			model.addAttribute("username_error", "Invalid Username");
			return "signup";
		}
		if (!(User.isValidPassword(password))){
			model.addAttribute("password_error", "Invalid Password");
			return "signup";
		}
		if (!(password.equals(verify))){
			model.addAttribute("verify_error", "Passwords do not match");
			return "signup";
		}
		//if they validate, create a new user and put them in the session
		else{
			User u = new User(username, password); 
			userDao.save(u);
			HttpSession thisSession = request.getSession();
			setUserInSession(thisSession, u);
			return "redirect:blog/newpost";
		}
		//Session.thisSession = request.getSession();
		

	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		
		//get parameters from request
		
		//get user by their username
		
		//check password is correct
		
		//log them in, if so(i.e. setting the user in the session)
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User u = userDao.findByUsername(username);
		if (u == null){
			model.addAttribute("username_error", "Invalid Username");
			return "login";
		}		
		
		if (!(u.isMatchingPassword(password))){
			model.addAttribute("password_error", "Invalid Password");
			model.addAttribute("username", username);
			return "login";
		}
		//if they validate, create a new user and put them in the session
		else{
			HttpSession thisSession = request.getSession();
			setUserInSession(thisSession, u);
			return "redirect:blog/newpost";
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
