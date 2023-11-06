package com.rakshi.t4.security;

import com.rakshi.t4.config.ApiFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService service;
    private final AuthenticationEntryPoint entryPoint;
    private final AccessDeniedHandler handler;
    private final ApiFilter filter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(getEncoder());
    }

    @Bean
    PasswordEncoder getEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.formLogin().disable().httpBasic().disable();
        http.authorizeHttpRequests().antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/auth/seller/**").hasRole("SELLER")
                .antMatchers("/api/auth/consumer/**").hasRole("CONSUMER")
                .anyRequest().authenticated();

        http.exceptionHandling().accessDeniedHandler(handler).authenticationEntryPoint(entryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
