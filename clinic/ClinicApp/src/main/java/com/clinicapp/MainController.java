package com.clinicapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.clinicapp.entity.User;


@Controller
public class MainController 
{
	@GetMapping
	public String viewhomepage()
	{
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginpage()
	{
		return "login";
	}

}
