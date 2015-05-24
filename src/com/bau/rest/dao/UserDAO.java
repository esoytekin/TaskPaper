package com.bau.rest.dao;

import java.util.List;

import com.bau.rest.entity.User;

public interface UserDAO {
	
	void save(User entity);
	void update(User entity);
	void delete(User entity);
	User getById(Long id);
	User getByUsername(String username);
	List<User> getList();

}
