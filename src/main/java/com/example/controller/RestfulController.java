package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.User;
import com.example.service.UserService;

@RestController
public class RestfulController{
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/rest/hello", method = RequestMethod.GET)
	public String hello(){
		return "Restful API using Spring";
	}
	
	@RequestMapping(value = "/rest/adduser", 
			method = RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
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
	
	@RequestMapping(value="/rest/users", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> readUser(){
		List<User> users = userService.retreiveUsers();
		if(users.isEmpty()){
			return null;
		}
		return users;
	}
	
	@RequestMapping(value = "/rest/updateuser/{id}", 
			method = RequestMethod.PUT,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
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
	
	@RequestMapping(value = "/rest/delete/{id}", 
			method = RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<User> deleteUser(@PathVariable("id") int id){
		if(!userService.isExistUser(id)){
			User user = new User(id);
			user.setDescription("User does not exist.");
			
			return new ResponseEntity<User>(user,HttpStatus.NOT_FOUND);
		} else {
			
			return new ResponseEntity<User>(userService.deleteUser(id), HttpStatus.OK);
		}
		
	} 
	
	@RequestMapping(value="/rest/usersbyage/{age}", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> getUsersByAge(@PathVariable("age") int age){
		
		return userService.getUsersByAge(age);
	}
	
	@RequestMapping(value="/rest/usersbyname/{name}", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> getUsersByName(@PathVariable("name") String name){
		
		return userService.getUsersByName(name);
	}
}
