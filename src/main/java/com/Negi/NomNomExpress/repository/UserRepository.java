package com.Negi.NomNomExpress.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Negi.NomNomExpress.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long>{

	
	//find if user exists
	public Optional<UserEntity> findByUserName(String userName);
	//find if email is used
	public UserEntity findByUserEmail(String userEmail);
	
	//create mew user will be done by save()
	
}
