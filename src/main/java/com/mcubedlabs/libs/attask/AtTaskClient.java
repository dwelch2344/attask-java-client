package com.mcubedlabs.libs.attask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.mcubedlabs.libs.attask.model.Project;
import com.mcubedlabs.libs.attask.model.Task;

/**
 * The client for accessing an AtTask instance. 
 * 
 * @author dwelch2344
 *
 */
@Slf4j
public class AtTaskClient {

	private final String host, user, password;

	private final RestTemplate rt;
	private LoginResponse loginData;

	public AtTaskClient(String host, String user, String password) {
		super();
		this.host = host;
		this.user = user;
		this.password = password;

		this.rt = new RestTemplate();
		
		MappingJackson2HttpMessageConverter jackson = new MappingJackson2HttpMessageConverter();
		jackson.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		jackson.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(jackson);
		rt.setMessageConverters(converters);
	}

	public List<Project> getProjects(){
		List<Project> projects = request("/project/search", HttpMethod.GET, TYPE_LIST_PROJECT, null);
		return projects;
	}
	
	public List<Task> getTasksByProject(String projectId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("projectID", projectId);
		List<Task> tasks = request("/task/search", HttpMethod.GET, TYPE_LIST_TASK, params);
		return tasks;
	}
	
	protected <R> R request(String path, HttpMethod method, ParameterizedTypeReference<Envelope<R>> typeRef, Map<String, String> params){
		// TODO handle session timeouts
		if(loginData == null){
			login();
		}
		
		String url = this.host + path + (path.contains("?") ? "&" : "?") + "sessionID={sid}";
		params = params == null ? new HashMap<String, String>() : params;
		params.put("sid", loginData.getSessionID());
		
		for(String key : params.keySet()){
			if( !"sid".equals(key) ){
				url += String.format("&%s={%s}", key, key);
			}
		}
		
		log.trace("Making {} request to {}", method, url);
		
		Envelope<R> response = rt.exchange(url, method, null, typeRef, params).getBody();
		return response.getData();
	}
	
	protected void login() {
		log.trace("Logging in");

		@SuppressWarnings("serial")
		HashMap<String, String> params = new HashMap<String, String>() {{
			put("u", user);
			put("p", password);
		}};

		String url = this.host+ "/login?username={u}&password={p}";
		ParameterizedTypeReference<Envelope<LoginResponse>> responseType = new ParameterizedTypeReference<Envelope<LoginResponse>>(){};
		Envelope<LoginResponse> response = rt.exchange(url, HttpMethod.GET, null, responseType, params).getBody();
		
		log.trace("Logged in: {}", response.getData());
		
		loginData = response.getData();
	}
	
	// Workaround list-types for nested generics. Ugly, I know
	private static final ParameterizedTypeReference<Envelope<List<Project>>> TYPE_LIST_PROJECT = new ParameterizedTypeReference<Envelope<List<Project>>>(){};
	private static final ParameterizedTypeReference<Envelope<List<Task>>> TYPE_LIST_TASK = new ParameterizedTypeReference<Envelope<List<Task>>>(){};
	
}
