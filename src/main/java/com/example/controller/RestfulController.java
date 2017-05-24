package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	public User createUser(@RequestBody User user){
		if(userService.existedUser(user.getId()) == 0){
			userService.addUser(user);
			user.setDescription("Create succesfully");
			
			return user;
		} else {
			user.setDescription("ID existed!");
			
			return user;
		}
	}
	
	@RequestMapping(value="/rest/users", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> readUser(){
		List<User> users = userService.getUser();
		if(users.isEmpty()){
			return null;
		}
		return users;
	}
	
	@RequestMapping(value = "/rest/updateuser/{id}", 
			method = RequestMethod.PUT,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User updateUser(@PathVariable("id") int id,@RequestBody User user){
		if(userService.existedUser(id) == 0){
			user.setId(id);
			user.setDescription("User is not exist.");
			
			return user;
		} else {
			user.setId(id);
			userService.updateUser(id, user);
			user.setDescription("Upate successfully!");
			
			return user;
		}
	}
	
	@RequestMapping(value = "/rest/delete/{id}", 
			method = RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User deleteUser(@PathVariable("id") int id){
		if(userService.existedUser(id) == 0){
			User user = new User(id);
			user.setDescription("User is not exist.");
			
			return user;
		} else {
			User user = new User(id);
			user.setName(userService.getUser(id).getName());
			user.setAge(userService.getUser(id).getAge());
			user.setDescription(userService.getUser(id).getName());
			userService.deleteUser(id);
			
			return user;
		}
		
	}
	
	@RequestMapping(value="/rest/usersbyage/{age}", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> getUserByAge(@PathVariable("age") int age){
		
		return userService.getUserByAge(age);
	}
	
	@RequestMapping(value="/rest/usersbyname/{name}", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> getUserByName(@PathVariable("name") String name){
		
		return userService.getUserByName(name);
	}
}
