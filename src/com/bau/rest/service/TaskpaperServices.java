package com.bau.rest.service;

import java.util.List;

import com.bau.rest.entity.Category;
import com.bau.rest.entity.SubTask;
import com.bau.rest.entity.Task;
import com.bau.rest.entity.User;
import com.bau.rest.entity.UserRole;

public interface TaskpaperServices {
	Category getCategoryByName(String name);
	Category getCategoryById(Long id);
	List<Category> getCategoriesByUser();
	void saveCategory(Category category);
	void updateCategory(Category category);
	void deleteCategory(Category category);

	void saveTask(Task task);
	void updateTask(Task task);
	Task getTaskById(Long id);
	List<Task> getTasksByCategory(Category c);
	void deleteTask(Task task);
	
	User getUser();
	
	void saveSubtask(SubTask subtask);
	List<SubTask> getSubtasksByTask(Task task);
	SubTask getSubtaskById(Long id);
	void updateSubtask(SubTask subtask);
	void deleteSubtask(SubTask subTask);
	
	void saveUser(User user);
	User getUserByUsername(String username);
	
	void saveUserRole(UserRole role);
	
	
}
