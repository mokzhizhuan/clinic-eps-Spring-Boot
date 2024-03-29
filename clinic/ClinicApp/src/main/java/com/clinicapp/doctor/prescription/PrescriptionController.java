package com.clinicapp.doctor.prescription;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicapp.ControllerHelper;
import com.clinicapp.Utility;
import com.clinicapp.admin.payment.PaymentService;
import com.clinicapp.admin.setting.EmailSettingBag;
import com.clinicapp.admin.setting.PaymentSettingBag;
import com.clinicapp.admin.setting.SettingService;
import com.clinicapp.admin.users.UserService;
import com.clinicapp.entity.Medicine;
import com.clinicapp.entity.Payment;
import com.clinicapp.entity.Prescription;
import com.clinicapp.entity.User;
import com.clinicapp.supplier.medicine.MedicineService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PrescriptionController {

	@Autowired
	private PrescriptionService service;
	
	@Autowired
	private MedicineService medservice;
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private ControllerHelper controllerHelper;
	
	@Autowired
	private PaymentService paymentService;
	
	
	@GetMapping("/prescription")
	public String listAll(Model model)
	{
		List<Prescription> listprescription = service.listAll();
		model.addAttribute("listprescriptions", listprescription);
		
		return "prescription";
	}
	
	@GetMapping("/prescription/new")
	public String newUser(Model model)
	{
		
		Prescription prescription = new Prescription();
		List<Medicine> listMedicines = medservice.listAll();
		List<User> listusers = userservice.ListAllbyPatient();
		model.addAttribute("prescription", prescription);
		model.addAttribute("listmedicine",listMedicines);
		model.addAttribute("listusers",listusers);
		model.addAttribute("pageTitle", "Create New Prescription");
		return "prescription_form";
	}
	
	@GetMapping("/prescription_history")
	public String prescription_history(Model model,HttpServletRequest request)
	{
		User patient = controllerHelper.getAuthenticatedCustomer(request);
		List<Prescription> listprescription = service.listPatientprescription(patient);
		model.addAttribute("listprescriptions", listprescription);
		return "prescription_history";
	}
	
	
	@GetMapping("/prescription_history/payment")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		User patient = controllerHelper.getAuthenticatedCustomer(request);
		
		
		List<Prescription> listprescription = service.listPatientprescription(patient);
		
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String paypalClientId = paymentSettings.getClientID();
		
		model.addAttribute("payment", new Payment());
		model.addAttribute("paypalClientId", paypalClientId);
		model.addAttribute("patient", patient);
		model.addAttribute("listprescriptions", listprescription);
		
		return "checkout";
	}
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) 
			throws UnsupportedEncodingException, MessagingException {
		String paymentType = request.getParameter("paymentMethod");
		System.out.print(paymentType);
		
		User patient = controllerHelper.getAuthenticatedCustomer(request);
		
			
		List<Prescription> cartItems = service.listPatientprescription(patient);
		service.deleteByPrescription(patient);
		sendOrderConfirmationEmail(request, cartItems, patient, paymentType);
		
		return "order_completed";
	}
	
	@PostMapping("/place_order_card")
	public String placeOrderCard(Model model , HttpServletRequest request , Payment payment) 
			throws UnsupportedEncodingException, MessagingException 
	{
		String paymentType = "CREDIT_CARD";
		
		User patient = controllerHelper.getAuthenticatedCustomer(request);
		
		
		List<Prescription> cartItems = service.listPatientprescription(patient);
		float totalcost = 0;
		for(Prescription pre : cartItems)
		{
			totalcost += pre.getTotalcost();
		}
		payment.setPrice(totalcost);
		service.deleteByPrescription(patient);
		sendOrderConfirmationEmail(request, cartItems, patient, paymentType);
		paymentService.save(payment);
		
		return "order_completed";
	}
	
	private void sendOrderConfirmationEmail(HttpServletRequest request, List<Prescription> order , User patient, String paymentType) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");
		
		String toAddress = patient.getEmail();
		String subject = emailSettings.getOrderConfirmationSubject();
		String content = emailSettings.getOrderConfirmationContent();
		
		subject = subject.concat("Order ID: " +  String.valueOf(patient.getId()));
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		float totalcost = 0;
		for(Prescription pre : order)
		{
			totalcost += pre.getTotalcost();
		}
		
		
		content = content.concat("<p>Name: " + patient.getFullName() + "<br/>");
		content = content.concat("ID: " + String.valueOf(patient.getId()) + "<br/>");
		content = content.concat("Total Amount : $" + String.format("%.2f", totalcost) + "<br/>");
		content = content.concat("Payment Method : " + paymentType  + "</p>");
		content = content.concat("</details>");
		
		helper.setText(content, true);
		mailSender.send(message);		
	}
	
	@PostMapping("/prescription/save")
	public String savedUser(Prescription prescription, RedirectAttributes redirectAttributes, HttpServletRequest request) 
			throws IOException, MessagingException
	{
		System.out.println(prescription);
		Set<Medicine> meds = prescription.getMedicine();
		double cost = (double)prescription.getCost();
		for(Medicine med : meds)
		{
			cost += cost + med.getCost();
		}
		double totalcost = cost;
		prescription.setTotalcost((float)totalcost);
		prescription.setVerfication(false);
		String randCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder random = new StringBuilder();
        Random rnd = new Random();
        while (random.length() < 30) { // length of the random string.
            int index = (int) (rnd.nextFloat() * randCHARS.length());
            random.append(randCHARS.charAt(index));
        }
        User patient = prescription.getPatient();
		String token = random.toString();
		prescription.setVerficationcode(token);
		prescription.setName(patient.getFullName());
		service.save(prescription);
		String email = request.getParameter("pharaemail");
		processRequestForm(request, email, redirectAttributes, token);
		redirectAttributes.addFlashAttribute("message", "The prescription has been saved successfully.");
		return "redirect:/prescription";
	}
	
	public void processRequestForm(HttpServletRequest request, String email, RedirectAttributes redirectAttributes, String token) 
			throws UnsupportedEncodingException, MessagingException {
		
		String link = Utility.getSiteURL(request) + "/prescription/verify?token=" + token;
		sendEmail(link, email);
	}
	
	private void sendEmail(String link, String email) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "Prescription verfication";
		String bufferimage = null; 
		try {
			bufferimage = generateQRCode(link);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String content = "<html><p>Hello," + email +"</p>"
				+ "<p>You have requested to verify the prescription.</p>"
				+ "Click the link below to verify the prescription:</p>"
				+ "<p><a href=\"" + link + "\">" + "<img src=\"cid:image1\" width=\"30%\" height=\"30%\"/>" + "</a></p>"
				+ "<br>"
				+ "<p>Thanks, "
				+ "Clinic Team.</p></html>";
		 // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");
 
        System.out.println(bufferimage);
        // creates multi-part
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		Map<String, String> inlineImages = new HashMap<String, String>();
		inlineImages.put("image1", bufferimage);
		 // adds inline image attachments
        if (inlineImages != null && inlineImages.size() > 0) {
            Set<String> setImageID = inlineImages.keySet();
             
            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                 
                String imageFilePath = inlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
 
                multipart.addBodyPart(imagePart);
            }
        }
    
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);		
		helper.setText(content, true);
		
		message.setContent(multipart);
		mailSender.send(message);
	}
	
	public static String generateQRCode(String link) throws WriterException, IOException {
        String qrCodePath = "C:\\Users\\Mok Zhi Zhuan\\Documents\\workspace-spring-tool-suite-4-4.19.1.RELEASE\\clinic\\ClinicApp\\src\\main\\resources\\static\\qrcode\\" ;
        String paths = link.replace("http://localhost:8080/clinic/prescription/verify?token=" , "");
        String qrCodeName = qrCodePath+paths+"-QRCODE.png";
        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(link, BarcodeFormat.QR_CODE, 400, 400);
        Path path = FileSystems.getDefault().getPath(qrCodeName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        
        return qrCodeName;
    }
	
	@GetMapping("/prescription/verify")
	public String verify(String token, Model model) {
		Prescription prescription = service.getByResetPasswordToken(token);
		if (prescription == null) {
			model.addAttribute("pageTitle", "Prescription");
			return "verify_fail";
		}
		prescription.setVerfication(true);
		prescription.setVerficationcode("");
		model.addAttribute("pageTitle", "Prescription");
		service.save(prescription);
		return "verify_success";
	}
}
