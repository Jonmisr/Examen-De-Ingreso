package com.galaxia.sistemasolar.PrediccionClima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrediccionClimaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrediccionClimaApplication.class, args);
		
		Demos demostracion = new Demos();
		demostracion.prediccionProgramaInformatico();
	}

}
