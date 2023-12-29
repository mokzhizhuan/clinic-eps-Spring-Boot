package com.clinicapp.admin.users;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clinicapp.entity.Role;
import com.clinicapp.entity.User;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService 
{
	@Autowired
	private UserRepository Userrepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> ListAll()
	{
		return (List<User>)Userrepo.findAll();
	}
	
	public List<User> ListAllbyPatient()
	{
		return (List<User>)Userrepo.findByPatient("Patient");
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		
		if(isUpdatingUser)
		{
			User existingUser = Userrepo.findById(user.getId()).get();
			if (user.getPassword().isEmpty())
			{
				user.setPassword(existingUser.getPassword());
			}
			else
			{
				//encodePassword(user);
			}
		}
		else
		{
			//encodePassword(user);
		}
		
		//encodePassword(user);
		return Userrepo.save(user);
	}
	
	public User updateAccount(User userInForm)
	{
		User userinDB = Userrepo.findById(userInForm.getId()).get();
		
		if(!userInForm.getPassword().isEmpty())
		{
			userinDB.setPassword(userInForm.getPassword());
		}
		
		userinDB.setFirstName(userInForm.getFirstName());
		
		return Userrepo.save(userinDB);
	}

	/*private void encodePassword(User user)
	{
		String encodepassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodepassword);
	}*/

	public boolean isEmailUnique(Integer id, String email) {
		User userbyEmail = Userrepo.getUserByEmail(email);
		
		if (userbyEmail == null) return true;
		
		boolean isCreatingNew = (id ==null);
		
		if(isCreatingNew)
		{
			if(userbyEmail != null) return false;
			else
			{
				if(userbyEmail.getId() != id)
				{
					return false;
				}
			}
		}
		
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
		return Userrepo.findById(id).get();
		}
		catch(NoSuchElementException ex)
		{
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}
	
	public void delete(Integer id) throws UserNotFoundException
	{
		Long countbyID = Userrepo.countById(id);
		if(countbyID == null || countbyID == 0)
		{
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
		Userrepo.deleteById(id);
	}
	
	public void updateEnabledStatus(Integer id , boolean enabled)
	{
		Userrepo.updateEnabledStatus(id, enabled);
	}

	public User getbyEmail(String email) 
	{
		return Userrepo.getUserByEmail(email);
	}


	public boolean isEmailUniques(String email) {
		User patient = Userrepo.findByEmail(email);
		return patient == null;
	}

	public boolean isEmailValid(String email) {
		User userbyEmail = Userrepo.getUserByEmail(email);
		String pharaverfication = userbyEmail.getRole();
		System.out.print(pharaverfication);
		if (userbyEmail == null && pharaverfication != "Pharmacist")
		{
			return false;
		}
		
		
		return true;
	}
}
