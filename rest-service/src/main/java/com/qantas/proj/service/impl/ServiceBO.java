package com.qantas.proj.service.impl;

import static com.qantas.proj.utils.Utils.ROLE_ADMIN;
import static com.qantas.proj.utils.Utils.getUserFromAuth;
import static com.qantas.proj.utils.Utils.isRole;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.qantas.proj.controller.exceptions.ServiceException;
import com.qantas.proj.pojos.User;
import com.qantas.proj.serviceinterface.BOServiceInterface;

@Service
public class ServiceBO implements BOServiceInterface{

	@Autowired
	RestTemplate restTemplate;
	@Value("${dbServiceUrl}")
	private String dbServiceUrl;

	public User createUser(User user) throws ServiceException {

		if( isUserExists(user.getUserId())) {
			throw new ServiceException("User name already exists.Please select a different username");
		}
		
		ResponseEntity<Resource<User>> exchange = restTemplate.exchange(dbServiceUrl,HttpMethod.POST,new HttpEntity<User>(user), new ParameterizedTypeReference<Resource<User>>(){});
		User content = exchange.getBody().getContent();
		return content;

	}

	private boolean isUserExists(String userid) throws ServiceException {

		try {
			ResponseEntity<Resource<User>> forObject = restTemplate.exchange(dbServiceUrl+"/"+userid,HttpMethod.GET,null, new ParameterizedTypeReference<Resource<User>>(){});
			if( forObject.getBody().getContent() != null) {
				return true;
			}
			return false;
		}catch(HttpClientErrorException e) {
			if( e.getStatusCode() == HttpStatus.NOT_FOUND) {
				return false;
			}
			throw e;
		}
		
	}

	public Collection<User> getAllUsers() throws ServiceException{

		if( !isRole(ROLE_ADMIN)) {
			throw new ServiceException("Unauthorized Operation");
		}
		try {
			ResponseEntity<Resources<User>> rateResponse =
					restTemplate.exchange(dbServiceUrl,
							HttpMethod.GET, null, new ParameterizedTypeReference<Resources<User>>(){});

			Resources<User> body = rateResponse.getBody();
			Collection<User> content = body.getContent();
			return content;
		}
		catch(Exception e) {
			throw new ServiceException("Unauthorized Operation",e.getCause());
		}
	}

	public User getUser(String userid) throws ServiceException {

		if( isRole(ROLE_ADMIN) || getUserFromAuth().equals(userid)) {
			ResponseEntity<Resource<User>> forObject = restTemplate.exchange(dbServiceUrl+"/"+userid,HttpMethod.GET,null, new ParameterizedTypeReference<Resource<User>>(){});
			return forObject.getBody().getContent();
		}
		throw new ServiceException("Unauthorized operation");
	}

	public User updateUser(User user) throws ServiceException {
		if( isUserExists(user.getUserId()) && (isRole(ROLE_ADMIN)|| getUserFromAuth().equals(user.getUserId()))) {
			ResponseEntity<Resource<User>> exchange = restTemplate.exchange(dbServiceUrl,HttpMethod.POST,new HttpEntity<User>(user), new ParameterizedTypeReference<Resource<User>>(){});
			User content = exchange.getBody().getContent();
			return content;
		}

		throw new ServiceException("Invalid user operation");
	}

	public void deleteUser(String userId) throws ServiceException {

		if(!isRole(ROLE_ADMIN)) {
			throw new ServiceException("Unauthorised operation");
		}
		restTemplate.delete(dbServiceUrl+"/"+userId);

	}
}
