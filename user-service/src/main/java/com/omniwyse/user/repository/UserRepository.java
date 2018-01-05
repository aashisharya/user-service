package com.omniwyse.user.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omniwyse.user.bo.Role;
import com.omniwyse.user.bo.UserBO;

public interface UserRepository extends JpaRepository<UserBO, Serializable>{
	UserBO findByEmail(String email);
	List<UserBO> getByRoles(Role role);
}
