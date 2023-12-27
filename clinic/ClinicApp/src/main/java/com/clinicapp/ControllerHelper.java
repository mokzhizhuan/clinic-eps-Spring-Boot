package com.clinicapp;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.clinicapp.admin.users.UserService;
import com.clinicapp.entity.User;


@Component
public class ControllerHelper {
	@Autowired 
	private UserService patientService;
	
	public User getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);				
		return patientService.getbyEmail(email);
	}
}
