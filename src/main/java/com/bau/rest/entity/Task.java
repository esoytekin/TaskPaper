package com.bau.rest.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("task")
public class Task extends TaskKernel{

	@ManyToOne
	@JoinColumn(name="CATEGORY_ID")
	private Category category;


	@Column(name="CHECK_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completeDate;
	
	@Transient
	private Long subTaskCount;


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public Date getCompleteDate() {
		return completeDate;
	}


	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Long getSubTaskCount() {
		return subTaskCount;
	}
	
	public void setSubTaskCount(Long subTaskCount) {
		this.subTaskCount = subTaskCount;
	}
	
}
