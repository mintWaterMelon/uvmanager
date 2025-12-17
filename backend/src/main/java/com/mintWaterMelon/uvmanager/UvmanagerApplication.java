package com.mintWaterMelon.uvmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class UvmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UvmanagerApplication.class, args);
	}

}
