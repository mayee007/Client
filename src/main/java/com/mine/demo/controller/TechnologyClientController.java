package com.mine.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.mine.SpringDataTest.Model.Technology;

import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/techs")
public class TechnologyClientController {

	@Value("${app.baseUrl}")
	String baseUrl;
	
	String pathUrl = "/techs"; 
	private Logger logger = LoggerFactory.getLogger(TechnologyClientController.class);
	
	@Autowired
	RestTemplate restTemplate; 
	
	@GetMapping()
	public ModelAndView getAllTechs() {
		logger.info("inside TechnologyClientController().getAllTechs()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 

		ModelAndView mv = new ModelAndView(); 
        mv.addObject("allTechs", objs); 
        mv.setViewName("techs/allTechs"); 
        
        return mv; 
	}

	@GetMapping("/{id}")
    public @ResponseBody ModelAndView getTechnologyById(@PathVariable int id) {
		logger.info("inside TechnologyClientController().getTechnologyById()");
		logger.info("url = "+ baseUrl + pathUrl + "/" +id); 
		
		Object obj = restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Object.class);
		
		ModelAndView mv = new ModelAndView(); 
		if (obj == null) {
			mv.addObject("id", id); 
			mv.addObject("recordType", "Technology"); 
			mv.setViewName("NotFound");
			logger.info("Object not found with ID "+ id);
		} else {		
			mv.addObject("tech", obj); 
			mv.setViewName("techs/singleTechnology");
			logger.info("after call, Technology = "+obj.toString());			
		}
        
        return mv; 
    }
	
	@PostMapping
    public @ResponseBody ModelAndView addTechnology(@ModelAttribute Technology tech) {
		logger.info("inside TechnologyClientController().addTechnology(), technology is "+tech);
		logger.info("url = "+ baseUrl + pathUrl); 
		
		logger.info("Technology object to add = " + tech);
		logger.info("ID is "+ tech.getTechnologyId());
		logger.info("technology Type is " + tech.getTechnologyType());
		logger.info("Category  is " + tech.getCategory());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    JSONObject jsonObj = new JSONObject();
	    jsonObj.put("category", tech.getCategory());
	    jsonObj.put("technologyType", tech.getTechnologyType()); 
	   
		HttpEntity<String> request = 
			      new HttpEntity<String>(jsonObj.toString(), headers);
		Object obj = restTemplate.postForEntity(baseUrl +pathUrl, request, Object.class);
		
		logger.info(obj.toString()); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("tech", obj); 
		mv.setViewName("techs/singleTechnology");
        
        return mv; 
    }
	
	@DeleteMapping("/{id}")
    public @ResponseBody ModelAndView deleteTechnology(@PathVariable int id) {
		logger.info("inside TechnologyClientController().deleteTechnology(), id is "+id);
		logger.info("delete path = " + baseUrl +pathUrl+ "/" + id);
		restTemplate.delete(baseUrl +pathUrl+ "/" + id);
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("allTechs", objs); 
	    mv.setViewName("techs/allTechs"); 
        
        return mv;         
    }
	
	@PutMapping("/{id}")
    public @ResponseBody ModelAndView updateTechnology(@PathVariable int id, 
    				@ModelAttribute Technology name) {
		logger.info("inside TechnologyClientController().updateTechnology(), name is "+name);
		logger.info("url = "+ baseUrl + pathUrl); 
		logger.info("name object to update = " + name);
		
		restTemplate.put(baseUrl +pathUrl , Technology.class, name);
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allTechs", objs); 
        mv.setViewName("techs/allTechs"); 
        
        return mv; 
    }		
	
	@GetMapping("/add")
	public ModelAndView addTechnology() {
		logger.info("inside TechnologyClientController().addTechnology()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.setViewName("techs/addTechnology"); 
        
        return mv; 
	}
}
