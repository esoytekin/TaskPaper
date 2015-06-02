package com.bau.rest.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.bau.rest.dao.CategoryDAO;
import com.bau.rest.dao.SubTaskDAO;
import com.bau.rest.dao.TaskDAO;
import com.bau.rest.dao.UserDAO;
import com.bau.rest.dao.UserRoleDAO;
import com.bau.rest.entity.Category;
import com.bau.rest.entity.SubTask;
import com.bau.rest.entity.Task;
import com.bau.rest.entity.User;
import com.bau.rest.entity.UserRole;
import com.bau.rest.service.TaskpaperServices;

@Component
public class TaskpaperServicesImpl implements TaskpaperServices{
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private TaskDAO taskDAO;
	
	@Autowired
	private SubTaskDAO subtaskDAO;
	
	@Autowired
	private UserRoleDAO userRoleDAO;
	
	@SuppressWarnings("unused")
	private String getUserRoles(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String roles = auth.getAuthorities().toString();
		return roles;
	}
	
	private String getUsername(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		return username;
	}
	
	@Override
	public User getUser(){
		
		return userDAO.getByUsername(getUsername());
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryDAO.getByName(name,getUser());
	}
	
	@Override
	public Category getCategoryById(Long id) {
		return categoryDAO.getById(id);
	}
	
	@Override
	public List<Category> getCategoriesByUser() {
		List<Category> categories =  categoryDAO.getListByUser(getUser());
		if(categories.size() == 0){
			categories.add(createAndGetFirstCategory());
		}
		for (Category category : categories) {
			long taskCount = taskDAO.getTaskCountByCategory(category);
			category.setTaskCount(taskCount);
		}
		return categories;
	}
	
	private Category createAndGetFirstCategory(){
			Category c = new Category();
			c.setUser(getUser());
			c.setDate(new Date());
			c.setEnabled(true);
			c.setName("Inbox");
			categoryDAO.save(c);
			return c;
		
	}
	
	@Override
	public void saveTask(Task task) {
		taskDAO.save(task);
	}
	
	@Override
	public void updateTask(Task task) {
		taskDAO.update(task);
	}
	
	@Override
	public Task getTaskById(Long id) {
		Task t = taskDAO.getById(id);
		t.setSubTaskCount(subtaskDAO.getSubTaskCountByTask(t));
		return t;
	}
	
	@Override
	public List<Task> getTasksByCategory(Category c) {
		List<Task> tasks = taskDAO.getByCategory(c);
		for (Task task : tasks) {
			task.setSubTaskCount(subtaskDAO.getSubTaskCountByTask(task));
		}
		return tasks;
	}
	
	@Override
	public void deleteTask(Task task) {
		List<SubTask> subtasks = subtaskDAO.getByTask(task);
		for (SubTask subTask : subtasks) {
			subtaskDAO.delete(subTask);
		}
		taskDAO.delete(task);
		
	}
	
	@Override
	public void saveCategory(Category category) {
		categoryDAO.save(category);
	}
	
	@Override
	public void updateCategory(Category category) {
		categoryDAO.update(category);
	}
	
	@Override
	public void deleteCategory(Category category) {
		List<Task> tasks = taskDAO.getByCategory(category);
		for (Task task : tasks) {
			deleteTask(task);
		}
		categoryDAO.delete(category);
	}
	
	
	@Override
	public void saveSubtask(SubTask subtask) {
		subtaskDAO.save(subtask);
	}
	
	@Override
	public List<SubTask> getSubtasksByTask(Task task) {
		return subtaskDAO.getByTask(task);
	}
	
	@Override
	public SubTask getSubtaskById(Long id) {
		return subtaskDAO.getById(id);
	}
	
	@Override
	public void updateSubtask(SubTask subtask) {
		subtaskDAO.update(subtask);
	}
	
	@Override
	public void deleteSubtask(SubTask subTask) {
		subtaskDAO.delete(subTask);
	}
	
	@Override
	public void saveUser(User user) {
		userDAO.save(user);
	}
	
	@Override
	public User getUserByUsername(String username) {
		return userDAO.getByUsername(username);
	}
	
	@Override
	public void saveUserRole(UserRole role) {
		userRoleDAO.save(role);
	}
	

}
