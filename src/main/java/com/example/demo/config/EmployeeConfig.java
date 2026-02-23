package com.example.demo.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class EmployeeConfig {
	
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public EmployeeConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
         http
             .csrf(csrf->csrf.disable())
             .authorizeHttpRequests(authorizeRequests ->authorizeRequests
	              .requestMatchers("/api/v1/addEmployee","/welcome","/api/loans/emp/processToKafka").permitAll()
	              .anyRequest()// Allow access to public and login pages // Require ADMIN role for admin paths
	              .authenticated() // Authenticate all other requests
                 ).exceptionHandling(exception -> exception
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                 );
		   return http.build();
                
    }
	
}
