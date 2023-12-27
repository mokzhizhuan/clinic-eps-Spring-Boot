package com.clinicapp.supplier.medicine;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicapp.entity.Medicine;
import com.clinicapp.entity.Prescription;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MedicineService {

	@Autowired
	private MedicineRepository repo;
	
	public List<Medicine> listAll()
	{
		return (List<Medicine>)repo.findAll();
	}
	
	public Medicine save(Medicine medicine)
	{
		return repo.save(medicine);
	}
	
	public Medicine get(Integer id) throws MedicineNotFoundException {
		try {
		return repo.findById(id).get();
		}
		catch(NoSuchElementException ex)
		{
			throw new MedicineNotFoundException("Could not find any user with ID " + id);
		}
	}
	
	public void delete(Integer id) throws MedicineNotFoundException
	{
		Long countbyID = repo.countById(id);
		if(countbyID == null || countbyID == 0)
		{
			throw new MedicineNotFoundException("Could not find any user with ID " + id);
		}
		
		repo.deleteById(id);
	}
}
