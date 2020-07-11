package com.mine.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
import com.mine.SpringDataTest.Model.TestClass;
import com.mine.demo.service.InfoRepo;
import com.mine.demo.service.TestClassRepo;

@Controller
@RequestMapping("/infos")
public class InfoClientController {

	@Value("${app.baseUrl}")
	String baseUrl;
	
	String pathUrl = "/infos"; 
	private Logger logger = LoggerFactory.getLogger(InfoClientController.class);
	
	@Autowired
	RestTemplate restTemplate; 
	
	@Autowired 
	InfoRepo redisRepo; 
	
	@Autowired 
	TestClassRepo testClassRepo; 
	
	@GetMapping()
	public ModelAndView getAllInfos() {
		logger.info("inside InfoClientController().getAllInfos()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		RestTemplate restTemplate = new RestTemplate();
		
		Info[] objs = restTemplate.getForObject(baseUrl +pathUrl, Info[].class); 
		
		// store all objects in redis 
		//logger.info("storing all infos in Redis");
		//for (Info info: objs) {
		//	redisRepo.save(info);  
		//}
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allInfos", objs); 
        mv.setViewName("infos/allInfos"); 
        
        return mv; 
	}

	@GetMapping("/{id}")	
	@Cacheable(value="INFO", key="#id")
    public @ResponseBody ModelAndView getInfoById(@PathVariable int id) {
		logger.info("inside InfoClientController().getInfoById()");
		logger.info("url = "+ baseUrl + pathUrl + "/" +id); 
		
		Info info = new Info(); 
		// before calling, verify whether it exists in Redis 
		//if (redisRepo.existsById(id)) {
		//	logger.info("got a hit for Info record "+id+" in Redis");
			//redisRepo.findById(id); 
		//} else {
		 	info = restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Info.class);
		//}
	
		ModelAndView mv = new ModelAndView(); 
		if (info == null) {
			mv.addObject("id", id); 
			mv.addObject("recordType", "Info"); 
			mv.setViewName("NotFound");
			logger.info("Object not found with ID "+ id);
		} else {	
			// saving record in Redis 
			//Info info = restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Info.class);
			//logger.info("saving Info:" + info.getId() + " to Redis cluster");
			//redisRepo.save(info); 
			
			mv.addObject("info", info); 
			mv.setViewName("infos/singleInfo");
			//logger.info("after call, Info = "+info.toString());			
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
		
		// add object into redis 
		logger.info("storing Info object info redis");				
		redisRepo.save(response.getBody()); 
		
		mv.addObject("info", response.getBody()); 
		mv.setViewName("infos/singleInfo");
        return mv; 
    }
	
	
	@DeleteMapping("/{id}")
    public @ResponseBody ModelAndView deleteInfo(@PathVariable int id) {
		logger.info("inside InfoClientController().deleteInfo(), id is "+id);
		logger.info("delete path = " + baseUrl +pathUrl+ "/" + id);
	
		restTemplate.delete(baseUrl +pathUrl+ "/" + id);
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("allInfos", objs); 
	    mv.setViewName("infos/allInfos"); 
        
        return mv;         
    }
	
	@PutMapping("/{infoId}")
	public ModelAndView updateInfo(@PathVariable int infoId,
			Map<String, Object> model, 
			@RequestParam("technologyId") String techId,  
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
        req_payload.put("id", infoId);
        req_payload.put("technology", getTechnologyById(Integer.parseInt(techId)));
        req_payload.put("subject", subject);
        req_payload.put("description", description); 
        req_payload.put("submitDate", new Date()); 
        req_payload.put("modifiedDate", new Date()); 

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        
        // call put operation
        restTemplate.put(baseUrl +pathUrl+"/"+infoId, request);
		
        // get all infos
        Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class);
        
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("info", objs); 
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
        Object[] objs = restTemplate.getForObject(baseUrl +"/techs", Object[].class); 
		
		//logger.info("all techs = " + objs.toString());
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