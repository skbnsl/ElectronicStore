package com.lcwd.electronic.store.config;

import io.jsonwebtoken.JwtHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.lcwd.electronic.store.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig  {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private com.lcwd.electronic.store.security.JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private com.lcwd.electronic.store.security.JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                    .anyRequest().authenticated().and()
//                    .formLogin()
//                    .loginPage("login.html")
//                    .loginProcessingUrl("/process-url")
//                    .defaultSuccessUrl("/dashboard")
//                    .failureUrl("error")
//                    .and()
//                    .logout()
//                    .logoutUrl("/do-logout");
                //this code is for basic authentication using->  httpBasic()
                    http
                    .csrf()
                    .disable()
                    .cors()
                    .disable()
                    .authorizeRequests()
                    .antMatchers("/auth/login")
                    .permitAll()
                    .antMatchers(HttpMethod.POST,"/users")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /*@Bean
    public UserDetailsService userDetailsService(){
        //create user
        //InMemoryUserDetailsManager is implementation class of UserDetails

       // UserDetails normal = User.builder().username("sagar").password(passwordEncoder().encode("123")).roles("NORMAL").build();

        //UserDetails admin = User.builder().username("naresh").password(passwordEncoder().encode("123")).roles("ADMIN").build();


        // return new InMemoryUserDetailsManager(normal,admin);
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

}
