package com.example.RestfulCRUD;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import com.example.config.AppConfig;
import com.example.controllers.RestfulController;
import com.example.entities.User;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, RestfulCrudApplication.class})
@WebAppConfiguration
public class RestfulCrudApplicationTests {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),                        
			Charset.forName("utf8")                     
			);
	private MockMvc mockMvc;
	
	@Mock
	private UserService userServiceMock;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Resource
	private FilterChainProxy filterChainProxy;
	
	@InjectMocks
	private RestfulController restController;
	
	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
									.addFilters(filterChainProxy).build();
		this.mockMvc = MockMvcBuilders.standaloneSetup(restController).build();
	}
	//========================Test get all users=======================================
		@Test
		public void retreiveUsers_sucess() throws Exception {
			User user1 = new UserBuilder()
							.withID(1)
							.withName("Darren")
							.withAge(23)
							.withDescription("Java dev")
							.buid();
			User user2 = new UserBuilder()
							.withID(2)
							.withName("Larry")
							.withAge(22)
							.withDescription("Android dev")
							.buid();
			when(userServiceMock.retreiveUsers()).thenReturn(Arrays.asList(user1,user2));
			
			mockMvc.perform(get("/rest/users"))
						.andExpect(status().isOk())
						.andExpect(content().contentType(APPLICATION_JSON_UTF8))
						.andExpect(jsonPath("$", hasSize(2)))
						.andExpect(jsonPath("$[0].id", is(1)))
						.andExpect(jsonPath("$[0].name", is("Darren")))
						.andExpect(jsonPath("$[0].age", is(23)))
						.andExpect(jsonPath("$[0].description", is("Java dev")))
						.andExpect(jsonPath("$[1].id", is(2)))
						.andExpect(jsonPath("$[1].name", is("Larry")))
						.andExpect(jsonPath("$[1].age", is(22)))
						.andExpect(jsonPath("$[1].description", is("Android dev")));
			
			verify(userServiceMock, times(1)).retreiveUsers();
		}
		
		//========================Test add user=======================================
		@Test
		public void addUser_sucess() throws Exception {
			User user = new UserBuilder()
					.withID(3)
					.withName("Ella")
					.withAge(23)
					.buid();
			when(userServiceMock.isExistUser(user.getId())).thenReturn(false);
			doNothing().when(userServiceMock).addUser(user);
			
			mockMvc.perform(post("/rest/adduser")
							.contentType(APPLICATION_JSON_UTF8)
							.content(new ObjectMapper().writeValueAsString(user)))
						.andExpect(status().isCreated())
						.andExpect(content().contentType(APPLICATION_JSON_UTF8))
						.andExpect(jsonPath("$.id", is(3)))
			            .andExpect(jsonPath("$.name", is("Ella")))
			            .andExpect(jsonPath("$.age", is(23)));
			
			ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
			verify(userServiceMock, times(1)).isExistUser(user.getId());
			verify(userServiceMock, times(1)).addUser(userCaptor.capture());
			
			User u = userCaptor.getValue();
			assertThat(u.getId(), is(3));
			assertThat(u.getName(), is("Ella"));
			assertThat(u.getAge(), is(23));
		}
		
	@Test
	public void addUser_fail() throws Exception{
		User user = new UserBuilder()
				.withID(3)
				.withName("Ella")
				.withAge(23)
				.buid();
		
		when(userServiceMock.isExistUser(user.getId())).thenReturn(true);
		
		mockMvc.perform(post("/rest/adduser")
					.contentType(APPLICATION_JSON_UTF8)
					.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isConflict());	
		
		verify(userServiceMock, times(1)).isExistUser(user.getId());
		
	}
	//========================Test delete user=======================================
	@Test
	public void deleteUser_success() throws Exception {
		User user = new UserBuilder()
				.withID(1)
				.withName("Darren")
				.withAge(23)
				.withDescription("Java dev")
				.buid();
		int userID = user.getId();

		when(userServiceMock.isExistUser(userID)).thenReturn(true);
		when(userServiceMock.deleteUser(userID)).thenReturn(user);

		mockMvc.perform(delete("/rest/delete/{id}", userID))
					.andExpect(status().isOk())
					.andExpect(content().contentType(APPLICATION_JSON_UTF8))
					.andExpect(jsonPath("$.id", is(1)))
					.andExpect(jsonPath("$.name", is("Darren")))
					.andExpect(jsonPath("$.age", is(23)))
					.andExpect(jsonPath("$.description", is("Java dev")));
		
		verify(userServiceMock, times(1)).isExistUser(userID);
		verify(userServiceMock, times(1)).deleteUser(userID);		
	}

	@Test
	public void deleteUser_fail() throws Exception{
		User user = new UserBuilder()
				.withID(1000)
				.buid();
		int userID = user.getId();
		when(userServiceMock.isExistUser(userID)).thenReturn(false);

		mockMvc.perform(delete("/rest/delete/{id}", userID))
					.andExpect(status().isNotFound());
		
		verify(userServiceMock, times(1)).isExistUser(userID);	
	}
	//========================Test update user=======================================
	@Test
	public void updateUser_success() throws Exception{
		User user = new UserBuilder()
				.withID(1)
				.withName("Darren")
				.withAge(15)
				.buid();
		int userID = user.getId();
		
		when(userServiceMock.isExistUser(userID)).thenReturn(true);
		when(userServiceMock.updateUser(user)).thenReturn(user);

		mockMvc.perform(put("/rest/updateuser/{id}", userID)
						.contentType(APPLICATION_JSON_UTF8)
						.content(new ObjectMapper().writeValueAsString(user)))
					.andExpect(status().isOk());
		
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userServiceMock, times(1)).isExistUser(user.getId());
		verify(userServiceMock, times(1)).updateUser(userCaptor.capture());
		
		User u = userCaptor.getValue();
		assertThat(u.getId(), is(1));
		assertThat(u.getName(), is("Darren"));
		assertThat(u.getAge(), is(15));
	}
	
	//========================Test Restful API with Spring OAuth2=======================================
	@Test
	public void retreiveUserUnauthorized () throws Exception {
		mockMvc.perform(get("/rest/users"))
					.andExpect(status().isUnauthorized())
					.andExpect(jsonPath("$.error", is("unauthorized")));
	}
	
	private String getAccessToken(String username, String password) throws Exception {
		String authorization = "Basic "
				+ new String(Base64Utils.encode("my-trusted-client:secret".getBytes()));
		String content = mockMvc.perform(post("/oauth/token")
											.header("Authorization", authorization)
											.contentType(APPLICATION_JSON_UTF8)
											.param("username", username)
											.param("password", password)
											.param("grant_type", "password")
											.param("client_id", "my-trusted-client")
											.param("client_secret", "123456"))
								.andExpect(status().isOk())
								.andExpect(content().contentType(APPLICATION_JSON_UTF8))
								.andExpect(jsonPath("$.access_token", is(notNullValue())))
								.andExpect(jsonPath("$.token_type", is("bearer")))
								.andExpect(jsonPath("$.refresh_token", is(notNullValue())))
								.andExpect(jsonPath("$.expires_in", is(greaterThan(500))))
								.andExpect(jsonPath("$.scope", is("read write trust")))
								.andReturn().getResponse().getContentAsString();

		return content.substring(17, 53);
	}
	
	@Test
	public void retreiveUserAuthorized() throws Exception {
		String accessToken = getAccessToken("darren", "123456");
		
		mockMvc.perform(get("/rest/users")
						.header("Authorization", "Bearer "+accessToken))
				.andExpect(status().isOk());
	}
	
	@Test
	public void usersEndpointAccessDenied() throws Exception {
		mockMvc.perform(get("/rest/users")
						.header("Authorization", "Bearer "+getAccessToken("larry", "123456")))
				.andExpect(status().isForbidden());
	}
}
