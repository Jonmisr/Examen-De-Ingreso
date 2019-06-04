package com.mvc;

import java.util.Scanner;

public class Demos {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		
		SistemaSolar sol = SistemaSolar.getInstanceSistemaSolar();
		Ferengi ferengi = new Ferengi();
		Betasoide beta = new Betasoide();
		Vulcano vulcano = new Vulcano();
		
		sol.agregarPlaneta(ferengi);
		sol.agregarPlaneta(beta);
		sol.agregarPlaneta(vulcano);

		//Al Iniciar El Programa, Hay Varias Veces Que El JFrame No Cargar Bien Y Tira Un NullPointerException
		//El Scanner Es Para Que Se Cargue La Imagen Asi Corre Sin Problemas
		System.out.println("Ingrese La Cantidad De Anios A Predecir:");
		int anios = scanner.nextInt();
		
		//Recorro Los Dias En Base A La Cantidad De Anios Que Quiero Que Pasen
		for (int dias = 0; dias < (365 * anios); dias++) {

			sol.getPlanetas().stream().forEach(planeta -> planeta.setTiempoMovimiento());
			sol.getPlanetas().stream().forEach(planeta -> planeta.setMovimiento());			
			
			//Algoritmos Condiciones
			sol.sucesoPeriodoDeSequia(dias);
			sol.sucesoPeriodoDeLluvia(dias);
			sol.sucesoPeriodoOptimo(dias);
			
			try {
				Thread.sleep(1000 / sol.getFPS());
			} catch (Exception e) {
			}
		}
		
		scanner.close();
		System.out.println("Cantidad Sucesos Sequias = " + sol.getContadorSequias());
		for(Integer unDia : sol.getDiasOcurridosSequias()) { System.out.println(unDia);}
		System.out.println("Cantidad Sucesos Lluvias = " + sol.getContadorLluvias());
		System.out.println("Dia = " + sol.getMaximoDia() + " - Perimetro = " + sol.getMaximoPerimetro());
		System.out.println("Cantidad Sucesos Optimos = " + sol.getContadorOptimo());
		for(Integer unDia : sol.getDiasOcurridosOptimos()) { System.out.println(unDia);}
	}
}