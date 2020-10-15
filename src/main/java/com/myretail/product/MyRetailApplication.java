package com.myretail.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "classpath:${environment:local}.properties", ignoreResourceNotFound = true),
        @PropertySource("classpath:application.properties")
})
public class MyRetailApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyRetailApplication.class, args);
    }
}




