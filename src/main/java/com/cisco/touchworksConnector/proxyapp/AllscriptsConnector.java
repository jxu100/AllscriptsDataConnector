package com.cisco.touchworksConnector.proxyapp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;

import io.swagger.annotations.ApiOperation;

import com.cisco.touchworksConnector.config.TouchworksAPI;
import com.cisco.touchworksConnector.util.HttpUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@RestController
@EnableSwagger2
@SpringBootApplication
@ConfigurationProperties("allscripts")
public class AllscriptsConnector {

	static Logger log = Logger.getLogger(AllscriptsConnector.class);
	
    public static void main(String[] args) {
        SpringApplication.run(AllscriptsConnector.class, args);
    }

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }
   
	@Value("${tw.app.url}")
	private String twAppUrl;
	
	@Value("${tw.token.url}")
	private String twTokenUrl;
	
	@Value("${pm.app.url}")
	private String pmAppUrl;
	
	@Value("${pm.token.url}")
	private String pmTokenUrl;
	
	@Value("${tw.appUser}")
	private String twAppUser;
	
	@Value("${pm.appUser}")
	private String pmAppUser;
	
	@Value("${contentType}")
	private String contentType;
	
	@Value("${tokenrequest}")
	private String  tokenRequest;
	
	private  Map<String, String> twActionMap = new HashMap<String, String>();
    
    public Map<String, String> getTwActionMap(){
    		return twActionMap;
    }
  
    private  Map<String, String> pmActionMap = new HashMap<String, String>();
    
    public Map<String, String> getPmActionMap(){
    		return pmActionMap;
    }
  
    private  Map<String, String> twDefaultMap = new HashMap<String, String>();
    
    public Map<String, String> getTwDefaultMap(){
    		return twDefaultMap;
    }
    
    private  Map<String, String> pmDefaultMap = new HashMap<String, String>();
    
    public Map<String, String> getPmDefaultMap(){
    		return pmDefaultMap;
    }
    
    @Value("${unity.request}")
    private String uRequest;
    
	private String twToken;
	private Instant twLastUpdate;
	private String pmToken;
	private Instant pmLastUpdate;
	private static final int TOKEN_TIMEOUT = 600;
	
    @ApiOperation(value = "Allscripts Unity EMR API proxy", nickname = "API Proxy")
    @RequestMapping(value = "/{emr}/{action:.+}", method = RequestMethod.GET)
    @ResponseBody
    public String handleAPI(
    		@PathVariable("emr") String emr, //support tw, pm for now
    		@PathVariable("action") String action, 
    		@RequestParam(value = "PatientID", required = false) String patient,
    		@RequestParam(value = "Parameter1", required = false) String p1,
    		@RequestParam(value = "Parameter2", required = false) String p2,
    		@RequestParam(value = "Parameter3", required = false) String p3,
    		@RequestParam(value = "Parameter4", required = false) String p4,
    		@RequestParam(value = "Parameter5", required = false) String p5,
    		@RequestParam(value = "Parameter6", required = false) String p6,
    		@RequestParam(value = "Data", required = false) String data
    		){
    	
    		String resp = "";
    		Map<String, String> actionMap;
    		Map<String, String> defaultMap;
    		String appUrl;
    		String tokenUrl;
    		String appUser;
    		
    		if("tw".equals(emr)) {
    			actionMap = twActionMap;
    			defaultMap = twDefaultMap;
    			appUrl = twAppUrl;
    			tokenUrl = twTokenUrl;
    			appUser = twAppUser;
    		}else if("pm".equals(emr)){
    			actionMap = pmActionMap;
    			defaultMap = pmDefaultMap;
    			appUrl = pmAppUrl;
    			tokenUrl = pmTokenUrl;
    			appUser = pmAppUser;
    		} else {
    			log.error("EMR " + emr + " is not supported");
    			return resp;
    		}
    		  		
    		if(!getActions(actionMap).contains(action)) {
    			log.info("illegal action : " + action);
    			return resp;
    		}
    		
    		String token = "";
    		
    		if("tw".equals(emr)) {
    			token = getToken(tokenUrl, twToken, twLastUpdate);
    		} else if("pm".equals(emr)){
    			token = getToken(tokenUrl, pmToken, pmLastUpdate);
    		}
    	
    		String payload = uRequest;
    		payload = payload.replace("%appUser%", appUser);
    		
    		if(defaultMap.containsKey(action)) {
    			//payload = api.getPayload(action);
    			payload = defaultMap.get(action);
    		} 
    			
    		payload = payload.replace("%action%", action);  		
    		payload = payload.replace("%token%", token);
    				
    		HashMap<String, String> params = new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;

			{   put("PatientID",patient);
    			    put("Parameter1",p1);
    			    put("Parameter2",p2);
    			    put("Parameter3",p3);
    			    put("Parameter4",p4);
    			    put("Parameter5",p5);
    			    put("Parameter6",p6);
    			    put("Data",data);
    			}
		};
    			
    		for (Entry<String, String> p : params.entrySet()) {
    			if (p.getValue() != null) {
    				//payload = payload.replaceAll(p.getKey() + "\":\"[a-zA-Z0-9/|()_.-]*\",", p.getKey() + "\":\"" + p.getValue() + "\",");
    				payload = payload.replaceAll(p.getKey() + "\":\"[^\"]*", p.getKey() + "\":\"" + p.getValue());
    			}
    		}
    			
    		log.debug("payload = " + payload);
    		
    		
    		try {
    			resp = HttpUtil.invokeHttpPost(appUrl, payload, contentType);
    			log.debug("raw response =" + resp);
    			if(resp != null && resp.startsWith("[")) {
    				resp = resp.substring(1, resp.length() - 1);
    			}
    		}catch (Exception e) {
    			return resp;
    		}
    		return resp;
	 }


    private String getToken(String tokenUrl, String token, Instant lastUpdate){
    		if(token == null || Instant.now().minusSeconds(TOKEN_TIMEOUT).isAfter(lastUpdate)) {
			
    			try {
    				token = HttpUtil.invokeHttpPost(tokenUrl, tokenRequest, contentType);
    				lastUpdate = Instant.now();
    			}catch (Exception e) {
    				log.error("Exception in get token " + e.getMessage());
    				token = null;
    			}   			
    		}
    		log.info("token = " + token);
    		return token;
    }
    
    public ArrayList<String> getActions(Map<String, String> actionMap){
    		ArrayList <String>actions = new ArrayList<String>();
		for(String s: actionMap.values()) {
			actions.addAll(Arrays.asList(s.split(",")));
		}
		log.debug("actions map size=" + actions.size());
		return actions;
    }
}
