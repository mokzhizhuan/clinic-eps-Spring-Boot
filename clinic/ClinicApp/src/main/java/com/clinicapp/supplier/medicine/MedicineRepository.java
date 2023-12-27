package com.clinicapp.supplier.medicine;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.clinicapp.entity.Medicine;

public interface MedicineRepository extends CrudRepository<Medicine, Integer> {

	Long countById(Integer id);
	
	
}
