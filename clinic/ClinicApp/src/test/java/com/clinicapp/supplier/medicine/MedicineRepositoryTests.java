package com.clinicapp.supplier.medicine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.clinicapp.entity.Medicine;




@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class MedicineRepositoryTests {

	@Autowired
	private MedicineRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void createmedicine()
	{
		Medicine med = new Medicine("Eye Drops", "Eye Drops", 40.00);
		
		Medicine savedmed = repo.save(med);
		
		assertThat(savedmed.getId()).isGreaterThan(0);	
	}
	
	
	@Test
	public void testListAllusers()
	{
		Iterable<Medicine> listMedicine = repo.findAll();
		listMedicine.forEach(Medicine -> System.out.println(Medicine));
	}
	
	@Test
	public void testGetMedicineById() {
		Medicine Medicinem = repo.findById(3).get();
		System.out.println(Medicinem);
		assertThat(Medicinem).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		Medicine Medicinem = repo.findById(3).get();
		Medicinem.setDescription("headache");;
		
		repo.save(Medicinem);
	}
	
	
	
	@Test
	public void testDeleteUser() {
		Integer MedicineId = 4;
		repo.deleteById(MedicineId);
		
	}
	
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}	
}
