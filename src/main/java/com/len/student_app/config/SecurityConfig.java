package com.len.student_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.len.student_app.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	   @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http)
	            throws Exception {

	        http
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/auth/register").permitAll()
	                .requestMatchers(
	                        HttpMethod.POST, "/students"
	                    ).hasRole("ADMIN")

	                    .requestMatchers(
	                        HttpMethod.PUT, "/students/**"
	                    ).hasRole("ADMIN")

	                    .requestMatchers(
	                        HttpMethod.DELETE, "/students/**"
	                    ).hasRole("ADMIN")

	                    
	                    .requestMatchers(
	                        HttpMethod.GET, "/students", "/students/**"
	                    ).hasAnyRole("USER", "ADMIN")

	                    .anyRequest().authenticated()
	            )
	            .httpBasic(Customizer.withDefaults());

	        return http.build();
	    }
	   @Bean
	   public AuthenticationProvider authenticationProvider(
	           CustomUserDetailsService userDetailsService,
	           PasswordEncoder passwordEncoder) {

	       DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

	       provider.setUserDetailsService(userDetailsService);
	       provider.setPasswordEncoder(passwordEncoder);

	       return provider;
	   }
}
