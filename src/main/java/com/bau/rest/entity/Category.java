package com.bau.rest.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.bau.rest.pojo.Repeater;

@Entity
@Table(name="C_CATEGORY")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;
	
	@Column(name="NAME")
	private String name;

	@Column(name="DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Column(name="ENABLED")
	@Type(type="yes_no")
	private Boolean enabled;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="REPEATER")
	private Repeater repeater;
	
	@Transient
	private Long taskCount;
	
	@Transient
	private Long completedTaskCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Long getTaskCount() {
		return taskCount;
	}
	
	public void setTaskCount(Long taskCount) {
		this.taskCount = taskCount;
	}
	
	public Long getCompletedTaskCount() {
		return completedTaskCount;
	}
	
	public void setCompletedTaskCount(Long completedTaskCount) {
		this.completedTaskCount = completedTaskCount;
	}
	
	public Repeater getRepeater() {
		return repeater;
	}
	
	
	public void setRepeater(Repeater repeater) {
		this.repeater = repeater;
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", enabled=" + enabled
				+ ", repeater=" + repeater + "]";
	}
	
	
	
}
