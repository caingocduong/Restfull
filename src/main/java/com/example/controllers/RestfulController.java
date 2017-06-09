package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.User;
import com.example.services.UserServiceImpl;

@RestController
public class RestfulController{
	@Autowired
	private UserServiceImpl userService;
	
	public  RestfulController(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	public RestfulController() {}
	
	@GetMapping(value = "/")
	public String hello(){
		return "Restful API using Spring";
	}
	
	@PostMapping(value = "/rest/adduser", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody User user){
		if(!userService.isExistUser(user.getId())){
			userService.addUser(user);
			user.setDescription("Create succesfully");
			
			return new ResponseEntity<User>(user,HttpStatus.CREATED);
		} else {
			user.setDescription("ID existed!");
			
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping(value="/rest/users", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> readUser(){
		//Logger log = LogManager.getLogger(RestfulController.class);
		List<User> users = userService.retreiveUsers();
		if(users.isEmpty()){
			
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@PutMapping(value = "/rest/updateuser/{id}", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@PathVariable("id") int id,@RequestBody User user){
		if(!userService.isExistUser(user.getId())){
			user.setId(id);
			user.setDescription("User does not exist.");
			
			return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
		} else {
			user.setId(id);
			userService.updateUser(user);
			user.setDescription("Updated successfully!");
			
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}
	
	@DeleteMapping(value = "/rest/delete/{id}", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> deleteUser(@PathVariable("id") int id){
		if(!userService.isExistUser(id)){
			User user = new User();
			user.setId(id);
			user.setDescription("User does not exist.");
			
			return new ResponseEntity<User>(user,HttpStatus.NOT_FOUND);
		} else {
			
			return new ResponseEntity<User>(userService.deleteUser(id), HttpStatus.OK);
		}
		
	} 
	
	@GetMapping(value="/rest/usersbyage/{age}", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public List<User> getUsersByAge(@PathVariable("age") int age){
		
		return userService.getUsersByAge(age);
	}
	
	@GetMapping(value="/rest/usersbyname/{name}", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public List<User> getUsersByName(@PathVariable("name") String name){
		
		return userService.getUsersByName(name);
	}
}
