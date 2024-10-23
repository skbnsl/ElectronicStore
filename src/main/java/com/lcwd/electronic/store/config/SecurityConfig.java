package com.lcwd.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    //securityFilterChains beans
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        //configurations

        //disbaling cors
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        //disbaling csrf
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        /*url-public,private,protected ** admin/user */
        security.authorizeHttpRequests(request->{

             request.antMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
                     .antMatchers(HttpMethod.PUT,"/users/**").hasAnyRole("ADMIN","NORMAL")
                     .antMatchers(HttpMethod.GET,"/products/**").permitAll()
                     .antMatchers("/products/**").hasRole("ADMIN")
                     .antMatchers(HttpMethod.GET,"/users/**").permitAll()
                     .antMatchers(HttpMethod.GET,"/categories/**").permitAll()
                     .antMatchers("/categories/**").hasRole("ADMIN");

        });

        //security type
        security.httpBasic(Customizer.withDefaults());
        return security.build();
    }

//password encoder
@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}

}