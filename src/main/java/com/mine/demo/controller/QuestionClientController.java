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
import com.mine.SpringDataTest.Model.Question;
import com.mine.SpringDataTest.Model.Technology;

import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/questions")
public class QuestionClientController {

	@Value("${app.baseUrl}")
	String baseUrl;
	
	String pathUrl = "/questions"; 
	private Logger logger = LoggerFactory.getLogger(QuestionClientController.class);
	
	@Autowired
	RestTemplate restTemplate; 
	
	@GetMapping()
	public ModelAndView getAllQuestions() {
		logger.info("inside QuestionClientController().getAllQuestions()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
		logger.info(objs.toString());
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allQuestions", objs); 
        mv.setViewName("questions/allQuestions"); 
        
        return mv; 
	}

	@GetMapping("/{id}")
    public @ResponseBody ModelAndView getQuestionById(@PathVariable int id) {
		logger.info("inside QuestionClientController().getQuestionById()");
		logger.info("url = "+ baseUrl + pathUrl + "/" +id); 
		
		Object obj = restTemplate.getForObject(baseUrl +pathUrl+"/"+id, Object.class);
		
		ModelAndView mv = new ModelAndView(); 
		if (obj == null) {
			mv.addObject("id", id); 
			mv.addObject("recordType", "Question"); 
			mv.setViewName("NotFound");
			logger.info("Object not found with ID "+ id);
		} else {		
			mv.addObject("question", obj); 
			mv.setViewName("questions/singleQuestion");
			logger.info("after call, Question = "+obj.toString());			
		}
        
        return mv; 
    }
	
	@PostMapping
    public @ResponseBody ModelAndView addQuestion(@ModelAttribute Question question) {
		logger.info("inside QuestionClientController().addQuestion(), name is "+question);
		logger.info("url = "+ baseUrl + pathUrl); 
		
		logger.info("name object to add = " + question);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    JSONObject jsonObj = new JSONObject();
	    jsonObj.put("question", question.getQuestion());
	   
		HttpEntity<String> request = 
			      new HttpEntity<String>(jsonObj.toString(), headers);
		Object obj = restTemplate.postForEntity(baseUrl +pathUrl, request, Object.class);
		
		logger.info(obj.toString()); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("Question", obj); 
		mv.setViewName("Questions/singleQuestion");
        
        return mv; 
    }
	
	@DeleteMapping("/{id}")
    public @ResponseBody ModelAndView deleteQuestion(@PathVariable int id) {
		logger.info("inside QuestionClientController().deleteQuestion(), id is "+id);
		logger.info("delete path = " + baseUrl +pathUrl+ "/" + id);
		
		restTemplate.delete(baseUrl +pathUrl+ "/" + id);
		
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("allQuestions", objs); 
	    mv.setViewName("questions/allQuestions"); 
        
        return mv;         
    }
	
	@PutMapping("/{id}")
    public @ResponseBody ModelAndView updateQuestion(@PathVariable int id, 
    				@ModelAttribute Question question) {
		logger.info("inside QuestionClientController().updateQuestion(), name is "+question);
		logger.info("url = "+ baseUrl + pathUrl); 
		logger.info("name object to update = " + question);
		
		restTemplate.put(baseUrl +pathUrl , Technology.class, question);
		Object[] objs = restTemplate.getForObject(baseUrl +pathUrl, Object[].class); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.addObject("allQuestions", objs); 
        mv.setViewName("questions/allQuestions"); 
        
        return mv; 
    }	
	
	@GetMapping("/add")
	public ModelAndView addQuestion() {
		logger.info("inside QuestionClientController().addQuestion()");
		logger.info("url = "+ baseUrl + pathUrl); 
		
        ModelAndView mv = new ModelAndView(); 
        mv.setViewName("questions/addQuestion"); 
        
        return mv; 
	}
}
