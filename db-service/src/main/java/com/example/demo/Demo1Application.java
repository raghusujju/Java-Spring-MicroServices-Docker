package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.qantas.proj.jpa.entities.Address;
import com.qantas.proj.jpa.entities.User;

@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages={"com.qantas.proj.jpa"})
@EntityScan(basePackages={"com.qantas.proj.jpa.entities"})
@ComponentScan(basePackageClasses= {RepoConfigurator.class})
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

}

@SuppressWarnings("deprecation")
@Configuration
class RepoConfigurator extends 	RepositoryRestConfigurerAdapter{
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		// TODO Auto-generated method stub
		config.exposeIdsFor(User.class,Address.class);
	}
	
}
