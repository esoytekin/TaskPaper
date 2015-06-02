package com.bau.rest.dao;

import java.util.List;

import com.bau.rest.entity.SubTask;
import com.bau.rest.entity.Task;

public interface SubTaskDAO {

	void save(SubTask entity);
	void update(SubTask entity);
	void delete(SubTask entity);
	SubTask getById(Long id);
	List<SubTask> getList();
	List<SubTask> getByTask(Task entity);
	Long getSubTaskCountByTask(Task entity);
}
