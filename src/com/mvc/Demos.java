package com.mvc;

import com.mvc.estrategiaImp.StrategyPeriodoLluvia;
import com.mvc.estrategiaImp.StrategyPeriodoOptimo;
import com.mvc.estrategiaImp.StrategyPeriodoSequia;

public class Demos {

	public static void main(String[] args) {
		
		int totalDiasAnio = 365;
		int anios = 10;
		//Instancio Sistema Solar
		SistemaSolar sol = SistemaSolar.getInstanceSistemaSolar();
		//Instancio Planetas
		Ferengi ferengi = new Ferengi();
		Betasoide beta = new Betasoide();
		Vulcano vulcano = new Vulcano();
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
			
			int dias = i;	
			sol.getPlanetas().stream().forEach(planeta -> planeta.setTiempoMovimiento());
			sol.getPlanetas().stream().forEach(planeta -> planeta.setMovimiento());			
			
			//Aplico Algoritmos Dentro De La Lista Que Tiene El Sol

			sol.getCondicionesStrategy().stream().forEach(estrategia -> sol.aplicarCondiciones(estrategia, dias));
	
			try {
				Thread.sleep(1000 / sol.getFPS());
			} catch (Exception e) {
			}
		}
		
		System.out.println("Cantidad Sucesos Sequias = " + sequia.getContadorSequias());
		//for(Integer unDia : sequia.getDiasOcurridosSequias()) { System.out.println(unDia);}
		System.out.println("Cantidad Sucesos Lluvias = " + lluvia.getContadorLluvias());
		System.out.println("Dia = " + lluvia.getMaximoDia() + " - Perimetro = " + lluvia.getMaximoPerimetro());
		System.out.println("Cantidad Sucesos Optimos = " + optimo.getContadorOptimo());
		//for(Integer unDia : optimo.getDiasOcurridosOptimos()) { System.out.println(unDia);}
	}
}