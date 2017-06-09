package com.example.services;

import java.util.List;

import com.example.entities.User;

public interface UserService {
	List<User> retreiveUsers();
	void addUser(User user);
	User getUser(int id);
	boolean isExistUser(int id);
	User deleteUser(int id);
	User updateUser(User user);
	List<User> getUsersByAge(int age);
	List<User> getUsersByName(String name);
	
}
