package com.Negi.NomNomExpress.service;

import com.Negi.NomNomExpress.Dao.*;
import com.Negi.NomNomExpress.entity.UserEntity;


public interface UserService {
	
	public UserEntity registerUser(UserEntity user);
		
	public UserEntity updateUser(UpdateUserDao updateUserDao);
	
	public UserEntity updateRole(UpdateRoleDao updateRoleDao);

	public UserEntity getUser(long id);
	
}
