package com.mine.SpringDataTest.Model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("TestClass")
public class TestClass implements Serializable{
	int id; 
	String str;

	public TestClass() {		
	}
	public TestClass(int id, String str) {
		super();
		this.id = id;
		this.str = str;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	} 
	

}
