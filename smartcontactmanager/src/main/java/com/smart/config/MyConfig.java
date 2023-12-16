package com.smart.config;


import org.springframework.context.annotation.Bean;   
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter  {
	@Bean
	public UserDetailsService  getUserDetailsService()
	{
		return new UserDetailsServiceImpl();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoauthenticationProvider=new DaoAuthenticationProvider();
		daoauthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoauthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoauthenticationProvider;
	}
	
	///Configuration Method
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	     auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/**")
			 .hasRole("ADMIN")
		     .antMatchers("/user/**")
		     .hasRole("USER")
		     .antMatchers("/**")
		     .permitAll()
		     .and()
		     .formLogin()
      	     //.loginPage("/signin")
      	     .loginProcessingUrl("/dologin")
      	     .defaultSuccessUrl("/user/index")
      	   //  .failureUrl("/login-fail")
		     .and()
		     .csrf()
		     .disable(); 
	}
	
	
	
//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider daoauthenticationProvider=new DaoAuthenticationProvider();
//		daoauthenticationProvider.setUserDetailsService(this.getUserDetailsService());
//		daoauthenticationProvider.setPasswordEncoder(passwordEncoder() );
//		return daoauthenticationProvider; 
//	}
//	
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
//	{
//		return configuration.getAuthenticationManager();
//	}
//
//	     
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//	{
//        http
//                .authorizeHttpRequests((authorize) -> authorize
//                        .antMatchers("/admin/**")
//                        .hasRole("ADMIN")
//                        .antMatchers("/user/**")
//                        .hasRole("USER")
//                        .antMatchers("/**")
//                        .permitAll().anyRequest().authenticated())
//                        
//                .formLogin(login -> login
//                        .loginPage("/signin")
//                        .loginProcessingUrl("/dologin")
//                        .defaultSuccessUrl("/user/index"));
//	 
//		http.authenticationProvider(authenticationProvider());
//		DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
//		return defaultSecurityFilterChain;
//		  
//	}  
//
//	
}
