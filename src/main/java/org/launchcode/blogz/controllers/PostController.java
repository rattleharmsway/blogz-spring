package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.PostDao;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {
	public static int pageEntries = 5;

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		if (title == "" || title == null){
			model.addAttribute("body", body);
			model.addAttribute("error", "You Need to submit a title");
			return "newpost";
		}	
		if (body == "" || body == null){
			model.addAttribute("value", title);
			model.addAttribute("error", "You Need to submit a body");
			return "newpost";
		}
		//if they validate, create a new post
		else{
			User u = getUserFromSession(request.getSession());
			Post p = new Post(title, body, u);
			postDao.save(p);
			model.addAttribute("user", u);
			model.addAttribute("post", p);
			//return "redirect:blog/newpost";
			int uid = p.getUid();
			
			return "redirect:/blog/"+ u.getUsername()+"/" + uid; // TODO - this redirect should go to the new post's page  
		}
		
		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		User u = userDao.findByUsername(username);
		
		Post p = postDao.findByUid(uid);
		

		model.addAttribute("user", u);
		model.addAttribute("post", p);
		
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userIndex(@PathVariable String username, Model model) {
				
		// TODO - implement userPosts
		User u = userDao.findByUsername(username);
		
		List<Post> posts = postDao.findByAuthor(u);
		//List<Post> posts = postDao.findAll();				
		if (posts.size()> pageEntries){
			posts = posts.subList(0, 5);
			model.addAttribute("next_hidden", "false");
			model.addAttribute("next_page", "/blog/archive/" + username + "/" + 1);
		}
		else{
			model.addAttribute("next_hidden", "true");
		}
		
		model.addAttribute("posts", posts);	
		model.addAttribute("prev_hidden", "true");		
		
		return "blog";
	}
	
	@RequestMapping(value = "/blog/archive/{username}/{pageNumber}", method = RequestMethod.GET)
	public String userPage(@PathVariable String username, @PathVariable int pageNumber, Model model) {
		User u = userDao.findByUsername(username);
		List<Post> posts = postDao.findByAuthor(u);
		
		if (pageNumber == 0){
			posts = posts.subList(0, posts.size()-1);
			model.addAttribute("prev_hidden", "true");
		}
		else{
			posts = posts.subList((pageNumber*pageEntries)-1, posts.size());
			model.addAttribute("prev_hidden", "false");
			model.addAttribute("prev_page", "/blog/archive/" + username + "/" + (pageNumber-1));
		}
		
		if (posts.size() >= pageEntries){
			
			posts = posts.subList(0, pageEntries);
			model.addAttribute("next_hidden", "false");
			model.addAttribute("next_page", "/blog/archive/" + username + "/"  + (pageNumber+1));
		}
		else{
			model.addAttribute("next_hidden", "true");
		}

		//model.addAttribute("user", u);
		model.addAttribute("posts", posts);		
		return "blog";
		
	}	
	
}
