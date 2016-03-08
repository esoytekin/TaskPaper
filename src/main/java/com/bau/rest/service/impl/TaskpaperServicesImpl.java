package com.bau.rest.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
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
	
	Logger logger = Logger.getLogger(TaskpaperServicesImpl.class);
	
	private static final String INBOX = "Inbox";
	
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
		logger.debug("Retrieving categories for user..");
		List<Category> categories =  categoryDAO.getListByUser(getUser());
		if(categories.size() == 0){
			logger.info("Couldn't find any category. Creating Inbox...");
			categories.add(createAndGetFirstCategory());
		}
		
		if(!categories.get(0).getName().equals(INBOX)){
			logger.debug("Need to reorder categories...");
			
			reOrderCategories(categories);
			
		}

		for (Category category : categories) {
			//set category task count
			long taskCount = taskDAO.getTaskCountByCategory(category);
			category.setTaskCount(taskCount);
			
			//check for completed repeated tasks
			if(category.getRepeater() != null){
				checkForRepeaters(category);
			}
		}
		return categories;
	}

	private void reOrderCategories(List<Category> categories) {
		Category inbox = null;
		for (int i = 0; i < categories.size(); i++) {
			Category cat = categories.get(i);
			if(cat.getName().equals(INBOX)){
				inbox = categories.remove(i);
				break;
			}
		}
		
		List<Category> newCategories = new ArrayList<Category>();
		newCategories.add(inbox);
		newCategories.addAll(categories);
		
		categories.clear();
		categories.addAll(newCategories);
	}

	private void checkForRepeaters(Category category) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		logger.debug("Checking for repeaters...");
		switch (category.getRepeater()) {
		case NO_REPEAT:
			break;
		case DAILY:
			List<Task> tasks = getTasksByCategory(category);
			for (Task task : tasks) {
				if(task.getDone()){
					Date completeDate = task.getCompleteDate();
					if(completeDate.getTime()<c.getTimeInMillis()){
						logger.debug("Task is completed and Daily task. now reseting status...");
						task.setDone(false);
						updateTask(task);
					}
				}
			}
			
			break;
		case WEEKLY:
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			tasks = getTasksByCategory(category);
			for (Task task : tasks) {
				if(task.getDone()){
					Date completeDate = task.getCompleteDate();
					if(completeDate.getTime()<c.getTimeInMillis()){
						logger.debug("Task is completed and Weekly task. now reseting status...");
						task.setDone(false);
						updateTask(task);
					}
				}
			}
			
			break;
		case MONTHLY:
			c.set(Calendar.DAY_OF_MONTH, 1);
			tasks = getTasksByCategory(category);
			for (Task task : tasks) {
				if(task.getDone()){
					Date completeDate = task.getCompleteDate();
					if(completeDate.getTime()<c.getTimeInMillis()){
						logger.debug("Task is completed and Monthly task. now reseting status...");
						task.setDone(false);
						task.setCompleteDate(null);
						updateTask(task);
					}
				}
			}
			
			break;
		default:
			break;
		}
	}

	private Category createAndGetFirstCategory(){
			Category c = new Category();
			c.setUser(getUser());
			c.setDate(new Date());
			c.setEnabled(true);
			c.setName(INBOX);
			categoryDAO.save(c);
			logger.debug("Created inbox category...");
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
