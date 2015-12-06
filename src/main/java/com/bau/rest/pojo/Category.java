package com.bau.rest.pojo;

import java.util.Date;

public class Category {

	private Long id;
	
	private String name;

	private Date date;
	
	private Boolean enabled;
	
	private Repeater repeater;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public Repeater getRepeater() {
		return repeater;
	}
	
	public void setRepeater(Repeater repeater) {
		this.repeater = repeater;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", date=" + date
				+ ", enabled=" + enabled + "]";
	}
	
	
	
}
