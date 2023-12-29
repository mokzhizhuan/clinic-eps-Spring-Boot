package com.clinicapp.doctor.prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicapp.admin.users.UserService;

@RestController
public class PrescriptionRestController {

	@Autowired
	private UserService service;
	
	@PostMapping("/prescription/check_email")
	public String checkEmailValid(@Param("email") String email)
	{
		return service.isEmailValid(email) ? "OK" : "Duplicated";
	}
	
}
