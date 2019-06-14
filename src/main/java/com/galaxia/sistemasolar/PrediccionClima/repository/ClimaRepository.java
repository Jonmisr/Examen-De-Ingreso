package com.galaxia.sistemasolar.PrediccionClima.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.galaxia.sistemasolar.PrediccionClima.models.Clima;

public interface ClimaRepository extends JpaRepository<Clima, Long>{

}
