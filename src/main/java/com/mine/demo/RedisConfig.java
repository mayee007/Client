package com.mine.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import com.mine.SpringDataTest.Model.Info;

@org.springframework.context.annotation.Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
    
	@Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration 
        	= new RedisStandaloneConfiguration(redisHost, redisPort);
        redisStandaloneConfiguration.setDatabase(0);
        System.out.println("inside RedisConfig, host"+redisHost);
        System.out.println("port"+redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		  RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		  template.setEnableTransactionSupport(true);
		  template.setConnectionFactory(connectionFactory());
		  return template; 
	}
	
	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager rcm = RedisCacheManager.builder(connectionFactory())
		  //.cacheDefaults(cacheConfiguration())
		  .transactionAware()
		  .build();
		return rcm;
	}  
	
    /* 
    @Bean 
    public RedisTemplate<String, Technology> redisTemplateTechnology() {
    	RedisTemplate<String, Technology> redisTemplate = new RedisTemplate<String, Technology>(); 
    	redisTemplate.setConnectionFactory(jedisConnectionFactory());
    	return redisTemplate; 
    }

    @Bean 
    public RedisTemplate<String, Info> redisTemplateInfo() {
    	RedisTemplate<String, Info> redisTemplate = new RedisTemplate<String, Info>(); 
    	redisTemplate.setConnectionFactory(jedisConnectionFactory());
    	return redis Template; 
    }
    
    @Bean 
    public RedisTemplate<String, Problem> redisTemplateProblem() {
    	RedisTemplate<String, Problem> redisTemplate = new RedisTemplate<String, Problem>(); 
    	redisTemplate.setConnectionFactory(jedisConnectionFactory());
    	return redisTemplate; 
    }  */   
}