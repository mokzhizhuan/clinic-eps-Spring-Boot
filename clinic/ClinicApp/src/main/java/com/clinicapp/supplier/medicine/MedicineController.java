package com.clinicapp.supplier.medicine;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicapp.entity.Medicine;
import com.clinicapp.entity.User;

@Controller
public class MedicineController {

	@Autowired
	private MedicineService service;

	
	@GetMapping("/medicine")
	public String listAll(Model model)
	{
		List<Medicine> listmedicine = service.listAll();
		model.addAttribute("listmedicines", listmedicine);
		
		return "medicine";
	}
	
	@GetMapping("/medicine/new")
	public String newUser(Model model)
	{
		
		Medicine medicine = new Medicine();
		model.addAttribute("medicine", medicine);
		model.addAttribute("pageTitle", "Create New Medicine");
		return "medicine_form";
	}
	
	@PostMapping("/medicine/save")
	public String savedUser(Medicine medicine, RedirectAttributes redirectAttributes) throws IOException
	{
		System.out.println(medicine);
		
		service.save(medicine);
		
		redirectAttributes.addFlashAttribute("message", "The medicine has been saved successfully.");
		return "redirect:/medicine";
	}
}
