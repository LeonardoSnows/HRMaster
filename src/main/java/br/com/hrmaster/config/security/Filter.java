package br.com.hrmaster.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Filter {

    @Bean
    SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        return http.csrf((AbstractHttpConfigurer::disable))
                .authorizeHttpRequests((request) -> request.requestMatchers(
                                "/h2-console/**",
                                "/hr/register/employee",
                                "/hr/pgforgot_password",
                                "/register/employee",
                                "/forgot-password",
                                "/resetPassword/**",
                                "/hr/resetPassword/**",
                                "/vendor/**",
                                "/css/**",
                                "/js/**"
                                )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                ).headers((h) -> {
                    h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                })
                .formLogin(login -> login
                        .loginPage("/hr/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/do-login")
                        .defaultSuccessUrl("/hr/dashboard", true)
                        .permitAll())

                .logout(LogoutConfigurer::permitAll)
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomEmployeeDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
