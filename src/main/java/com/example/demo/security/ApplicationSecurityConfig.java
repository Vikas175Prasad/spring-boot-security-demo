package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import static com.example.demo.security.ApplicationUserRole.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.example.demo.student.Student;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter{

	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
		
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()
			.antMatchers("/","index","/css/*","/js/*").permitAll()
			.antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
	}

	@Override
	@Bean
	protected UserDetailsService userDetailsService() {// Determines how we get users details

		UserDetails jamesGordonUser = User.builder()
				.username("james")
				.password(passwordEncoder.encode("james")) // Must be encrypted using BCrypt
				.roles(STUDENT.name()) //Internally stored as ROLE_STUDENT
				.build();
		
		
		UserDetails vikasUser = User.builder()
				.username("vikas")
				.password(passwordEncoder.encode("vikas")) 
				.roles(ADMIN.name()) 
				.build();
		
		return new InMemoryUserDetailsManager(
					
						jamesGordonUser,
						vikasUser
				);
	}

}
