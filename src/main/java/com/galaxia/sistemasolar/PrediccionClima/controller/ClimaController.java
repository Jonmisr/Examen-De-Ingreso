package com.galaxia.sistemasolar.PrediccionClima.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.galaxia.sistemasolar.PrediccionClima.models.Clima;
import com.galaxia.sistemasolar.PrediccionClima.repository.ClimaRepository;

@RestController
@RequestMapping("predicciones/clima")
public class ClimaController {

	@Autowired
	private ClimaRepository CR;

	@GetMapping // http://localhost:9090/predicciones/clima
	public List<Clima> lista() {

		return CR.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void create(String condicionClimatica) {
				
		Clima unClima = new Clima(condicionClimatica);
		CR.save(unClima);
	}
	
	@GetMapping("?dia={dia}")
	@ResponseStatus(HttpStatus.OK)
	public Clima get(@PathVariable ("dia") long dia) {	
		
		if(!(CR.existsById(dia))) { System.out.println("No Se Encontro El Id: " + dia);
								   return null;}
		
		return CR.getOne(dia);		 
}
	
	@DeleteMapping("/borrarDias")
	@ResponseStatus(HttpStatus.OK)
	public String deleteAll() {
		
		CR.deleteAll();
		return "Todos Los Resultados Fueron Eliminados De La DB"; 
}
}
