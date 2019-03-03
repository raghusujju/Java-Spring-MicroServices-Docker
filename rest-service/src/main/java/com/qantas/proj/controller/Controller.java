package com.qantas.proj.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.qantas.proj.controller.exceptions.ServiceException;
import com.qantas.proj.pojos.User;
import com.qantas.proj.serviceinterface.BOServiceInterface;



@RestController
@RequestMapping("/qantas/userapp")
public class Controller {
	
	Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	RestTemplate restTemplate;
	@Value("${dbServiceUrl}")
	private String dbServiceUrl;
	@Autowired
	BOServiceInterface serviceBo;
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserDetails(@PathVariable("userId") String userId) throws ServiceException {
		User user = serviceBo.getUser(userId);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@GetMapping("/user/")
	public ResponseEntity<Collection<User>> getAllUsers() throws ServiceException {
		Collection<User> allUsers = serviceBo.getAllUsers();
		return new ResponseEntity<Collection<User>>(allUsers,HttpStatus.OK);
	}
	
	@PostMapping("/user") 
	public ResponseEntity<User> createUser(@RequestBody User user) throws ServiceException, Exception{
		User createUser = serviceBo.createUser(user);
		return new ResponseEntity<User>(createUser,HttpStatus.OK);
	}
	
	
	@PutMapping("/user/{userId}") 
	public ResponseEntity<User> updateUser(@PathVariable("userId") String userId,@RequestBody User user) throws ServiceException, Exception{
		user.setUserId(userId);
		User createUser = serviceBo.updateUser(user);
		return new ResponseEntity<User>(createUser,HttpStatus.OK);
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) throws ServiceException, Exception{
		serviceBo.deleteUser(userId);
		return new ResponseEntity<String>("User deleted successfully",HttpStatus.OK);
	}
}
