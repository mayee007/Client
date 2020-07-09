package com.mine.demo.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mine.SpringDataTest.Model.Info;

@Repository
public interface InfoRepo extends CrudRepository<Info, Integer>{

}
