package com.mine.demo.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mine.SpringDataTest.Model.FullName;

@Repository
public interface FullNameRepo extends CrudRepository<FullName, Integer>{

}
