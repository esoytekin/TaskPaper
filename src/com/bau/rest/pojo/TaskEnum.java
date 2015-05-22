package com.bau.rest.pojo;

import java.util.ArrayList;
import java.util.List;

import com.bau.rest.entity.User;

public enum TaskEnum {
	instance;
	
	private List<Task> tasklist = new ArrayList<Task>();
	
	private User user;
	
	private TaskEnum() {
	}
	
	public List<Task> getTasklist() {
		return tasklist;
	}
	
	public void setTasklist(List<Task> tasklist) {
		this.tasklist = tasklist;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
