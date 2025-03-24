package com.catalisa.taxes_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class TaxesApiApplication {

	public static void main(String[] args) {
		Dotenv.load();
		SpringApplication.run(TaxesApiApplication.class, args);
	}

}
