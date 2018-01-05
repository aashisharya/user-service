package com.omniwyse.user.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.omniwyse.user.bo.AddressBO;
import com.omniwyse.user.bo.LibraryUserBO;
import com.omniwyse.user.bo.RegisteredUserBO;
import com.omniwyse.user.bo.Role;
import com.omniwyse.user.bo.UserBO;
import com.omniwyse.user.bo.UsersCredential;
import com.omniwyse.user.exception.UserServiceException;
import com.omniwyse.user.repository.AddressRepository;
import com.omniwyse.user.repository.LibraryUserRepository;
import com.omniwyse.user.repository.RegisteredUserRepository;
import com.omniwyse.user.repository.RoleRepository;
import com.omniwyse.user.repository.StatusRepository;
import com.omniwyse.user.repository.UserRepository;
import com.omniwyse.user.repository.UsersCredentialRepository;
import com.omniwyse.user.util.PEncriptionDecriptin;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UsersCredentialRepository usersCredentialRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	RegisteredUserRepository registeredUserRepository;
	
	@Autowired
	LibraryUserRepository libraryUserRepository;

	private static final Logger logger = Logger.getLogger(UserService.class);

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
	public Object[] getLoginDetailsByUserName(String email) throws UsernameNotFoundException {
		logger.info("loading user for username :: " + email);
		UserBO user = userRepository.findByEmail(email);
		if (user == null ) {
			logger.info("No Record available for username : " + email );
			throw new UsernameNotFoundException(String.format("User with username=%s was not found", email));
		}
		UsersCredential credential = usersCredentialRepository.findByUser(user);
		if(!credential.getStatus().getStatus().equalsIgnoreCase("Active")) {
			logger.info("User is not Active : " + email );
			throw new UsernameNotFoundException(String.format("User with username=%s was not active", email));
		}
		Set<String> roles = new HashSet<>();
		for (Role role : user.getRoles()) {
			logger.info("adding role to user : " + role.getRole());
			roles.add(role.getRole());
		}

		return new Object[] { user.getEmail(), PEncriptionDecriptin.decrypt(credential.getPassword()), new ArrayList<>(roles) };
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public UserBO registerUser(UserBO userBO,AddressBO addressBO, Long libraryId, List<String> roles, String password) throws UserServiceException {
		UserBO savedUser = null;
		UsersCredential userCredentials = null;
		List<Role> rolesBO = new ArrayList<Role>();
		try {
			for (String role : roles) {
				rolesBO.add(roleRepository.findByRole(role));
			}
			userBO.setRoles(rolesBO);
			savedUser = userRepository.save(userBO);
			addressBO.setUser(savedUser);
			addressRepository.save(addressBO);
			userCredentials = new UsersCredential();
			userCredentials.setUser(savedUser);
			userCredentials.setEmail(savedUser.getEmail());
			userCredentials.setPassword(PEncriptionDecriptin.encrypt(password));
			userCredentials.setStatus(statusRepository.findByStatus("Active"));
			usersCredentialRepository.save(userCredentials);
			registeredUserRepository.save(new RegisteredUserBO(0.0, 0.0, savedUser));
			if(libraryId != null)
				libraryUserRepository.save(new LibraryUserBO(libraryId,savedUser));
			else
				libraryUserRepository.save(new LibraryUserBO(savedUser));
		} catch (Exception ex) {
			logger.info("invalid details : "+ex);
			throw new UserServiceException("invalid Details...", ex);
		}
		return savedUser;
	}
	
	@Transactional(propagation =  Propagation.REQUIRED,rollbackFor = Exception.class,readOnly = false)
	public boolean addRole(String username,String rolename) throws UserServiceException {
		UserBO user = null;
		Role role =  null;
		try {
			user =  userRepository.findByEmail(username);
			role = roleRepository.findByRole(rolename);
			roleRepository.insertInUsersRoles(user.getId(), role.getId());
		}catch(Exception ex) {
			logger.info("Exception while inserting role in users_roles "+ex);
			throw new UserServiceException("Exception while inserting role in users_roles",ex);
		}
		return true;
	}
	
	@Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class,readOnly = false)
	public UserBO updateUser(UserBO userBO) throws UserServiceException {
		UserBO user = null;
		try {
			userBO.setRoles(roleRepository.findByUsers(userBO));
			user = userRepository.save(userBO);
		}catch(Exception ex) {
			logger.info("Exception while updating user" + ex);
			throw new UserServiceException("Exception while updating user",ex);
		}
		return user;
	}
	
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor= Exception.class,readOnly = false)
	public AddressBO updateAddress(AddressBO addressBO) throws UserServiceException {
		AddressBO address =  null;
		try {
			addressBO.setUser(addressRepository.findOne(addressBO.getId()).getUser());
			address = addressRepository.save(addressBO);
		}catch(Exception ex) {
			logger.info("Exception while updating address"+ex);
			throw new UserServiceException("Exception while updating address",ex);
		}
		return address;
	}
	
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor= Exception.class,readOnly = false)
	public UsersCredential updateCredential(String username,String password) throws UserServiceException {
		UsersCredential userCredential = null;
		try {
			userCredential = usersCredentialRepository.findByEmail(username);
			userCredential.setPassword(PEncriptionDecriptin.encrypt(password));
			userCredential =  usersCredentialRepository.save(userCredential);
		}catch(Exception ex) {
			logger.info("Exception while updating user credential" + ex);
			throw new UserServiceException("Exception while updating user credential",ex);
		}
		return userCredential;
	}
	
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor= Exception.class,readOnly = true)
	public List<UserBO> getUsersByRole(String roleName) throws UserServiceException{
		List<UserBO> users = null;
		try {
			Role role = roleRepository.findByRole(roleName);
			users = userRepository.getByRoles(role);
		}catch(Exception ex) {
			logger.info("Exception while getting list of users by role" + ex);
			throw new UserServiceException("Exception while getting list of users by role",ex);
		}
		return users;
	}
	
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor= Exception.class,readOnly = false)
	public Boolean updateUserPaymentDetails(String username, Double dueAmount,Double paidAmount) throws UserServiceException{
		UserBO user = userRepository.findByEmail(username);
		RegisteredUserBO savedUser =  registeredUserRepository.findByUser(user);
		Double due = savedUser.getDueAmount();
		Double paid = savedUser.getPaidAmount();
		savedUser.setDueAmount(due + dueAmount);
		savedUser.setPaidAmount(paid + paidAmount);
		registeredUserRepository.save(savedUser);
		return true;
	}
	
}
