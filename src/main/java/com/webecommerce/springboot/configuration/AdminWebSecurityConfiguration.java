//package com.webecommerce.springboot.configuration;
//
//import com.webecommerce.springboot.security.oauth.CustomOAuth2UserService;
//import com.webecommerce.springboot.security.oauth.OAuth2LoginSuccessHandler;
//import com.webecommerce.springboot.service.impl.UserDetailsServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//@Order(2)
//public class AdminWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Bean
//    public static PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .cors() // Ng??n ch???n request t??? m???t domain kh??c
//                .and()
//                .authorizeRequests()
//                .antMatchers("/", "/login").permitAll() // Cho ph??p t???t c??? m???i ng?????i truy c???p v??o ?????a ch??? n??y
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated() // T???t c??? c??c request kh??c ?????u c???n ph???i x??c th???c m???i ???????c truy c???p
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .defaultSuccessUrl("/admin", true)
//                .and()
//                .logout().permitAll();
//
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//                .ignoring()
//                .antMatchers("/resources/**", "/static/**", "/client/assets/**", "/admin/assets/**", "/css/**", "/js/**", "/img/**", "/icon/**");
//    }
//
//}
