package com.clinicapp.admin.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.clinicapp.entity.Role;
import com.clinicapp.entity.User;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void createuser()
	{
		Role roleAdmin = entityManager.find(Role.class, 1);
		User mok = new User("gamerdark44@outlook.com", "Kk126523$" , "Mok" , "Zhi Zhuan");
		mok.addRole(roleAdmin);
		mok.setEnabled(true);
		
		User saveduser = repo.save(mok);
		
		assertThat(saveduser.getId()).isGreaterThan(0);	
	}
	
	@Test
	public void createuserwithtworoles()
	{
		User Ravi = new User("ravi@gmail.com", "ravi2020" , "Ravi",  "Kumar");
		Role roleEditor = new Role();
		roleEditor.setId(3);
		Role roleAssistant = new Role();
		roleAssistant.setId(5);
		
		Ravi.addRole(roleEditor);
		Ravi.addRole(roleAssistant);
		Ravi.setEnabled(true);
		
		User saveduser = repo.save(Ravi);
		
		assertThat(saveduser.getId()).isGreaterThan(0);	
	}
	
	@Test
	public void testListAllusers()
	{
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userNam = repo.findById(3).get();
		System.out.println(userNam);
		assertThat(userNam).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userNam = repo.findById(3).get();
		userNam.setEnabled(true);
		userNam.setEmail("namjavaprogrammer@gmail.com");
		
		repo.save(userNam);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userRavi = repo.findById(4).get();
		Role roleEditor = new Role();
		roleEditor.setId(2);
		Role roleSalesperson = new Role();
		roleEditor.setId(5);
		
		userRavi.getRoles().remove(roleEditor);
		userRavi.addRole(roleSalesperson);
		
		repo.save(userRavi);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 4;
		repo.deleteById(userId);
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "ravi@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	/*@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
		
	}*/	
	
	
}
