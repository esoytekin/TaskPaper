package com.bau.rest.dao;

import java.util.List;

import com.bau.rest.entity.UserRole;

public interface UserRoleDAO {
	UserRole getById(Long id);
	UserRole getByUsername(String username);
	void save(UserRole entity);
	void update(UserRole entity);
	void delete(UserRole entity);
	List<UserRole> getUserRoles();
	
}
