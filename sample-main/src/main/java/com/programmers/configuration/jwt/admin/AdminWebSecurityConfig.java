package com.programmers.configuration.jwt.admin;

import com.programmers.configuration.jwt.WebSecurityConfigBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
@EnableWebSecurity
public class AdminWebSecurityConfig extends WebSecurityConfigBase {

    @Autowired
    private AdminJwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private AdminJwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // We don't need CSRF
        httpSecurity.antMatcher("/admin/**").csrf().disable().authorizeRequests().antMatchers("/admin/login")
                .permitAll()
                // all the above requests need to be authenticated
                .and().sessionManagement()
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // rest all requests are not for this security group
                // so leave them as is
                .and().authorizeRequests().antMatchers("/**").permitAll();
        httpSecurity.cors();
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}