package com.qantas.proj.service.impl.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.qantas.proj.controller.exceptions.ServiceException;
import com.qantas.proj.pojos.User;
import com.qantas.proj.serviceinterface.BOServiceInterface;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class ServiceBOTest {

	@TestConfiguration
    static class ServiceBOTestConfig {

        @Bean
        public BOServiceInterface serviceBO() {
            return new com.qantas.proj.service.impl.ServiceBO();
        }
        
        @Bean
        public RestTemplate createMockRestTemplate() {
        	return mock(RestTemplate.class);
        }
    }
	
	@Autowired
	BOServiceInterface serviceBO;
	
	@Autowired
	RestTemplate restTemplate;
	User user;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setup() {
		System.out.println("Running the test cases");
		user = new User();
		user.setUserId("testuser");
		user.setFname("firstname");
		user.setLname("lasname");
		user.setDob(new java.sql.Date(2018,01,01));
	}
	
	private void createRoleForAuth(String role,String name) {
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn(name);
		when(authentication.getAuthorities()).thenAnswer(new Answer<Collection<SimpleGrantedAuthority>>() {

			@Override
			public Collection<SimpleGrantedAuthority> answer(InvocationOnMock invocation) throws Throwable {
				Collection<SimpleGrantedAuthority> listOfGrantedAuthorities=new ArrayList<SimpleGrantedAuthority>();
				listOfGrantedAuthorities.add(new SimpleGrantedAuthority(role));
				return (Collection<SimpleGrantedAuthority>) listOfGrantedAuthorities;
			}
			
		});
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}
	
	
	@Test
	public void createUserSuccessfullyIfNotExists() throws ServiceException {
		
		ResponseEntity<Resource<User>> mockResponse = (ResponseEntity<Resource<User>>)mock(ResponseEntity.class);
		Resource<User> resource = mock(Resource.class);
		when(resource.getContent()).thenReturn(user);
		when(mockResponse.getBody()).thenReturn(resource);
		
		when(restTemplate.exchange(Mockito.anyString(),Mockito.eq(HttpMethod.GET),Mockito.eq(null),Mockito.eq(new ParameterizedTypeReference<Resource<User>>(){}))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		
		when(restTemplate.exchange(Mockito.anyString(),Mockito.eq(HttpMethod.POST),Mockito.any(),Mockito.eq(new ParameterizedTypeReference<Resource<User>>(){}))).thenReturn(mockResponse);
		
		User createUser = serviceBO.createUser(user);
		
		assertEquals(user.getUserId(), createUser.getUserId());
	}
	
	@Test(expected=ServiceException.class)
	public void throwExceptionIfUserIdAlreadyExists() throws ServiceException {
		
		ResponseEntity<Resource<User>> mockResponse = (ResponseEntity<Resource<User>>)mock(ResponseEntity.class);
		Resource<User> resource = mock(Resource.class);
		when(resource.getContent()).thenReturn(user);
		when(mockResponse.getBody()).thenReturn(resource);
		when(restTemplate.exchange(Mockito.anyString(),Mockito.eq(HttpMethod.GET),Mockito.eq(null),Mockito.eq(new ParameterizedTypeReference<Resource<User>>(){}))).thenReturn(mockResponse);
		User createUser = serviceBO.createUser(user);
	}
	
	@Test(expected=ServiceException.class)
	public void deletUserThrowsExceptionIfNotAdmin() throws ServiceException {
		
		createRoleForAuth("ROLE_USER",user.getUserId());
	
		serviceBO.deleteUser(user.getUserId());
	}
	
	@Test
	public void deletUserSuccessfullyWithAdminRole() throws ServiceException {
		
		createRoleForAuth("ROLE_ADMIN",user.getUserId());
		
		serviceBO.deleteUser(user.getUserId());
	}
	
	@Test
	public void getUserDetailsReturnsUserSuccessfully() throws ServiceException {
		createRoleForAuth("ROLE_ADMIN",user.getUserId());
		
		ResponseEntity<Resource<User>> mockResponse = (ResponseEntity<Resource<User>>)mock(ResponseEntity.class);
		Resource<User> resource = mock(Resource.class);
		when(resource.getContent()).thenReturn(user);
		when(mockResponse.getBody()).thenReturn(resource);
		when(restTemplate.exchange(Mockito.anyString(),Mockito.eq(HttpMethod.GET),Mockito.eq(null),Mockito.eq(new ParameterizedTypeReference<Resource<User>>(){}))).thenReturn(mockResponse);
		
		User user2 = serviceBO.getUser("testuser");
		assertEquals(user.getUserId(), user2.getUserId());
	}

	@Test(expected=ServiceException.class)
	public void failToGetUserDetailsDueToNonAdminRoleAndUserNameMisMatch() throws ServiceException {
		createRoleForAuth("ROLE_USER","dummy");
		serviceBO.getUser("testuser");
	}
	

	@Test
	public void getUserDetailsReturnsUserSuccessfullyWhenUserIdMatches() throws ServiceException {
		createRoleForAuth("ROLE_USER",user.getUserId());
		
		ResponseEntity<Resource<User>> mockResponse = (ResponseEntity<Resource<User>>)mock(ResponseEntity.class);
		Resource<User> resource = mock(Resource.class);
		when(resource.getContent()).thenReturn(user);
		when(mockResponse.getBody()).thenReturn(resource);
		when(restTemplate.exchange(Mockito.anyString(),Mockito.eq(HttpMethod.GET),Mockito.eq(null),Mockito.eq(new ParameterizedTypeReference<Resource<User>>(){}))).thenReturn(mockResponse);
		
		User user2 = serviceBO.getUser("testuser");
		assertEquals(user.getUserId(), user2.getUserId());
	}
	
}
