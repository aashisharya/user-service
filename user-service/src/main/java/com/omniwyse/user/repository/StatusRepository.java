package com.omniwyse.user.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omniwyse.user.bo.UserStatusBO;

public interface StatusRepository extends JpaRepository<UserStatusBO, Serializable>{
	UserStatusBO findByStatus(String status);
}
