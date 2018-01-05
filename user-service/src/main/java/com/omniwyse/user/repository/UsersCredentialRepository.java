package com.omniwyse.user.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omniwyse.user.bo.UserBO;
import com.omniwyse.user.bo.UsersCredential;


public interface UsersCredentialRepository extends JpaRepository<UsersCredential, Serializable>{
	public UsersCredential findByUser(UserBO user);
	public UsersCredential findByEmail(String email);
}
