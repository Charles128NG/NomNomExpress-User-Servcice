package com.Negi.NomNomExpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Negi.NomNomExpress.Dao.AuthResponse;
import com.Negi.NomNomExpress.Dao.LoginDao;
import com.Negi.NomNomExpress.Dao.TestPojo;
import com.Negi.NomNomExpress.Dao.UpdateRoleDao;
import com.Negi.NomNomExpress.entity.UserEntity;
import com.Negi.NomNomExpress.exceptions.RESTException;
import com.Negi.NomNomExpress.security.JWTUtil;
import com.Negi.NomNomExpress.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager autenticationManager;
	
	@Autowired
	private JWTUtil jwtUtil;
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserEntity user) throws RESTException{
		if(user.getUserName() == null) {
			throw new RESTException("username can't be null",HttpStatus.BAD_REQUEST);
		}
		if(user.getPassword() == null) {
			throw new RESTException("password can't be null",HttpStatus.BAD_REQUEST);
		}
		if(user.getUserEmail() == null) {
			throw new RESTException("email address can't be null",HttpStatus.BAD_REQUEST);
		}
		try {
			userService.registerUser(user);
			return new ResponseEntity<>("User registered successfuly", HttpStatus.OK);
		}catch(Exception e) {
			throw new RESTException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDao loginDao) throws RESTException{
		
		try {
			Authentication authentication = autenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDao.getUserName(), loginDao.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtUtil.generateToken(authentication);
			return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RESTException(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/test")
	public ResponseEntity<?> test(@RequestBody TestPojo a){
		System.out.println(userService.getUser(1l));
		return new ResponseEntity<>(a.getNum()*a.getNum(), HttpStatus.OK);
	}
	
	@PostMapping("/updateRole")
	public ResponseEntity<?>updateRole(@RequestBody UpdateRoleDao updateRoleDao){
		try {
			UserEntity updatedUser = userService.updateRole(updateRoleDao);
			
			return new ResponseEntity<>(updatedUser ,HttpStatus.OK);
		}
		catch(Exception e) {
			throw new RESTException("Error Updating Role",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
