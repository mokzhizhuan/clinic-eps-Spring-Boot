package com.clinicapp.doctor.prescription;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.clinicapp.entity.Prescription;

public interface PrescriptionRepository extends CrudRepository<Prescription, Integer> {

	Long countById(Integer id);

	@Query("SELECT p FROM Prescription p WHERE p.verficationcode = ?1")
	public Prescription findByVerficationCode(String code);
	
	@Query("UPDATE Prescription p SET p.verfication = true, p.verficationcode = null WHERE p.id = ?1")
	@Modifying
	public void enable(Integer id);
	
	@Query("SELECT p FROM Prescription p WHERE p.verfication = true AND p.name = ?1")
	public List<Prescription> findbyName(String name);

	@Modifying
	@Query("DELETE Prescription p WHERE p.patient.id = ?1")
	public void deleteByPatient(Integer id); 
}
