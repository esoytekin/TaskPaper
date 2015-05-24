package com.bau.rest.dao;

import java.util.List;

import com.bau.rest.entity.Category;
import com.bau.rest.entity.User;

public interface CategoryDAO {

	void save(Category entity);
	void update(Category entity);
	void delete(Category entity);
	Category getById(Long id);
	Category getByName(String name, User user);
	List<Category> getList();
	List<Category> getListByUser(User user);
}
