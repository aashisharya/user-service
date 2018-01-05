package com.omniwyse.user.resource;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniwyse.user.bo.AddressBO;
import com.omniwyse.user.bo.UserBO;
import com.omniwyse.user.bo.UsersCredential;
import com.omniwyse.user.exception.UserServiceException;
import com.omniwyse.user.service.UserService;


@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/user-details", method = RequestMethod.POST)
	public ResponseEntity<Object[]> getLoginDetailsByUserName(@RequestHeader(value="username") String username) {
		logger.info("username" +username);
		return new ResponseEntity<Object[]>(userService.getLoginDetailsByUserName(username), HttpStatus.OK);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserBO> registerUser(@RequestBody String userDetails) {
		UserBO user =  null;
		ObjectMapper mapper = null;
		JsonNode node = null;
		try {
			mapper  = new ObjectMapper();
			try {
				node = mapper.readTree(userDetails);
			} catch (IOException e) {
				logger.info("Exception while parsing json object : "+e);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			UserBO userBO = mapper.convertValue(node.get("userBO"), UserBO.class);
			AddressBO addressBO =  mapper.convertValue(node.get("addressBO"), AddressBO.class);
			Long libraryId = mapper.convertValue(node.get("libraryId"), Long.class);
			String password = mapper.convertValue(node.get("password"), String.class);
			@SuppressWarnings("unchecked")
			List<String> roles =  mapper.convertValue(node.get("roles"), List.class);
			user = userService.registerUser(userBO,addressBO,libraryId,roles,password);
			user.setRoles(null);
		}catch(UserServiceException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@PutMapping("/add-role/{role}")
	public ResponseEntity<?> addRole(@RequestHeader String username,@PathVariable String role){
		boolean result = false;
		try {
			result = userService.addRole(username, role);
		}catch(UserServiceException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Boolean>(result,HttpStatus.OK);
	}
	
	@PutMapping("/update-user")
	public ResponseEntity<?> updateUser(@RequestBody UserBO userBO){
		UserBO user =  null;
		try {
			user = userService.updateUser(userBO);
		}catch(UserServiceException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		user.setRoles(null);
		return new ResponseEntity<UserBO>(user,HttpStatus.OK);
	}
	
	@PutMapping("/update-address")
	public ResponseEntity<?> updateAddress(@RequestBody AddressBO addressBO){
		AddressBO address = null;
		try {
			address = userService.updateAddress(addressBO);
		}catch(UserServiceException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		address.setUser(null);
		return new ResponseEntity<AddressBO>(address,HttpStatus.OK);
	}
	
	@PutMapping("/update-credential")
	public ResponseEntity<?> updateCredetial(@RequestHeader("username") String username,@RequestBody String password){
		UsersCredential usersCredential = null;
		
		try {
			usersCredential = userService.updateCredential(username, password);
		}catch(UserServiceException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		usersCredential.setUser(null);
		return new ResponseEntity<>(usersCredential,HttpStatus.OK);
	}
	
	@GetMapping("/get-user/{rolename}")
	public ResponseEntity<?> getUsersByRole(@PathVariable String rolename){
		List<UserBO> users = null;
		try {
			users = userService.getUsersByRole(rolename);
		}catch(UserServiceException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(users,HttpStatus.OK);
	}
	
	@PutMapping("/update-payment/{dueAmount}/{paidAmount}")
	public ResponseEntity<?> updateUserPaymentDetails(@RequestHeader String username,@PathVariable Double dueAmount,@PathVariable Double paidAmount){
		Boolean  flag =  false;
		try {
			flag = userService.updateUserPaymentDetails(username,dueAmount,paidAmount);
		}catch(UserServiceException ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(flag,HttpStatus.OK);
	}
	
	@GetMapping("/accessDenied")
	public String accessDeniedTest() {
		return "from user service Sorry you dont have permission to this link...";
	}
}
