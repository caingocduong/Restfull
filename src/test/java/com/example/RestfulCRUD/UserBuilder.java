package com.example.RestfulCRUD;

import com.example.entities.User;

public class UserBuilder {
	int id;
	String name;
	int age;
	String description;
	
	public UserBuilder withID(int id){
		this.id = id;
		
		return this;
	}
	
	public UserBuilder withName(String name){
		this.name = name;
		
		return this;
	}
	
	public UserBuilder withAge(int age){
		this.age = age;
		
		return this;
	}
	
	public UserBuilder withDescription(String description){
		this.description = description;
		
		return this;
	}
	
	public User buid(){
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setAge(age);
		user.setDescription(description);
		
		return user;
	}
}
