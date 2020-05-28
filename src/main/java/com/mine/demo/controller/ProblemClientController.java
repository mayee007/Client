package com.mine.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.mine.SpringDataTest.Model.Problem;
import com.mine.SpringDataTest.Model.Technology;

import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/problems")
public class ProblemClientController {

	@Value("${app.baseUrl}")
	String baseUrl;
	
	String pathUrl = "/problems"; 
	private Logger logger = LoggerFactory.getLogger(ProblemClientController.class);
	
	@GetMapping()
	public ModelAndView getAllProblems() {
		logger.info("inside ProblemClientController().getAllProblems()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		RestTemplate restTemplate = new RestTemplate();
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allProblems", objs); 
        mv.setViewName("problems/allProblems"); 
        
        return mv; 
	}

	@GetMapping("/{id}")
    public @ResponseBody ModelAndView getProblemById(@PathVariable int id) {
		logger.info("inside ProblemClientController().getProblemById()");
		logger.info("url = "+ baseUrl + pathUrl + "/" +id); 
		
		RestTemplate restTemplate = new RestTemplate();
		Object obj = restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Object.class);
		
		
		ModelAndView mv = new ModelAndView(); 
		if (obj == null) {
			mv.addObject("id", id); 
			mv.addObject("recordType", "Problem"); 
			mv.setViewName("NotFound");
			logger.info("Object not found with ID "+ id);
		} else {		
			mv.addObject("problem", obj); 
			mv.setViewName("problems/singleProblem");
			logger.info("after call, Problem = "+obj.toString());			
		}
        
        return mv; 
    }
	
	@PostMapping
    //public @ResponseBody ModelAndView addProblem(@ModelAttribute Problem problem) {
	public ModelAndView addInfo(Map<String, Object> model, @RequestParam("technologyId") String techId,  
			@RequestParam("problem") String problem, 
			@RequestParam("reasonForProblem") String reasonForProblem,
			@RequestParam("solution") String solution) {
		logger.info("inside ProblemClientController().addProblem(), name is "+problem);
		logger.info("url = "+ baseUrl + pathUrl); 
		
		logger.info("problem = " + problem);
		logger.info("reasonForProblem = " + reasonForProblem);
		logger.info("solution = "+ solution);
		logger.info("technology ID = " + techId);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        Map map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");

        headers.setAll(map);

        Map req_payload = new HashMap();
        req_payload.put("technology", getTechnologyById(Integer.parseInt(techId)));
        req_payload.put("problem", problem);
        req_payload.put("reasonForProblem", reasonForProblem);
        req_payload.put("solution", solution); 
        req_payload.put("submitDate", new Date()); 
        req_payload.put("modifiedDate", new Date()); 

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Problem> response = restTemplate.postForEntity(baseUrl +pathUrl, request, Problem.class);
		
		logger.info(response.getBody().toString()); 
		
		ModelAndView mv = new ModelAndView(); 
		logger.info("status code =" + response.getStatusCode());
		if (response.getStatusCode() != HttpStatus.OK) {
			mv.setViewName("NotFound");
			mv.addObject("recordType", "Problem");
			mv.addObject("id", 0); 
			return mv; 
		}		
		
		mv.addObject("problem", response.getBody()); 
		mv.setViewName("problems/singleProblem");
        return mv; 
    }
	
	@DeleteMapping("/{id}")
    public @ResponseBody ModelAndView deleteProblem(@PathVariable int id) {
		logger.info("inside ProblemClientController().deleteProblem(), id is "+id);
		logger.info("delete path = " + baseUrl +pathUrl+ "/" + id);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(baseUrl +pathUrl+ "/" + id);
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("allProblems", objs); 
	    mv.setViewName("problems/allProblems"); 
        
        return mv;         
    }
	
	@PutMapping("/{id}")
    public @ResponseBody ModelAndView updateProblem(@PathVariable int id, 
    				@ModelAttribute Problem name) {
		logger.info("inside ProblemClientController().updateProblem(), name is "+name);
		logger.info("url = "+ baseUrl + pathUrl); 
		logger.info("name object to update = " + name);
		
		RestTemplate restTemplate = new RestTemplate();
		//restTemplate.put(baseUrl +pathUrl, name);
		
		restTemplate.put(baseUrl +pathUrl , Problem.class, name);
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allProblems", objs); 
        mv.setViewName("problems/allProblems"); 
        
        return mv; 
    }
	
	@GetMapping("/add")
	public ModelAndView addProblem() {
		logger.info("inside ProblemClientController().addProblem()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.setViewName("problems/addProblem"); 
        
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
