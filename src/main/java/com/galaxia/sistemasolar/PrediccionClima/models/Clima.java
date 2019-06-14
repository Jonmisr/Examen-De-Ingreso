package com.galaxia.sistemasolar.PrediccionClima.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Clima {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long dia;
	
	private String clima;

	public Clima(String clima) {
		this.clima = clima;
	}
	
	public Long getDia() {
		return dia;
	}

	public void setDia(Long dia) {
		this.dia = dia;
	}

	public String getClima() {
		return clima;
	}

	public void setClima(String clima) {
		this.clima = clima;
	}	
}