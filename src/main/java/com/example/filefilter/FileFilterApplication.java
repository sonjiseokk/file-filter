package com.example.filefilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FileFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileFilterApplication.class, args);
	}

}
