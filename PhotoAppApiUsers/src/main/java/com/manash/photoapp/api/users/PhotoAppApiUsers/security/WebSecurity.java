package com.manash.photoapp.api.users.PhotoAppApiUsers.security;

import com.manash.photoapp.api.users.PhotoAppApiUsers.service.UsersService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableWebSecurity
//public class WebSecurity {
//	private Environment environment;
//	private UsersService usersService;
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//	@Autowired
//	public WebSecurity(Environment environment, UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder)
//	{
//		this.environment = environment;
//		this.usersService = usersService;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//	}
//	@Bean
//	protected SecurityFilterChain configure(HttpSecurity http) throws Exception{
//		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//		authenticationManagerBuilder.userDetailsService(usersService)
//			.passwordEncoder(bCryptPasswordEncoder);
//		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//		http.csrf(csrfConfigurer -> {
//				csrfConfigurer.disable();
//				csrfConfigurer.ignoringRequestMatchers(PathRequest.toH2Console());
//			}
//		);
//
//
//
//		http.authorizeHttpRequests(auth ->
//			auth
//				.requestMatchers(
//					PathRequest.toH2Console(),
//					AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/users/signup")
//
//				)
//				.permitAll()
////
//				.requestMatchers(new AntPathRequestMatcher("/users/**")).access(new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')"))
//				.anyRequest().authenticated()
//		);
//
//
//		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//		http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));
//
//		return http.build();
//	}
//
//}

@Configuration
@EnableWebSecurity
public class WebSecurity {

	private Environment environment;
	private UsersService usersService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(Environment environment, UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.environment = environment;
		this.usersService = usersService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}



	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		// Configure AuthenticationManagerBuilder
		AuthenticationManagerBuilder authenticationManagerBuilder =
			http.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(usersService)
			.passwordEncoder(bCryptPasswordEncoder);

		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		// Create AuthenticationFilter
		AuthenticationFilter authenticationFilter =
			new AuthenticationFilter(usersService, environment, authenticationManager);
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

		http.csrf((csrf) -> csrf.disable());

		http.authorizeHttpRequests((authz) -> authz
				.requestMatchers(
										PathRequest.toH2Console(),
										AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/users/signup"),
					AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/actuator/**")
									)
									.permitAll()
				.requestMatchers(new AntPathRequestMatcher("/users/**"))
				.permitAll()
				.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())

			.addFilter(authenticationFilter)
			.authenticationManager(authenticationManager)
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));
		return http.build();

	}
}