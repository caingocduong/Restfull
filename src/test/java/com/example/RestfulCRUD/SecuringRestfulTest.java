package com.example.RestfulCRUD;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.example.config.AppConfig;
import com.example.entities.User;
import com.example.security.AuthorizationServerConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class, RestfulCrudApplication.class})
@SpringBootTest(classes = AuthorizationServerConfiguration.class)
public class SecuringRestfulTest {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),                        
			Charset.forName("utf8")                     
			);
	@Autowired
    private WebApplicationContext wac;
 
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
 
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
          .addFilter(springSecurityFilterChain).build();
    }
    
	@Test
	public void retreiveUserUnauthorized () throws Exception {
		mockMvc.perform(get("/rest/users"))
					.andExpect(status().isUnauthorized())
					.andExpect(jsonPath("$.error", is("unauthorized")));
	}
	
	private String getAccessToken(String username, String password) throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("client_id", "my-trusted-client");
	    params.add("username", username);
	    params.add("password", password);
	 
	    ResultActions result 
	      = mockMvc.perform(post("/oauth/token")
	        .params(params)
	        .with(httpBasic("my-trusted-client","secret"))
	        .accept("application/json;charset=UTF-8"))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType("application/json;charset=UTF-8"));
	 
	    String resultString = result.andReturn().getResponse().getContentAsString();
	 
	    JacksonJsonParser jsonParser = new JacksonJsonParser();
	    return jsonParser.parseMap(resultString).get("access_token").toString();
	}
	
	@Test
	public void retreiveUserAuthorized() throws Exception {
		String accessToken = getAccessToken("darren", "123456");
		User user1 = new UserBuilder()
				.withID(1)
				.withName("Darren")
				.withAge(23)
				.withDescription("Java dev")
				.buid();
		
		mockMvc.perform(post("/rest/adduser")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON_UTF8)
					.content(new ObjectMapper().writeValueAsString(user1)))
				.andExpect(status().isCreated());
		
		mockMvc.perform(get("/rest/users")
						.header("Authorization", "Bearer "+accessToken))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Darren")))
				.andExpect(jsonPath("$.age", is(23)))
				.andExpect(jsonPath("$.description", is("Java dev")));
	}
	
	@Test
	public void usersEndpointAccessDenied() throws Exception {
		mockMvc.perform(get("/rest/users")
						.header("Authorization", "Bearer "+getAccessToken("larry", "123456")))
				.andExpect(status().isForbidden());
	}
}
