package org.launchcode.blogz.controllers;

import java.util.List;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BlogController extends AbstractController {
	public static int pageEntries = 5;

	@RequestMapping(value = "/")
	public String index(Model model){
		
		// TODO - fetch users and pass to template
		List<User> users = userDao.findAll();
		if (users.size()> pageEntries){
			users = users.subList(0, 5);
			model.addAttribute("next_hidden", "false");
			model.addAttribute("next_page", "1");
		}
		else{
			model.addAttribute("next_hidden", "true");
		}
		//model.addAttribute("user", u);
		model.addAttribute("users", users);		
		model.addAttribute("prev_hidden", "true");
		return "index";
	}
	
	@RequestMapping(value = "/{pageNumber}", method = RequestMethod.GET)
	public String indexPage(@PathVariable int pageNumber, Model model) {
		List<User> users = userDao.findAll();
		if (pageNumber == 0){
			users = users.subList(0, users.size()-1);
			model.addAttribute("prev_hidden", "true");
		}
		else{
			users = users.subList((pageNumber*pageEntries)-1, users.size());
			model.addAttribute("prev_hidden", "false");
			model.addAttribute("prev_page", pageNumber-1);
		}
		
		if (users.size() >= pageEntries){
			
			users = users.subList(0, pageEntries);
			model.addAttribute("next_hidden", "false");
			model.addAttribute("next_page", pageNumber+1);
		}
		else{
			model.addAttribute("next_hidden", "true");
		}

		//model.addAttribute("user", u);
		model.addAttribute("users", users);		
		return "index";
		
	}
	
	@RequestMapping(value = "/blog")
	public String blogIndex(Model model) {
		
		// TODO - fetch posts and pass to template
		
		List<Post> posts = postDao.findAll();
		
		if (posts.size()> pageEntries){
			posts = posts.subList(0, 5);
			model.addAttribute("next_hidden", "false");
			model.addAttribute("next_page", "/blog/archive/1");
		}
		else{
			model.addAttribute("next_hidden", "true");
		}
		//model.addAttribute("user", u);
		model.addAttribute("posts", posts);	
		model.addAttribute("prev_hidden", "true");
		
		return "blog";
	}
	
	@RequestMapping(value = "/blog/archive/{pageNumber}", method = RequestMethod.GET)
	public String blogPage(@PathVariable int pageNumber, Model model) {
		List<Post> posts = postDao.findAll();
		if (pageNumber == 0){
			posts = posts.subList(0, posts.size()-1);
			model.addAttribute("prev_hidden", "true");
		}
		else{
			posts = posts.subList((pageNumber*pageEntries)-1, posts.size());
			model.addAttribute("prev_hidden", "false");
			model.addAttribute("prev_page", "/blog/archive/" + (pageNumber-1));
		}
		
		if (posts.size() >= pageEntries){
			
			posts = posts.subList(0, pageEntries);
			model.addAttribute("next_hidden", "false");
			model.addAttribute("next_page", "/blog/archive/" + (pageNumber+1));
		}
		else{
			model.addAttribute("next_hidden", "true");
		}

		//model.addAttribute("user", u);
		model.addAttribute("posts", posts);		
		return "blog";
		
	}
		
	
	
}
