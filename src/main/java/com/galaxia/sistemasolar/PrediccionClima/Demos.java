package com.galaxia.sistemasolar.PrediccionClima;

import java.util.Optional;

import com.galaxia.sistemasolar.PrediccionClima.controller.ClimaController;
import com.galaxia.sistemasolar.PrediccionClima.estrategiaImp.ICondicion;
import com.galaxia.sistemasolar.PrediccionClima.estrategiaImp.StrategyPeriodoLluvia;
import com.galaxia.sistemasolar.PrediccionClima.estrategiaImp.StrategyPeriodoOptimo;
import com.galaxia.sistemasolar.PrediccionClima.estrategiaImp.StrategyPeriodoSequia;

public class Demos {

	public void prediccionProgramaInformatico() {
		
		int totalDiasAnio = 365;
		int anios = 10;
		//Instancio Sistema Solar
		SistemaSolar sol = SistemaSolar.getInstanceSistemaSolar();
		//Instancio Planetas
		Planeta ferengi = new Planeta("Ferengi" ,500 ,1);
		Planeta beta = new Planeta("Betasoide" ,1000 ,3);
		Planeta vulcano = new Planeta("Vulcano" ,2000 ,-5);
		//Instancio Estrategias
		StrategyPeriodoSequia sequia = new StrategyPeriodoSequia(sol);
		StrategyPeriodoLluvia lluvia = new StrategyPeriodoLluvia(sol);
		StrategyPeriodoOptimo optimo = new StrategyPeriodoOptimo(sol);
		//Agrego Los Planetas Al Sol
		sol.agregarPlaneta(ferengi);
		sol.agregarPlaneta(beta);
		sol.agregarPlaneta(vulcano);
		//Agrego Las Estrategias Al Sol
		sol.agregarStrategy(sequia);
		sol.agregarStrategy(lluvia);
		sol.agregarStrategy(optimo);
		
		//Recorro Los Dias En Base A La Cantidad De Anios Que Quiero Que Pasen
		for(int i = 0; i < anios * totalDiasAnio; i++) {
			
			long dias = i;	
			sol.getPlanetas().stream().forEach(planeta -> planeta.setTiempoMovimiento());
			sol.getPlanetas().stream().forEach(planeta -> planeta.setMovimiento());			
			
			//Aplico Algoritmos Dentro De La Lista Que Tiene El Sol
			Optional<ICondicion> condicionCumplida = sol.getCondicionesStrategy()
					.stream()
					.filter(estrategia -> sol.aplicarCondiciones(estrategia, dias))
					.findAny();
											
			this.climaEstablecido(condicionCumplida);
		}
		
		System.out.println("Cantidad Sucesos Sequias = " + sequia.getContadorSequias());
		for(Long unDia : sequia.getDiasOcurridosSequias()) { System.out.println(unDia);}
		System.out.println("Cantidad Sucesos Lluvias = " + lluvia.getContadorLluvias());
		System.out.println("Dia = " + lluvia.getMaximoDia() + " - Perimetro = " + lluvia.getMaximoPerimetro());
		System.out.println("Cantidad Sucesos Optimos = " + optimo.getContadorOptimo());
		for(Long unDia : optimo.getDiasOcurridosOptimos()) { System.out.println(unDia);}
	}
	
	public void climaEstablecido(Optional<ICondicion> condicionCumplida) {

		ClimaController controladorCondicion = new ClimaController();

		if (condicionCumplida.isPresent()) {

			if (condicionCumplida.get().getClass().equals(StrategyPeriodoLluvia.class)) {
				controladorCondicion.create("lluvia");
			}

			else if (condicionCumplida.get().getClass().equals(StrategyPeriodoSequia.class)) {
				controladorCondicion.create("sequia");
			}

			else if (condicionCumplida.get().getClass().equals(StrategyPeriodoOptimo.class)) {
				controladorCondicion.create("optimo");
			}
		} else {
			controladorCondicion.create(" ");
		}
	}
}