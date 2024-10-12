package com.Negi.NomNomExpress.security;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.Negi.NomNomExpress.entity.*;
import com.Negi.NomNomExpress.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepo.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException(username+ " not found"));
		return new User(user.getUserName(),user.getPassword(), getGrantedAuthorities(user.getRoles()));
	}
	
	private Collection<GrantedAuthority> getGrantedAuthorities(Set<Role> roles) {
		return roles.stream().map((role) -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
	}
}
