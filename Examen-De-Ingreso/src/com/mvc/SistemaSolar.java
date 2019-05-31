package com.mvc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaSolar {

	private static Point centro;
	private static Point centroDibujo;
	private static SistemaSolar instanciaSistema;
	private ArrayList<Planeta> planetas;
	private int contadorSequias;
	private int contadorLluvias;
	private int contadorOptimo;
	private double maximoPerimetro;
	private int maximoDia;
	private int FPS;

	private SistemaSolar() {
		planetas = new ArrayList<>();
		this.FPS = 30;
		this.contadorSequias = 0;
		this.contadorLluvias = 0;
		this.contadorOptimo = 0;
		this.maximoDia = 0;
		this.maximoPerimetro = 0;
	}

	// Patron Singleton
	public static SistemaSolar getInstanceSistemaSolar() {

		if (instanciaSistema == null) {

			instanciaSistema = new SistemaSolar();
		}

		return instanciaSistema;
	}

	public void agregarPlaneta(Planeta unPlaneta) {
		unPlaneta.setSol(this);
		planetas.add(unPlaneta);
	}

	public void aumentarContadorSequias() {
		this.contadorSequias++;
	}
	
	public void aumentarContadorLluvias() {
		this.contadorLluvias++;
	}
	
	public void aumentarContadorOptimo() {
		this.contadorOptimo++;
	}

	public double getMaximoPerimetro() {
		return maximoPerimetro;
	}

	public int getMaximoDia() {
		return maximoDia;
	}

	public void setMaximoDia(int maximoDia) {
		this.maximoDia = maximoDia;
	}

	public void setMaximoPerimetro(double maximoPerimetro) {
		this.maximoPerimetro = maximoPerimetro;
	}

	public void setPosicionSistemaSolar(Graphics g) {

		g.setColor(Color.RED);
		PintarPunto(this.getCentroDibujo(), 15, g);
	}

	public void PintarPunto(Point c, int r, Graphics g) {
		g.fillOval(c.x - r / 2, c.y - r / 2, r, r);
	}
	
	public Point getCentroDibujo() {
		return centroDibujo;
	}

	public void setCentroDibujo(Point centroDibujo) {
		SistemaSolar.centroDibujo = centroDibujo;
		SistemaSolar.centro = new Point(0,0);
	}

	public Point getCentro() {
		return centro;
	}

	public int getFPS() {
		return FPS;
	}
	
	public int getContadorSequias() {
		return contadorSequias;
	}
	
	public int getContadorLluvias() {
		return contadorLluvias;
	}
	
	public int getContadorOptimo() {
		return contadorOptimo;
	}

	public void setCentro(Point centro) {
		SistemaSolar.centro = centro;
	}
	
//Metodo Para Tener Una Vision De Los Puntos Definidos
	
	public void dibujarLineasEntrePuntos(Graphics g) {
		
		g.setColor(Color.CYAN);
		g.drawLine((int) (this.getCentroDibujo().getX()), (int) (this.getCentroDibujo().getY()), (int) (planetas.get(0).getMovimiento().getX()),
				(int) (planetas.get(0).getMovimiento().getY()));

		g.drawLine((int) (this.getCentroDibujo().getX()), (int) (this.getCentroDibujo().getY()), (int) (planetas.get(1).getMovimiento().getX()),
				(int) (planetas.get(1).getMovimiento().getY()));

		g.drawLine((int) (this.getCentroDibujo().getX()), (int) (this.getCentroDibujo().getY()), (int) (planetas.get(2).getMovimiento().getX()),
				(int) (planetas.get(2).getMovimiento().getY()));

		g.drawLine((int) (planetas.get(0).getMovimiento().getX()), (int) (planetas.get(0).getMovimiento().getY()),
				(int) (planetas.get(1).getMovimiento().getX()), (int) (planetas.get(1).getMovimiento().getY()));

		g.drawLine((int) (planetas.get(0).getMovimiento().getX()), (int) (planetas.get(0).getMovimiento().getY()),
				(int) (planetas.get(2).getMovimiento().getX()), (int) (planetas.get(2).getMovimiento().getY()));

		g.drawLine((int) (planetas.get(1).getMovimiento().getX()), (int) (planetas.get(1).getMovimiento().getY()),
				(int) (planetas.get(2).getMovimiento().getX()), (int) (planetas.get(2).getMovimiento().getY()));
	}
	
//Metodo Para Redondear Valores De Tipo Double-------------------------------------------------------------------
	
	public double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		return resultado;
	}
		
//Algoritmo Para Calcular La Alineacion Total En El Sistema------------------------------------------------------	

		public void sucesoPeriodoDeSequia() {

			Planeta unPlaneta = this.planetas.get(0);
			
			double pendiente = calcularPendiente(unPlaneta);
			System.out.println("Pendiente = " + pendiente);
			double ordenadaAlOrigen = calcularOrdenada(pendiente, unPlaneta);
			System.out.println("Ordenada Al Origen = " + ordenadaAlOrigen);

			List<Planeta> aux = this.planetas.stream().filter(planeta -> planeta.getClass() != unPlaneta.getClass()).collect(Collectors.toList());

			if (aux.stream().allMatch(planeta -> compararPuntoConRecta(pendiente, ordenadaAlOrigen, planeta))) {

				this.aumentarContadorSequias();
			}
		}

		private double calcularPendiente(Planeta unPlaneta) {
			double deltaX = (unPlaneta.trayectoria().getX() - getCentro().getX());
			
			double deltaY = (unPlaneta.trayectoria().getY() - getCentro().getY());

			double pendiente = deltaY / deltaX;
			double pendienteRound = this.redondearDecimales(pendiente, 4);
			return pendienteRound;
		}

		private double calcularOrdenada(double pendiente, Planeta unPlaneta) {
			double ordenada = (unPlaneta.trayectoria().getY() - (pendiente * unPlaneta.trayectoria().getX()));
			return this.redondearDecimales(ordenada, 4);
		}

		private boolean compararPuntoConRecta(double pendiente, double ordenadaAlOrigen, Planeta unPlaneta) {
			
			double calculoDeX = (pendiente * unPlaneta.trayectoria().getX()) + ordenadaAlOrigen;
			double calculoX = this.redondearDecimales(calculoDeX, 4);
			
			double calculoDeY = unPlaneta.trayectoria().getY();
			double calculoY = this.redondearDecimales(calculoDeY, 4);
			System.out.println("Y = " + calculoY + " ; " + "X = " + calculoX);
			return (calculoY == calculoX);

		}	
	
//Algoritmo Para Calcular La Triangulacion Teniendo Dentro El Sol------------------------------------------------------
	public void sucesoPeriodoDeLluvia(int diaSuceso) {

	ArrayList<Double> valoresOrientados = new ArrayList<>();	
		
	Ferengi planetaFerengi = (Ferengi) planetas.get(0);
	Betasoide planetaBetasoide = (Betasoide) planetas.get(1);
	Vulcano planetaVulcano = (Vulcano) planetas.get(2);
	
	double orientacionTriangulo = calculoOrientacion(planetaFerengi, planetaBetasoide, planetaVulcano);
	double valorRedondeado = this.redondearDecimales(orientacionTriangulo, 4);

	double orientacionTrianguloPunto1 = calculoOrientacion(planetaFerengi, planetaBetasoide);
	double valorRedondeado1 = this.redondearDecimales(orientacionTrianguloPunto1, 4);
	double orientacionTrianguloPunto2 = calculoOrientacion(planetaBetasoide, planetaVulcano);
	double valorRedondeado2 = this.redondearDecimales(orientacionTrianguloPunto2, 4);
	double orientacionTrianguloPunto3 = calculoOrientacion(planetaVulcano, planetaFerengi);
	double valorRedondeado3 = this.redondearDecimales(orientacionTrianguloPunto3, 4);
	
	valoresOrientados.add(valorRedondeado1);
	valoresOrientados.add(valorRedondeado2);
	valoresOrientados.add(valorRedondeado3);
	
	if(valoresOrientados.stream().allMatch(valor -> tienenLaMismaOrientacion(valorRedondeado, valor))) {	
		this.aumentarContadorLluvias();		
		double perimetroTriangulo = calcularPerimetroDelTriangulo(planetaFerengi, planetaBetasoide, planetaVulcano);
		double perimetro = this.redondearDecimales(perimetroTriangulo, 4);
		if(perimetro > this.getMaximoPerimetro()) { this.setMaximoPerimetro(perimetro); 
													this.setMaximoDia(diaSuceso);}
	}
}
	
	private double calcularPerimetroDelTriangulo(Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		
		double primerLado =  Point2D.distance(planetaA.trayectoria().getX(), planetaA.trayectoria().getY(), 
							 planetaB.trayectoria().getX(), planetaB.trayectoria().getY());
		double segundoLado = Point2D.distance(planetaA.trayectoria().getX(), planetaA.trayectoria().getY(), 
							 planetaC.trayectoria().getX(), planetaC.trayectoria().getY());
		double tercerLado =  Point2D.distance(planetaB.trayectoria().getX(), planetaB.trayectoria().getY(), 
				 			 planetaC.trayectoria().getX(), planetaC.trayectoria().getY());
		
		return primerLado + segundoLado + tercerLado;		
	}
	
	private boolean tienenLaMismaOrientacion(double primeraOrientacion, double segundaOrientacion) {	
		return ((primeraOrientacion > 0) && (segundaOrientacion > 0)) ||
			   ((primeraOrientacion < 0) && (segundaOrientacion < 0));
	}
	
	private double calculoOrientacion(Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		
	return (planetaA.trayectoria().getX() - planetaC.trayectoria().getX()) *
		   (planetaB.trayectoria().getY() - planetaC.trayectoria().getY()) -
		   (planetaA.trayectoria().getY() - planetaC.trayectoria().getY()) *
		   (planetaB.trayectoria().getX() - planetaC.trayectoria().getX());
		
	}
	
	private double calculoOrientacion(Planeta planetaA, Planeta planetaB) {
		
	return (planetaA.trayectoria().getX() - this.getCentro().getX()) *
		   (planetaB.trayectoria().getY() - this.getCentro().getY()) -
		   (planetaA.trayectoria().getY() - this.getCentro().getY()) *
		   (planetaB.trayectoria().getX() - this.getCentro().getX());	
		
	}

//Algoritmo Para Calcular La Alineacion Solo Entre Planetas------------------------------------------------------

	public void sucesoPeriodoOptimo() {
		
	Ferengi planetaFerengi = (Ferengi) planetas.get(0);
	Betasoide planetaBetasoide = (Betasoide) planetas.get(1);
	Vulcano planetaVulcano = (Vulcano) planetas.get(2);	
		
	Point puntoXY = calcularSegmentoEntreDosPlanetas(planetaFerengi,  planetaBetasoide);
	
	boolean primeraCondicion = compararPuntosConLaRecta(planetaFerengi, planetaVulcano, puntoXY);
	boolean segundaCondicion = compararPuntosConLaRecta(planetaFerengi, puntoXY);
	
	if(primeraCondicion && segundaCondicion) { this.aumentarContadorOptimo(); }
	}
	
	public Point calcularSegmentoEntreDosPlanetas(Planeta planetaA, Planeta planetaB) {
		
		double puntoX = planetaB.trayectoria().getX() - planetaA.trayectoria().getX();
		double puntoXRedondeado = this.redondearDecimales(puntoX, 4);
		double puntoY = planetaB.trayectoria().getY() - planetaA.trayectoria().getY();
		double puntoYRedondeado = this.redondearDecimales(puntoY, 4);
		
		return new Point((int) puntoXRedondeado, (int) puntoYRedondeado);
	}
	
	public boolean compararPuntosConLaRecta(Planeta planetaA, Planeta planetaB, Point puntoXY) {
		
		double puntoEnX = (planetaB.trayectoria().getX() - planetaA.trayectoria().getX()) / puntoXY.getX();
		double puntoXRedondeado = this.redondearDecimales(puntoEnX, 4);
		double puntoEnY = (planetaB.trayectoria().getY() - planetaA.trayectoria().getY()) / puntoXY.getY();
		double puntoYRedondeado = this.redondearDecimales(puntoEnY, 4);
		
		System.out.println("Punto En X = " + puntoXRedondeado);
		System.out.println("Punto En Y = " + puntoYRedondeado);
		
		return puntoXRedondeado == puntoYRedondeado;
	}
	
	public boolean compararPuntosConLaRecta(Planeta planetaA, Point puntoXY) {
		
		double puntoEnX = (this.getCentro().getX() - planetaA.trayectoria().getX()) / puntoXY.getX();
		double puntoXRedondeado = this.redondearDecimales(puntoEnX, 4);
		double puntoEnY = (this.getCentro().getY() - planetaA.trayectoria().getY()) / puntoXY.getY();
		double puntoYRedondeado = this.redondearDecimales(puntoEnY, 4);
		
		return puntoXRedondeado != puntoYRedondeado;
	}
}