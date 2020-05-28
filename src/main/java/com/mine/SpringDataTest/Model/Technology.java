package com.mine.SpringDataTest.Model;

public class Technology implements java.io.Serializable {
	
	int technologyId;
	
	String technologyType;
	
	String category;
		
	public Technology() {
	}
	
	public Technology(int technologyId, String technologyType, String category) {
		super();
		this.technologyId = technologyId;
		this.technologyType = technologyType;
		this.category = category;
	}
	
	public int getTechnologyId() {
		return technologyId;
	}
	public void setTechnologyId(int technologyId) {
		this.technologyId = technologyId;
	}
	public String getTechnologyType() {
		return technologyType;
	}
	public void setTechnologyType(String technologyType) {
		this.technologyType = technologyType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
