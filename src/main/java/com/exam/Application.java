package com.exam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Slf4j
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		log.info("WebMvcConfigurer.addCorsMappings");
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("*")
						.allowedOrigins("http://localhost:3000","*");
			}
		};
	}

}
