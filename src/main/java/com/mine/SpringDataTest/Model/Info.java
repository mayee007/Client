package com.mine.SpringDataTest.Model;

import java.util.Date;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Info")
public class Info implements java.io.Serializable {
	int id; 
	
	String subject;
	
	String description; 
	
	Date submitDate; 
	
	Date modifiedDate;
	
	Technology technology;
	
	// default constructor
	public Info() {
	}
	
	// constructor with all params 
	public Info(int id, String subject, String description, Date submitDate, Date modifiedDate, Technology technology) {
		super();
		this.id = id;
		this.subject = subject;
		this.description = description;
		this.submitDate = submitDate;
		this.modifiedDate = modifiedDate;
		this.technology = technology;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Technology getTechnology() {
		return technology;
	}
	public void setTechnology(Technology technology) {
		this.technology = technology;
	} 
	
	
}
