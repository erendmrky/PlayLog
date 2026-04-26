package com.buren.playlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class PlaylogApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaylogApplication.class, args);
	}

}
