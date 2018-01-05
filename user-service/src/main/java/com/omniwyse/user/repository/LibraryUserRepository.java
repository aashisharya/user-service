package com.omniwyse.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omniwyse.user.bo.LibraryUserBO;

public interface LibraryUserRepository extends JpaRepository<LibraryUserBO, Long>{

}
