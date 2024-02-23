package com.cl.clapp.config;

import javax.annotation.Resource;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecuriyConfig{
    
    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
        
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

     @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
     /*   httpSecurity.authorizeHttpRequests()
        .antMatchers("/registeremployee").hasAuthority("ADMIN")
        .antMatchers("/applyLeave").hasAnyAuthority("USER","ADMIN")
        .antMatchers("/mockData").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .and()
        .authenticationProvider(daoAuthenticationProvider()); */
        httpSecurity
        .authorizeHttpRequests()
        .antMatchers("/getmysubordinatesleaves","/getmysubordinates","/getemployee").hasAuthority("ADMIN")
        .antMatchers("/getMogetMockData","/calculateLeaveDuration","/getProperties","/getMyLeaves").hasAnyAuthority("USER","ADMIN")
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .and()
        .httpBasic()
        .and()
        .authenticationProvider(daoAuthenticationProvider()); 
        
        //,"/getmysubordinates","/getmysubordinatesleaves","/updateleave"
        return httpSecurity.build();
    }
}
