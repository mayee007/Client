package com.mine.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.mine.SpringDataTest.Model.FullName;

import net.minidev.json.JSONObject;


@Controller
@RequestMapping("/names")
public class FullNameClientController {

	@Value("${app.baseUrl}")
	String baseUrl;
	
	String pathUrl = "/names"; 
	private Logger logger = LoggerFactory.getLogger(FullNameClientController.class);
	
	@GetMapping()
	public ModelAndView getAllNames() {
		logger.info("inside FullNameClientController().getAllNames()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		RestTemplate restTemplate = new RestTemplate();
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allNames", objs); 
        mv.setViewName("names/allNames"); 
        
        return mv; 
	}

	@GetMapping("/{id}")
    public @ResponseBody ModelAndView getFullNameById(@PathVariable int id) {
		logger.info("inside FullNameClientController().getFullNameById()");
		logger.info("url = "+ baseUrl + pathUrl + "/" +id); 
		
		RestTemplate restTemplate = new RestTemplate();
		Object obj = restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Object.class);
		
		
		ModelAndView mv = new ModelAndView(); 
		if (obj == null) {
			mv.addObject("id", id); 
			mv.addObject("recordType", "FullName"); 
			mv.setViewName("NotFound");
			logger.info("Object not found with ID "+ id);
		} else {		
			mv.addObject("fullName", obj); 
			mv.setViewName("names/singleName");
			logger.info("after call, fullname = "+obj.toString());			
		}
        
        return mv; 
    }
	
	@PostMapping
    public @ResponseBody ModelAndView addFullName(@ModelAttribute FullName name) {
		logger.info("inside FullNameClientController().addFullName(), name is "+name);
		logger.info("url = "+ baseUrl + pathUrl); 
		
		logger.info("name object to add = " + name);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    JSONObject jsonObj = new JSONObject();
	    jsonObj.put("firstName", name.getFirstName()); 
	    jsonObj.put("lastName", name.getLastName()); 
	   
		HttpEntity<String> request = 
			      new HttpEntity<String>(jsonObj.toString(), headers);
		Object obj = restTemplate.postForObject(baseUrl +pathUrl, request, Object.class);
		
		logger.info(obj.toString()); 
		
		ModelAndView mv = new ModelAndView(); 
        mv.addObject("fullName", obj); 
        mv.setViewName("names/singleName"); 
        
        return mv; 
    }
	
	@DeleteMapping("/{id}")
    public @ResponseBody ModelAndView deleteFullName(@PathVariable int id) {
		logger.info("inside FullNameClientController().deleteFullName(), id is "+id);
		logger.info("delete path = " + baseUrl +pathUrl+ "/" + id);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(baseUrl +pathUrl+ "/" + id);
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("allNames", objs); 
        mv.setViewName("names/allNames"); 
        
        return mv;         
    }
	
	@PutMapping("/{id}")
    public @ResponseBody ModelAndView updateFullName(@PathVariable int id, 
    				@ModelAttribute FullName name) {
		logger.info("inside FullNameClientController().updateFullName(), name is "+name);
		logger.info("url = "+ baseUrl + pathUrl); 
		logger.info("name object to update = " + name);
		
		RestTemplate restTemplate = new RestTemplate();
		//restTemplate.put(baseUrl +pathUrl, name);
		
		restTemplate.put(baseUrl +pathUrl , FullName.class, name);
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allNames", objs); 
        mv.setViewName("names/allNames"); 
        
        return mv; 
    }	
	
	@GetMapping("/add")
	public ModelAndView addFullName() {
		logger.info("inside FullNameClientController().addFullName()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.setViewName("names/addFullName"); 
        
        return mv; 
	}
}
