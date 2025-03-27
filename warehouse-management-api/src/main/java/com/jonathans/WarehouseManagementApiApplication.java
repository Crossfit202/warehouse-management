package com.jonathans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.jonathans.config") // Add this annotation
public class WarehouseManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseManagementApiApplication.class, args);
	}

}
