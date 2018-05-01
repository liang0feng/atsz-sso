package com.atsz.manager.service.redis;

public interface RedisService {

	 //set
    public String set(String key,String value);
    
    //get
    public String get(String key);
    
    //del
    public Long del(String key);
    
    //expire
    public Long expire(String key,Integer seconds);
    
    //expire
    public Long expire(String key,String value,Integer seconds);
	
	
}
