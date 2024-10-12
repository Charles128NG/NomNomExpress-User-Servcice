package com.Negi.NomNomExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Negi.NomNomExpress.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	public Role findByRoleName(String roleName);
}
