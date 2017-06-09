package com.example.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@ComponentScan
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	private ClientDetailsService clientDetailService;

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication()
		.withUser("darren").password("123456").roles("ADMIN")
		.and()
		.withUser("larry").password("123456").roles("USER");

	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
		.anonymous().disable()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/oauth/token").permitAll();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {

		return super.authenticationManagerBean();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {

		return super.userDetailsServiceBean();
	}

	@Bean
	public TokenStore tokenStore(){

		return new InMemoryTokenStore();
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailService));
		handler.setClientDetailsService(clientDetailService);

		return handler;
	}

	@Autowired
	@Bean
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception{
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);

		return store;
	}
	
	// this causes an IllegalArgumentException ("A ServletContext is required to configure default servlet handling")
	@Bean
    public PermissionEvaluator permissionEvaluator() {
        MyPermissionEvaluator bean = new MyPermissionEvaluator();
        return bean;
    }

}
