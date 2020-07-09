package com.mine.demo.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mine.SpringDataTest.Model.Info;
import com.mine.SpringDataTest.Model.TestClass;

@Repository
public interface TestClassRepo extends CrudRepository<TestClass, Integer>{

}
