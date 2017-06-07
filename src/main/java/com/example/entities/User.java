package com.example.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "User")
@NamedNativeQuery(name = "User.getUserByname" ,
					query = "select * from  User u where LOWER(u.name) LIKE LOWER (:name)",
					resultClass = User.class)

@Data public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String name;
	@Column
	private int age;
	@Column
	private String description;
	

}
