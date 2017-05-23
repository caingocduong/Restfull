package com.example.config;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
@Configuration
public class AppConfig {
	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://localhost:3306/UserData?useLegacyDatetimeCode=false&serverTimezone=UTC");
	    dataSource.setUsername("root");
	    dataSource.setPassword("caingocduong94");
	    return dataSource;
	}
	@Bean
	public EntityManager entityManager() {
	    return entityManagerFactory().getObject().createEntityManager();
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	    em.setDataSource(dataSource());
	    em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
	    em.setPackagesToScan("com.example.dao");
	    return em;
	}
}
