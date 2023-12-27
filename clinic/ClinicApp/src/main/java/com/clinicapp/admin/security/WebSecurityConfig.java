package com.clinicapp.admin.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public UserDetailsService UserDetailsService()
	{
		return new UsersDetailsService();
	}
	
	
	@Bean
	public PasswordEncoder nopasswordEncoder() {
	    return PlainTextPasswordEncoder.getInstance();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	     
	    authProvider.setUserDetailsService(UserDetailsService());
	    authProvider.setPasswordEncoder(nopasswordEncoder());
	 
	    return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(
	        AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


		http.authenticationProvider(authenticationProvider());
		
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/register").permitAll()
        		.requestMatchers("/create_patient").permitAll()
        		.requestMatchers("/verify**").permitAll()
        		.requestMatchers("/login").permitAll()
        		.requestMatchers("/users/**").hasAuthority("Admin")
        		.requestMatchers("/prescription/**").hasAnyAuthority("Doctor", "Pharmacist")
        		.requestMatchers("/prescription/verify**").hasAuthority("Pharmacist")
        		.requestMatchers("/prescription_history/**").hasAuthority("Patient")
        		.requestMatchers("/medicine/").hasAnyAuthority("Suppiler", "Admin")
                .anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login")
                    .usernameParameter("email")
                    .permitAll())
                .rememberMe(remenber -> remenber.key("AbcdEfghIjklmNopQrsTuvXyz_0123456789"))
                .logout(logout -> logout.permitAll());
        
        http.headers(head -> head.frameOptions(frame -> frame.sameOrigin()));
		//http.authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
 
        return http.build();
    }
	
	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("*"));
	        configuration.setAllowedMethods(Arrays.asList("*"));
	        configuration.setAllowedHeaders(Arrays.asList("*"));
	        configuration.setAllowCredentials(true);
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }

 
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
	     
}
