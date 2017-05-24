package com.example.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.repository.UserRepository;

public class UserDAO {
	@Autowired
	private UserRepository userRepo;

	public UserDAO(){

	}
	public UserDAO(UserRepository userRepo){
		this.userRepo = userRepo;
	}
	public void addUser(User bean){
		User user = new User();
		user.setName(bean.getName());
		user.setAge(bean.getAge());
		userRepo.save(user);
	}

	public List<User> getUser(){
		List<User> users =  (ArrayList<User>) userRepo.findAll();

		return users;
	}

	public User getUser(int id){
		
		return userRepo.findOne(id);
	}
	
	public int existedUser(int id){
		User user = userRepo.findOne(id);
		
		return user == null ? 0 : 1; 
	}
	
	public void deleteUser(int id){
		userRepo.delete(id);
	}

	public void updateUser(int id,User bean){
		User user = userRepo.findOne(id);
		user.setName(bean.getName());
		user.setAge(bean.getAge());

		userRepo.save(user);
	}
	
	public List<User> getUserByAge(int age){
		
		return userRepo.getUserByAge(age);
	}
	
	public List<User> getUserByName(String name){
		
		return userRepo.getUserByName(name);
	}
}
