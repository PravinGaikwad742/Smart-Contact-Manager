package com.smart.controller;

import java.io.File; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository repository;
	//method for adding common data to responce
	@ModelAttribute
	public void AddCommonData(Model model,Principal principal) {
		String userName=principal.getName();
		System.out.println("UsserName="+userName);
		User user = repository.getUserByUserName(userName);
		System.out.println("User="+user);
		model.addAttribute("user",user);
		
	}
	//dashboard Home
	@RequestMapping("/index")
	public String userdashboard(Model model,Principal principal)
	{
		model.addAttribute("title","User Dashboard");
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			   @RequestParam("profileImage") MultipartFile file,
			   Principal principal,
			   HttpSession session)
	{
		
		try {
			String name = principal.getName();
			User user = this.repository.getUserByUserName(name);
			
			if(file.isEmpty())
			{
				//if file is empty 
				System.out.println("Empty image");
			}
			else
			{
//				file the file to folder and update the name to contct
				contact.setImage(file.getOriginalFilename());
				File saveFile=new ClassPathResource("static/image").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				System.out.println("Path="+path);
				Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is Uppload.........");
				
			}
			
			user.getContacts().add(contact);
			contact.setUser(user);
			this.repository.save(user);
			System.out.println("Contact Data"+contact);
			System.out.println("Added to database.....");
			
			//Message sucess
			session.setAttribute("message", new Message("Your Contact is Added !! Add More..","success"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Some went Wrong !! Try Again..","danger"));
		}
		
		return "normal/add_contact_form";
	}
	//show contact Handler
	@GetMapping("/showcontact")
	public String showContacts() {
		return "";
		}
	
}
