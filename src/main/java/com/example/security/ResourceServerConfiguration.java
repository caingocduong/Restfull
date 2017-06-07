package com.example.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{
	private static final  String RESOURCE_ID = "my_rest_api";
	
	public void configure(ResourceServerSecurityConfigurer resources){
		resources.resourceId(RESOURCE_ID).stateless(false);
	}
	
	public void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity.anonymous().disable()
					.requestMatchers().antMatchers("/rest/**")
					.and().authorizeRequests()
					.antMatchers("/rest/**").access("hasRole('ADMIN')")
					.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}
