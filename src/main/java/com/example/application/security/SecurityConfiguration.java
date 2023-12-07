//package com.example.application.security;
//
//import com.example.application.views.LoginView;
//import com.vaadin.flow.spring.security.VaadinWebSecurity;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@EnableWebSecurity
//@EnableMethodSecurity
//@Configuration
//public class SecurityConfiguration extends VaadinWebSecurity {
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/agreements/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/docs/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/signin/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll());
//        http.formLogin(form -> form.loginPage("/signin").permitAll());
//        http.csrf((csrf) -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**")));
//        super.configure(http);
//        setLoginView(http, LoginView.class);
//
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
//            throws Exception {
//        return config.getAuthenticationManager();
//    }
//}
