package com.example.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.User;
import com.example.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;

	public UserService(){

	}
	public UserService(UserRepository userRepo){
		this.userRepo = userRepo;
	}
	
	public void addUser(User bean){
		User user = new User();
		user.setName(bean.getName());
		user.setAge(bean.getAge());
		userRepo.save(user);
	}
	
	public List<User> retreiveUsers(){
		List<User> users =  (ArrayList<User>) userRepo.findAll();

		return users;
	}

	public User getUser(int id){
		
		return userRepo.findOne(id);
	}
	
	public boolean isExistUser(int id){
		User user = userRepo.findOne(id);
		
		return user == null ? false : true; 
	}
	
	public User deleteUser(int id){
		User user = userRepo.findOne(id);
		userRepo.delete(id);
		
		return user;
	}
	
	public User updateUser(int id,User bean){
		User user = userRepo.findOne(id);
		user.setName(bean.getName());
		user.setAge(bean.getAge());
		userRepo.saveAndFlush(user);
		
		return user;
	}
	
	public User updateUser(User u){
		User user = new User();
		user.setId(u.getId());
		user.setName(u.getName());
		user.setAge(u.getAge());
		user.setDescription(u.getDescription());
		userRepo.save(user);
		
		return user;
	}
	
	public List<User> getUsersByAge(int age){
		
		return userRepo.getUsersByAge(age);
	}
	
	public List<User> getUsersByName(String name){
		
		return userRepo.getUsersByName(name);
	}
	
}
