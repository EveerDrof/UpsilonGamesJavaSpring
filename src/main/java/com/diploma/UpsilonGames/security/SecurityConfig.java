package com.diploma.UpsilonGames.security;

import com.diploma.UpsilonGames.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {;
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"**").permitAll()
                .antMatchers(HttpMethod.GET,"/games/**").permitAll()
                .antMatchers(HttpMethod.GET,"/pictures/**").permitAll()
                .antMatchers(HttpMethod.GET,"/marks/**").permitAll()
                .antMatchers(HttpMethod.GET,"/reviews/**").permitAll()
                .antMatchers(HttpMethod.POST,"/users/**").permitAll()
                .antMatchers(HttpMethod.GET,"/users/*").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/marks").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/games").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST,"/pictures/**").hasRole(UserRole.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
    @Bean
    public static BCryptPasswordEncoder encoder() {
        return  new BCryptPasswordEncoder(10);
    }
}
