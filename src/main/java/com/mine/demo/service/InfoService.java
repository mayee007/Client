package com.mine.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.mine.SpringDataTest.Model.Info;
import com.mine.demo.service.interfaces.IInfoService;

//@Service
public class InfoService implements IInfoService {

	@Value("${app.baseUrl}")
	String baseUrl;
	
	String pathUrl = "/infos"; 
	private Logger logger = LoggerFactory.getLogger(InfoService.class);
	
	@Autowired
	RestTemplate restTemplate; 
	
	public InfoService() {		
	}

	@Override
	@Cacheable(value="INFO", key="#id")
	public Info getInfoById(int id) {
		logger.info("inside InfoService().getInfoById()");
		return restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Info.class);
	}

	/* @Override
	//@Cacheable(value="allInfo")
	public Info[] getAllInfos() {
		logger.info("inside InfoService().getAllInfos()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(baseUrl +pathUrl, Info[].class); 
	}

	@Override
	public Info AddInfo(Info info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInfo(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Info updateInfo(Info info) {
		// TODO Auto-generated method stub
		return null;
	}
	*/ 
	
}
