package com.mvc;

public class Main {

	public static void main(String[] args) {

		double tiempoPrincipal = System.currentTimeMillis();
		double angulo = Math.toDegrees(0);
		
		for (int i = 0; i < 10; i++) {

			double tiempoActual = (System.currentTimeMillis() - tiempoPrincipal) / 1000f;

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Tiempo = " + tiempoActual);
			angulo = 2 * Math.PI * tiempoActual;
			double nuevo = redondearDecimales(angulo, 4);
			System.out.println(nuevo);
		}
	}

	public static double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		if (resultado < parteEntera) return Math.ceil(resultado);
		return resultado;
	}
	
}
