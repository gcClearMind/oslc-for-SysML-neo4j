package com.example.oslc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class Neo4jApplication {

	public static void main(String[] args)   {
		SpringApplication.run(Neo4jApplication.class, args);
	}

}
