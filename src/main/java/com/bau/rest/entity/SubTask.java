package com.bau.rest.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("subtask")
public class SubTask extends TaskKernel{
	
	@ManyToOne
	@JoinColumn(name="TASK_ID")
	private Task task;
	
	public Task getTask() {
		return task;
	}
	
	
	public void setTask(Task task) {
		this.task = task;
	}

}
