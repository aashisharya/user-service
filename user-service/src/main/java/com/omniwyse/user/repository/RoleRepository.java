package com.omniwyse.user.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.omniwyse.user.bo.Role;
import com.omniwyse.user.bo.UserBO;

public interface RoleRepository extends JpaRepository<Role, Serializable>{
	Role findByRole(String role);
	
	@Modifying
	@Query(value = "insert into users_roles (user_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
	void insertInUsersRoles(long userId,long roleId);
	
	List<Role> findByUsers(UserBO user);
}
