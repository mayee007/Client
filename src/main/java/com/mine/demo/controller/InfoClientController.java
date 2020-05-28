package com.mine.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.mine.SpringDataTest.Model.Info;
import com.mine.SpringDataTest.Model.Technology;
import com.mine.SpringDataTest.Model.Info;

@Controller
@RequestMapping("/infos")
public class InfoClientController {

	@Value("${app.baseUrl}")
	String baseUrl;
	
	String pathUrl = "/infos"; 
	private Logger logger = LoggerFactory.getLogger(InfoClientController.class);
	
	@GetMapping()
	public ModelAndView getAllInfos() {
		logger.info("inside InfoClientController().getAllInfos()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		RestTemplate restTemplate = new RestTemplate();
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allInfos", objs); 
        mv.setViewName("infos/allInfos"); 
        
        return mv; 
	}

	@GetMapping("/{id}")
    public @ResponseBody ModelAndView getInfoById(@PathVariable int id) {
		logger.info("inside InfoClientController().getInfoById()");
		logger.info("url = "+ baseUrl + pathUrl + "/" +id); 
		
		RestTemplate restTemplate = new RestTemplate();
		Object obj = restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Object.class);
		
		
		ModelAndView mv = new ModelAndView(); 
		if (obj == null) {
			mv.addObject("id", id); 
			mv.addObject("recordType", "Info"); 
			mv.setViewName("NotFound");
			logger.info("Object not found with ID "+ id);
		} else {		
			mv.addObject("info", obj); 
			mv.setViewName("infos/singleInfo");
			logger.info("after call, Info = "+obj.toString());			
		}
        
        return mv; 
    }
	
	@PostMapping
    //public @ResponseBody ModelAndView addInfo(@ModelAttribute Info Info) {
	public ModelAndView addInfo(Map<String, Object> model, @RequestParam("technologyId") String techId,  
			@RequestParam("subject") String subject, 
			@RequestParam("description") String description) 
	{
		logger.info("inside InfoClientController().addInfo()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		logger.info("subject = " + subject);
		logger.info("description = " + description);
		logger.info("technology ID = " + techId);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        Map map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");

        headers.setAll(map);

        Map req_payload = new HashMap();
        req_payload.put("technology", getTechnologyById(Integer.parseInt(techId)));
        req_payload.put("subject", subject);
        req_payload.put("description", description); 
        req_payload.put("submitDate", new Date()); 
        req_payload.put("modifiedDate", new Date()); 

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Info> response = restTemplate.postForEntity(baseUrl +pathUrl, request, Info.class);
		
		logger.info(response.getBody().toString()); 
		
		ModelAndView mv = new ModelAndView(); 
		logger.info("status code =" + response.getStatusCode());
		if (response.getStatusCode() != HttpStatus.OK) {
			mv.setViewName("NotFound");
			mv.addObject("recordType", "Info");
			mv.addObject("id", 0); 
			return mv; 
		}		
		
		mv.addObject("info", response.getBody()); 
		mv.setViewName("infos/singleInfo");
        return mv; 
    }
	
	
	@DeleteMapping("/{id}")
    public @ResponseBody ModelAndView deleteInfo(@PathVariable int id) {
		logger.info("inside InfoClientController().deleteInfo(), id is "+id);
		logger.info("delete path = " + baseUrl +pathUrl+ "/" + id);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(baseUrl +pathUrl+ "/" + id);
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("allInfos", objs); 
	    mv.setViewName("infos/allInfos"); 
        
        return mv;         
    }
	
	@PutMapping("/{id}")
    public @ResponseBody ModelAndView updateInfo(@PathVariable int id, 
    				@ModelAttribute Info name) {
		logger.info("inside InfoClientController().updateInfo(), name is "+name);
		logger.info("url = "+ baseUrl + pathUrl); 
		logger.info("name object to update = " + name);
		
		RestTemplate restTemplate = new RestTemplate();
		//restTemplate.put(baseUrl +pathUrl, name);
		
		restTemplate.put(baseUrl +pathUrl , Info.class, name);
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allInfos", objs); 
        mv.setViewName("infos/allInfos"); 
        
        return mv; 
    }	
	
	
	@GetMapping("/add")
	public ModelAndView addInfo() {
		logger.info("inside ProblemClientController().addInfo()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.setViewName("infos/addInfo"); 
        
        // get all technology as a list 
        RestTemplate restTemplate = new RestTemplate();
		Object[] objs = restTemplate.getForObject(baseUrl +"/techs", Object[].class); 
		
		logger.info("all techs = " + objs.toString());
        mv.addObject("allTechs", objs); 
        
        return mv; 
	}
	
	Technology getTechnologyById(int id) { 
		RestTemplate restTemplate = new RestTemplate();
		logger.info("inside getTechnologyById, id = "+ id);
		Technology obj = restTemplate.getForObject(baseUrl +"/techs/"+id, Technology.class);
		logger.info("obj found = "+ obj); 
		return obj; 
	}
}