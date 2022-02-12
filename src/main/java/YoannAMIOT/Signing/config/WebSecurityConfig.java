package YoannAMIOT.Signing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import YoannAMIOT.Signing.Authentication.AppAuthProvider;
import YoannAMIOT.Signing.Authentication.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserService userDetailsService;
	
	/**
	 * {@link https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-authentication-form}
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/", "/login", "/studentHome", "/addUser", "/loginDispatcher", "/fail", "/teacherHome", "/teacherClassroom/**", "/createSchoolDay/**", "/headmaster/**", "/fake").permitAll()
				.antMatchers(HttpMethod.POST, "/login", "/addUser","/morningSigning","/afternoonSigning", "/countersign/*", "/fake").permitAll()
				.antMatchers("/css/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.authenticationProvider(getProvider())
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/loginDispatcher")
				.failureUrl("/fail")
				.permitAll()
				.and()
            .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .permitAll();
	}
	
	/**
	 * {@link https://docs.spring.io/spring-security/site/docs/current/reference/html5/#authentication-password-storage-bcrypt}
	 * @return
	 */
	@Bean 
	public PasswordEncoder passwordEncoder() { 
	    return new BCryptPasswordEncoder(); 
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Bean
	public AuthenticationProvider getProvider() {
		AppAuthProvider appAuthProvider = new AppAuthProvider();
		appAuthProvider.setUserDetailsService(userDetailsService());
		return appAuthProvider;
	}
}
