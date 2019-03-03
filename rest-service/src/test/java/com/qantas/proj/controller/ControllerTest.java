package com.qantas.proj.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import com.qantas.proj.config.application.RestServiceApplication;
import com.qantas.proj.controller.exceptions.ServiceException;
import com.qantas.proj.pojos.User;
import com.qantas.proj.service.impl.ServiceBO;
import com.qantas.proj.serviceinterface.BOServiceInterface;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RestServiceApplication.class)
@WebMvcTest(Controller.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ControllerTest {

	MockMvc mvc;

	 @Autowired
	    private WebApplicationContext wac;

	@InjectMocks Controller controller;
	 

	@TestConfiguration
    static class ControllerCofig{
		@Primary
		@Bean
		public BOServiceInterface service() {
			return mock(ServiceBO.class);
		}
	}
	
	@Autowired
	BOServiceInterface serviceBO;

	User user;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		//ReflectionTestUtils.setField( mvc, "Controller", controller );
		System.out.println("Running the test cases");
		user = new User();
		user.setUserId("testuser");
		user.setFname("firstname");
		user.setLname("lasname");
		user.setDob(new java.sql.Date(2018,01,01));
	}
	
	@Test
	public void throwExceptionForGetUserWithId() throws Exception {

		when(serviceBO.getUser(Mockito.anyString())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		ResultActions performGet = mvc.perform(get("/qantas/userapp/user/{userId}","testuser"));
		performGet.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void handleServiceExceptionForGetUserWithId() throws Exception {

		String ERROR_MESSAGE = "test error";
		when(serviceBO.getUser(Mockito.anyString())).thenThrow(new ServiceException(ERROR_MESSAGE));
		ResultActions performGet = mvc.perform(get("/qantas/userapp/user/{userId}","testuser"));
		performGet.andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message", is(ERROR_MESSAGE)));
	}

	@Test
	public void returnUserJSONExistingUser() throws Exception {
		when(serviceBO.getUser(Mockito.anyString())).thenReturn(user);
		ResultActions perform = mvc.perform(get("/qantas/userapp/user/{userId}",user.getUserId()));
		
		MvcResult response = perform.andExpect(status().isOk()).andExpect(jsonPath("$.userId", is(user.getUserId()))).andReturn();
		MockHttpServletResponse mvcresponse = response.getResponse();
		
		assertTrue(mvcresponse.getContentType().contains(ContentType.APPLICATION_JSON.getMimeType()));
		
	}
	
	@Test
	public void getAllUsersSuccessfully() throws Exception {

		ArrayList<User> value = new ArrayList<User>();
		value.add(user);
		value.add(user);
		when(serviceBO.getAllUsers()).thenReturn(value);
		 MvcResult andReturn = mvc.perform(get("/qantas/userapp/user/")).andExpect(status().isOk())
				 .andExpect(jsonPath("[0].userId", is(user.getUserId())))
				 .andExpect(jsonPath("[1].userId", is(user.getUserId())))
				 .andReturn();
		 MockHttpServletResponse response = andReturn.getResponse();
		 assertTrue(response.getContentType().contains(ContentType.APPLICATION_JSON.getMimeType()));
	}
	
	@Test
	public void throwExceptionwhengetAllUsersThrowsServiceException() throws Exception {
		ArrayList<User> value = new ArrayList<User>();
		value.add(user);
		value.add(user);
		when(serviceBO.getAllUsers()).thenThrow(new ServiceException("Invalid Operation"));;
		 mvc.perform(get("/qantas/userapp/user/")).andExpect(status().is4xxClientError());
	}
}
