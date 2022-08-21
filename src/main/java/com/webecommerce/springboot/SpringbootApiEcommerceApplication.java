package com.webecommerce.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringbootApiEcommerceApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApiEcommerceApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure the resource handler to serve files uploaded with CKFinder.
        String publicFilesDir = String.format("file:%s/uploads/", System.getProperty("user.dir"));
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(publicFilesDir);
    }
}
