package br.com.chunkuploadserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private UserDetailsService userDetailsService;
	private TokenAuthenticationService tokenAuthenticationService;
	

	public SecurityConfig(UserDetailsService userDetailsService) {
		super();
		this.userDetailsService = userDetailsService;
		this.tokenAuthenticationService = new TokenAuthenticationService("tooManySecrets", userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		StatelessLoginFilter statelessLoginFilter = new StatelessLoginFilter("/api/login", 
				tokenAuthenticationService, userDetailsService, authenticationManager());
		
		StatelessAuthenticationFilter statelessAuthenticationFilter = 
				new StatelessAuthenticationFilter(tokenAuthenticationService);
		
		http.csrf().disable().authorizeRequests()
			.antMatchers("/api/public/login").permitAll()
			.antMatchers(HttpMethod.POST, "/api/login").permitAll()
			.antMatchers(HttpMethod.POST, "/api/newAccount").permitAll()
			.antMatchers("/swagger-ui.html").permitAll()
			.antMatchers("/webjars/**").permitAll()
			.antMatchers("/swagger-resources/**").permitAll()
			.antMatchers("/v2/api-docs/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(statelessLoginFilter, UsernamePasswordAuthenticationFilter.class)		
			.addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.inMemoryAuthentication()
			.withUser("admin")
			.password("admin")
			.roles("ADMIN");
	}

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public TokenAuthenticationService tokenAuthenticationService() {
        return tokenAuthenticationService;
    }
    
    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
	
}
