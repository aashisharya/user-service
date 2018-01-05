package com.omniwyse.user.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omniwyse.user.bo.RegisteredUserBO;
import com.omniwyse.user.bo.UserBO;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUserBO, Serializable>{
	RegisteredUserBO findByUser(UserBO user);
}
