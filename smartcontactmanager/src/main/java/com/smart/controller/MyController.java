package com.smart.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;      

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;


 

// 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
//<!-- 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 -->
@Controller
public class MyController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	    private UserRepository userRepository;
		@GetMapping("/")   
		public String home(Model model)
		{
			model.addAttribute("title","This is home");
			return "home";
		}


		@GetMapping("/about")   
		public String about(Model model)
		{
		model.addAttribute("title","This is about");
		return "about";
		} 
		
          //Register User 
		
		@GetMapping("/signup")   
		public String signup(Model model)
		{
		model.addAttribute("title","This is signup Page");
		model.addAttribute("user",new User());
		return "signup";
		} 
	//registering user
		@PostMapping("/do_register")
		public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value = "agreement",defaultValue = "false")boolean agreement,
				                                   Model model,HttpSession session)
		{ 
			
			try {
				if(!agreement)
				{
					System.out.println("You have not agreed the term conditions");
					throw new Exception("You have not agreed the term conditions");
				}
				if(result.hasErrors())
				{   
					System.out.println(result.hasErrors());
					System.out.println("Error"+result.toString());
					model.addAttribute("user",user);
					return "signup";
				}
				System.out.println(user);
				System.out.println(agreement);
				 user.setRole("ROLE_USER");
				user.setEnabled(true);
				user.setImageUrl("membership.png");
				user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
				User save = this.userRepository.save(user);
				System.out.println(save);
				model.addAttribute("user",new User());
				session.setAttribute("message",new Message("Successfully register!","alert-Success"));
//				if(session.isNew())
//				{
//					session.removeAttribute("message");
//				}
				return "signup"; 
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
			
				e.printStackTrace();
				model.addAttribute("user",user);
				session.setAttribute("message",new Message("Something Went Wrong !!"+e.getMessage(),"alert-error"));
				session.setAttribute("emailmsg",new Message("must be a well-formed email address!!"+e.getMessage(),"alert-error"));
			
//				model.addAttribute("shd",);
				
				
				return "signup";
			}
			
		}

       //Handler for Custome Login
		@GetMapping("/signin")
		public String login(Model model)
		{
			model.addAttribute("title","Login Page");
			return "login";
		}
}
