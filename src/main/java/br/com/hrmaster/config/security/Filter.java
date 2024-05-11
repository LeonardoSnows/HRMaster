package br.com.hrmaster.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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

    @Autowired
    CustomEmployeeDetailsService CustomEmployeeDetailsService;

    @Bean
    SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        return http.csrf((AbstractHttpConfigurer::disable))
                .authorizeHttpRequests((request) -> request.requestMatchers(
                                "/h2-console/**",
                                "/hr/register",
                                "/register",
                                "/login",
                                "/do-login",
                                "/vendor/**",
                                "/css/**",
                                "/js/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                ).headers((h) -> {
                    h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                })
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/do-login")
                        .defaultSuccessUrl("/home/dashboard")
                        .failureForwardUrl("/login?error")
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

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(CustomEmployeeDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationProvider authenticationProvider = new EmployeeAuthenticationProvider();
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }
}
