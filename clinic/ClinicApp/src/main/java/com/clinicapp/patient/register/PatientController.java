package com.clinicapp.patient.register;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.clinicapp.Utility;
import com.clinicapp.admin.setting.EmailSettingBag;
import com.clinicapp.admin.setting.SettingService;
import com.clinicapp.admin.users.UserService;
import com.clinicapp.entity.Role;
import com.clinicapp.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PatientController {

	@Autowired 
	private UserService service;
	
	@Autowired 
	private PatientService patientservice;
	
	@Autowired 
	private SettingService settingService;
	
	@GetMapping("/register")
	public String viewRegisterpage(Model model)
	{
		User user = new User();
		model.addAttribute("users", user);
		return "register_form";
	}
	
	@PostMapping("/create_patient")
	public String createCustomer(User patient, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException 
	{
		Role role = new Role();
		role.setId(5);
		patient.addRole(role);
		patient.setRole("Patient");
		patient.setEnabled(false);
		String randCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder random = new StringBuilder();
        Random rnd = new Random();
        while (random.length() < 30) { // length of the random string.
            int index = (int) (rnd.nextFloat() * randCHARS.length());
            random.append(randCHARS.charAt(index));
        }
		String token = random.toString();
		patient.setVerficationCode(token);
		service.save(patient);
		sendVerificationEmail(request, patient);
		model.addAttribute("pageTitle", "Registration Succeeded!");
		
		return "register_success";
	}
	
	private void sendVerificationEmail(HttpServletRequest request, User patient) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = patient.getEmail();
		String subject = emailSettings.getPatientVerifySubject();
		String content = emailSettings.getPatientVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.concat(patient.getFullName() + "<br/><br/>");
		
		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + patient.getVerficationCode();
		
		content = content.concat(verifyURL + "<br/><br/>");
		
		content = content.concat("<br/><br/>" + "Thank you.<br/>" + "Shopme Team");
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL: " + verifyURL);
	}
	
	@GetMapping("/verify")
	public String verifyAccount(String code, Model model) 
	{
		boolean verified = patientservice.verify(code);
		model.addAttribute("pageTitle", "Patient");
		return (verified ? "verify_success" : "verify_fail");
	}
}
