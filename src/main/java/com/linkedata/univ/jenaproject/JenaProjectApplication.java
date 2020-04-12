package com.linkedata.univ.jenaproject;

import com.linkedata.univ.jenaproject.services.QueryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.management.Query;

@SpringBootApplication
public class JenaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JenaProjectApplication.class, args);
    }

    @Bean()
    public QueryService queryService() {
        return QueryService.buildOwlQueryService("univ.ttl"); // insert fileName here -- add file to resources folder
    }
}

// allowing any origin to access this REST API
@Configuration
@EnableWebMvc
class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
