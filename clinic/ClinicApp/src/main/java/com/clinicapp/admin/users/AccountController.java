package com.clinicapp.admin.users;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicapp.entity.User;



@Controller
public class AccountController {

	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal UserDetails loggedUser, Model model)
	{
		String email = loggedUser.getUsername();
		User user = service.getbyEmail(email);
		model.addAttribute("user", user);
		
		
		return "account_form";
	}
	
	@PostMapping("/account/update")
	public String savedUser(User user, RedirectAttributes redirectAttributes) 
			throws IOException
	{
		System.out.println(user);
		service.updateAccount(user);
		
		redirectAttributes.addFlashAttribute("message", "Your account details has been updated.");
		return "redirect:/account";
	}
}
