package com.example.RestfulCRUD;

import com.example.service.User;

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
		return new User(id,name,age,description);
	}
}
