package com.clinicapp.doctor.prescription;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicapp.entity.Prescription;
import com.clinicapp.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrescriptionService {

	@Autowired
	private PrescriptionRepository repo;
	
	public List<Prescription> listAll()
	{
		return (List<Prescription>)repo.findAll();
	}
	
	public Prescription save(Prescription prescription)
	{
		return repo.save(prescription);
	}
	
	public Prescription getByResetPasswordToken(String token) 
	{
		return repo.findByVerficationCode(token);
	}
	
	public Prescription get(Integer id) throws PrescriptionNotFoundException {
		try {
		return repo.findById(id).get();
		}
		catch(NoSuchElementException ex)
		{
			throw new PrescriptionNotFoundException("Could not find any user with ID " + id);
		}
	}
	
	public void delete(Integer id) throws PrescriptionNotFoundException
	{
		Long countbyID = repo.countById(id);
		if(countbyID == null || countbyID == 0)
		{
			throw new PrescriptionNotFoundException("Could not find any user with ID " + id);
		}
		
		repo.deleteById(id);
	}

	public List<Prescription> listPatientprescription(User patient) {
		// TODO Auto-generated method stub
		return repo.findbyName(patient.getFullName());
	}

	public void deleteByPrescription(User patient) {
		// TODO Auto-generated method stub
		repo.deleteByPatient(patient.getId());
	}
}
