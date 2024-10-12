package com.Negi.NomNomExpress.service;

import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Negi.NomNomExpress.Dao.LoginDao;
import com.Negi.NomNomExpress.Dao.UpdateRoleDao;
import com.Negi.NomNomExpress.Dao.UpdateUserDao;
import com.Negi.NomNomExpress.entity.Role;
import com.Negi.NomNomExpress.entity.UserEntity;
import com.Negi.NomNomExpress.exceptions.RESTException;
import com.Negi.NomNomExpress.repository.RoleRepository;
import com.Negi.NomNomExpress.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public UserEntity registerUser(UserEntity user) {
		try {
			if(userRepo.findByUserEmail(user.getUserEmail()) !=null) {
				throw new RESTException("Email already registered with us", HttpStatus.BAD_REQUEST);
			}
			if(userRepo.findByUserName(user.getUserName()).isPresent()) {
				throw new RESTException("User Name already registered with us", HttpStatus.BAD_REQUEST);
			}
			user.setPassword(encoder.encode(user.getPassword()));
			
			Role role = roleRepo.findByRoleName("ROLE_USER");
			
			user.setRoles(new HashSet<>(Collections.singletonList(role)));
			
			UserEntity savedUser = userRepo.save(user);
			return savedUser;
		}catch(Exception e) {
			throw new RESTException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@Override
	public UserEntity getUser(long id) {
		return userRepo.findById(id).get();
	}
	@Override
	public UserEntity updateUser(UpdateUserDao updateUserDao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserEntity updateRole(UpdateRoleDao updateRoleDao) {
		UserEntity user = null;
		
		if(!userRepo.findById(updateRoleDao.getUserId()).isPresent()){
			throw new RESTException("UserID not found", HttpStatus.BAD_REQUEST);
		}
		else {
			user = userRepo.findById(updateRoleDao.getUserId()).get();
		}
		
		Role role = roleRepo.findByRoleName(updateRoleDao.getRoleName());
		if(role == null) {
			throw new RESTException("UserID not found", HttpStatus.BAD_REQUEST);
		}
		else {
			user.getRoles().add(role);
			user = userRepo.save(user);
		}
		return user;
	}

}
