package com.clinicapp.admin.users;


import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicapp.entity.Role;
import com.clinicapp.entity.User;


@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listAll(Model model)
	{
		List<User> listUsers = service.ListAll();
		model.addAttribute("listUsers", listUsers);
		
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model)
	{
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String savedUser(User user, RedirectAttributes redirectAttributes) throws IOException
	{
		System.out.println(user);
		Set<Role> roles = user.getRoles();
		String role = roles.toString();
		String strrole = role.replace("[", "");
		String strroles = strrole.replace("]", "");
		user.setRole(strroles.toString());
		service.save(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model,  RedirectAttributes redirectAttributes) 
	{
		try
		{	
			List<Role> listRoles = service.listRoles();
			User user = service.get(id);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User {ID : " + id +")");
			model.addAttribute("listRoles", listRoles);
			return "user_form";
		}
		catch(UserNotFoundException ex)
		{
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
		catch(NullPointerException ex)
		{
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) 
	{
		try
		{	
			List<Role> listRoles = service.listRoles();
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has deleted successfully");
			return "redirect:/users";
		}
		catch(UserNotFoundException ex)
		{
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled,
			RedirectAttributes redirectAttributes)
	{
		service.updateEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status + ".";
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/users";
	}
}
