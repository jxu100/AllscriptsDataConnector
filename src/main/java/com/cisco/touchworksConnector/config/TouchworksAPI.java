package com.cisco.touchworksConnector.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource("classpath:apiconfig.properties")
@ConfigurationProperties("touchworks")
public class TouchworksAPI {
	
	static Logger log = Logger.getLogger(TouchworksAPI.class);
	
	@Value("${app.url}")
	private String appName;
	
    private  Map<String, String> actionMap = new HashMap<String, String>();
    
    public Map<String, String> getActionMap(){
    		return actionMap;
    }
  
    public  String getPayload(String key) {
    		if(actionMap == null || actionMap.size() == 0) {
			log.error("failed to load api config");
			return "";
		}
    		return actionMap.get(key);
    }
    
    public  boolean hasAction(String key) {
    	log.info("appname=" + appName);
    		if(actionMap == null || actionMap.size() == 0) {
    			log.error("failed to load api config");
    			return false;
    		}
    		return actionMap.containsKey(key);
    }
}