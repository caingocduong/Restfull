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

import com.example.dao.User;
import com.example.dao.UserDAO;
import com.example.repository.UserRepository;

@RestController
public class RestfulController{
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(value = "/rest/hello", method = RequestMethod.GET)
	public String hello(){
		return "Restful API using Spring";
	}
	
	@RequestMapping(value = "/rest/adduser", 
			method = RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User createUser(@RequestBody User user){
		UserDAO dao = new UserDAO(userRepo);
		if(dao.existedUser(user.getId()) == 0){
			dao.addUser(user);
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
		UserDAO dao = new UserDAO(userRepo);
		List<User> users = dao.getUser();
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
		UserDAO dao = new UserDAO(userRepo);
		if(dao.existedUser(id) == 0){
			user.setId(id);
			user.setDescription("User is not exist.");
			
			return user;
		} else {
			user.setId(id);
			dao.updateUser(id, user);
			user.setDescription("Upate successfully!");
			
			return user;
		}
	}
	
	@RequestMapping(value = "/rest/delete/{id}", 
			method = RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User deleteUser(@PathVariable("id") int id){
		UserDAO dao = new UserDAO(userRepo);
		if(dao.existedUser(id) == 0){
			User user = new User(id);
			user.setDescription("User is not exist.");
			
			return user;
		} else {
			User user = new User(id);
			user.setName(dao.getUser(id).getName());
			user.setAge(dao.getUser(id).getAge());
			user.setDescription(dao.getUser(id).getName());
			dao.deleteUser(id);
			
			return user;
		}
		
	}
	
	@RequestMapping(value="/rest/usersbyage/{age}", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> getUserByAge(@PathVariable("age") int age){
		UserDAO dao = new UserDAO(userRepo);
		
		return dao.getUserByAge(age);
	}
	
	@RequestMapping(value="/rest/usersbyname/{name}", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> getUserByName(@PathVariable("name") String name){
		UserDAO dao = new UserDAO(userRepo);
		
		return dao.getUserByName(name);
	}
}
