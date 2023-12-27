package com.clinicapp.patient.register;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinicapp.admin.users.UserRepository;
import com.clinicapp.entity.User;



@Service
public class PatientService {

	
	@Autowired
	private UserRepository patientrepo;
	

	public boolean isEmailUnique(String email) {
		User patient = patientrepo.findByEmail(email);
		return patient == null;
	}
	
	public User getCustomerByEmail(String email) {
		return patientrepo.findByEmail(email);
	}
	
	@Transactional
	public boolean verify(String verificationCode) {
		User patient = patientrepo.findByVerficationCode(verificationCode);
		
		if (patient == null || patient.isEnabled()) 
		{
			return false;
		} else {
			patientrepo.enable(patient.getId());
			return true;
		}
	}
	
	public String updateResetPasswordToken(String email) throws PatientNotFoundException {
		User patient = patientrepo.findByEmail(email);
		if (patient != null) {
			String randCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	        StringBuilder random = new StringBuilder();
	        Random rnd = new Random();
	        while (random.length() < 30) { // length of the random string.
	            int index = (int) (rnd.nextFloat() * randCHARS.length());
	            random.append(randCHARS.charAt(index));
	        }
			String token = random.toString();
			//customer.setResetPasswordToken(token);
			patientrepo.save(patient);
			
			return token;
		} else {
			throw new PatientNotFoundException("Could not find any customer with the email " + email);
		}
	}	
	
	public User getByResetPasswordToken(String token) 
	{
		return patientrepo.findByResetPasswordToken(token);
	}
	
	public void updatePassword(String token, String newPassword) throws PatientNotFoundException {
		User patient = patientrepo.findByResetPasswordToken(token);
		if (patient == null) {
			throw new PatientNotFoundException("No customer found: invalid token");
		}
		
		patient.setPassword(newPassword);
		patient.setResetPasswordToken(null);
		
		patientrepo.save(patient);
	}
}
