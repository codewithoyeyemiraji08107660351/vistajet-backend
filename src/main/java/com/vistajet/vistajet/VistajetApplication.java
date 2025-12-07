package com.vistajet.vistajet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VistajetApplication {

	public static void main(String[] args) {

        SpringApplication.run(VistajetApplication.class, args);
	}

}
