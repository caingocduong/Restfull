package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query(nativeQuery = true)
	List<User> getUsersByName(@Param("name") String name);
	
	@Query(value = "select * from User u Where u.age > :age",
			nativeQuery = true)
	List<User> getUsersByAge(@Param("age") int age);
	User findUserByName(String userName);
}
