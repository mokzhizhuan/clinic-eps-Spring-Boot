package com.clinicapp.admin.users;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.clinicapp.entity.User;

public interface UserRepository extends CrudRepository <User, Integer>{


	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);

	Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query( "SELECT u FROM User u WHERE u.role = ?1" )
	public List<User> findByPatient(String roles);

	@Query("SELECT u FROM User u WHERE u.email = ?1")
	public User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.verficationCode = ?1")
	public User findByVerficationCode(String code);

	public User findByResetPasswordToken(String token);

	@Query("UPDATE User u  SET u.enabled = true, u.verficationCode = null WHERE u.id = ?1")
	@Modifying
	public void enable(Integer id);
}
