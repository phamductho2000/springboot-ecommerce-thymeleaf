package com.webecommerce.springboot.configuration;

import com.webecommerce.springboot.security.CustomAccessDeniedHandler;
import com.webecommerce.springboot.security.oauth.CustomOAuth2UserService;
import com.webecommerce.springboot.security.oauth.OAuth2LoginSuccessHandler;
import com.webecommerce.springboot.service.RoleService;
import com.webecommerce.springboot.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Configuration
    @Order(1)
    public static class AdminWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private UserDetailsServiceImpl userDetailsService;

        @Bean
        public static PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/admin/**")
                    .authorizeRequests()
                    .antMatchers("/admin/login").permitAll()
                    .antMatchers("/logout").permitAll()
                    .anyRequest()
                    .hasAuthority("ADMIN")

                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/admin/login")
                    .failureUrl("/login?error=loginError")
                    .defaultSuccessUrl("/admin/dashboard", true)

                    .and()
                    .logout()
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")

                    .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())

                    .and()
                    .cors().and().csrf().disable();

        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public AccessDeniedHandler accessDeniedHandler() {
            return new CustomAccessDeniedHandler();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring() 
                    .antMatchers("/resources/**", "/static/**", "/client/assets/**", "/admin/assets/**", "/css/**", "/js/**", "/img/**", "/icon/**");
        }

    }

    @Configuration
    @Order(2)
    public static class ClientWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private UserDetailsServiceImpl userDetailsService;

        @Autowired
        private CustomOAuth2UserService oAuth2UserService;

        @Autowired
        private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

        @Bean
        public static PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/tai-khoan/**", "/thanh-toan").hasRole("USER")

                    .and()
                    .formLogin()
                    .loginPage("/dang-nhap")
                    .loginProcessingUrl("/user_login")
                    .failureUrl("/dang-nhap?error=loginError")
                    .defaultSuccessUrl("/", true)

                    .and()
                    .logout()
                    .logoutUrl("/user_logout")
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")

                    .and()
                    .oauth2Login()
                    .loginPage("/dang-nhap")
                    .userInfoEndpoint().userService(oAuth2UserService)
                    .and()
                    .successHandler(oAuth2LoginSuccessHandler)

                    .and()
                    .logout()
                    .logoutUrl("/user_logout")
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")

                    .and()
                    .cors().and().csrf().disable();

        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring()
                    .antMatchers("/resources/**", "/static/**", "/client/assets/**", "/admin/assets/**", "/css/**", "/js/**", "/img/**", "/icon/**");
        }

    }

}

