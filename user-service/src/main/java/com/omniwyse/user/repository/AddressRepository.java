package com.omniwyse.user.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omniwyse.user.bo.AddressBO;

public interface AddressRepository extends JpaRepository<AddressBO, Serializable>{

}
