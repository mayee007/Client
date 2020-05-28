package com.mine.SpringDataTest.Model;

public class FullName {


	int id; 
	
	String firstName; 
	
	String lastName; 
	
	public FullName() {
		
	}
	public FullName(int id, String firstName, String lastName) {
		super();
		this.id = id; 
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "FullName [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
}
