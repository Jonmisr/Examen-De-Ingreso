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
		//Los FPS Son Para El JFrame
		this.FPS = 30;
		this.contadorSequias = 0;
		this.contadorLluvias = 0;
		this.contadorOptimo = 0;
		//Maximo Dia En Base Al Perimetro Maximo
		this.maximoDia = 0;
		//Maximo Perimetro Para Guardar 
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
		//La Posicion Del Sol Para Los Calculos En Los Algoritmos
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
	
//Metodo Para Tener Una Vision De Los Puntos Definidos En El JFrame
	
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
	
//Metodo Para Redondear Valores De Tipo Double Definiendo Su Cantidad De Numeros Despues Del 0.-------------------------------------------------------------------
	
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

			//Filtro La Lista De Planetas Para Que Al Comparar Con La Recta No Calcule El Del Planeta Que Utilice
			List<Planeta> aux = this.planetas.stream().filter(planeta -> planeta.getClass() != unPlaneta.getClass()).collect(Collectors.toList());

			//Recorro La Lista De Planetas Y Si Todos Pertenecen A La Recta Entonces Deberia Suceder La Sequia
			//Si Uno No Cumple La Condicion El Resto No Lo Comprueba
			if (aux.stream().allMatch(planeta -> compararPuntoConRecta(pendiente, ordenadaAlOrigen, planeta))) {

				this.aumentarContadorSequias();
			}
		}

	//Esta Funcion Lo Que Hace Es Calcular La Diferencia Entre 2 Puntos Y Obtener Su DeltaX y DeltaY
	//Al Dividirlos Me Debe Dar La Pendiente m De La Ecuacion -> y = m.x + b	
	//El getCentro() Es De La Posicion Del Sol En (0,0)	
		
		private double calcularPendiente(Planeta unPlaneta) {
			
			double deltaX = (unPlaneta.trayectoria().getX() - getCentro().getX());			
			double deltaY = (unPlaneta.trayectoria().getY() - getCentro().getY());

			double pendiente = deltaY / deltaX;
			//Lo Redondeo Hasta 4 Decimales
			double pendienteRound = this.redondearDecimales(pendiente, 4);
			return pendienteRound;
		}

		//Una Vez Obtenido La Pendiente -> m Sacada Entre Un Planeta Y El Sol,
		//Calculo Cual Es La Ordenada Al Origen -> b
		//Resto La Posicion Y Del Planeta Con La Pendiente Calculada Por La Posicion X Del Planeta
		//Quedando La Ecuacion b = y - m.x
		
		private double calcularOrdenada(double pendiente, Planeta unPlaneta) {
			double ordenada = (unPlaneta.trayectoria().getY() - (pendiente * unPlaneta.trayectoria().getX()));
			return this.redondearDecimales(ordenada, 4);
		}
		
		//Esta Funcion Al Sacar La Pendiente Y La Ordenada Entre 1 Planeta Y El Sol En (0,0)
		//Le Paso Los Demas Planetas Sus Puntos Y Reemplazo En La Ecuacion y = m.x + b Los Valores
		//Y Si El Punto X == Y Quiere Decir Que El P(x,y) Pertenece A La Recta

		private boolean compararPuntoConRecta(double pendiente, double ordenadaAlOrigen, Planeta unPlaneta) {
			
			double calculoDeX = (pendiente * unPlaneta.trayectoria().getX()) + ordenadaAlOrigen;
			double calculoX = this.redondearDecimales(calculoDeX, 4);
			
			double calculoDeY = unPlaneta.trayectoria().getY();
			double calculoY = this.redondearDecimales(calculoDeY, 4);
			//Este Printf Es Para Ver Todas Las Iteraciones Del Resultado Para Saber Que Pasa
			System.out.println("Y = " + calculoY + " ; " + "X = " + calculoX);
			return (calculoY == calculoX);

		}	
	
//Algoritmo Para Calcular La Triangulacion Teniendo Dentro El Sol------------------------------------------------------
	public void sucesoPeriodoDeLluvia(int diaSuceso) {

	//Esta Lista Es Para Los Valores De Segmento Entre Planetas Redondeado Ya Su Lado
	ArrayList<Double> valoresOrientados = new ArrayList<>();	
		
	Ferengi planetaFerengi = (Ferengi) planetas.get(0);
	Betasoide planetaBetasoide = (Betasoide) planetas.get(1);
	Vulcano planetaVulcano = (Vulcano) planetas.get(2);
	
	//Calculo La Orientacion De ABC
	double orientacionTriangulo = calculoOrientacion(planetaFerengi, planetaBetasoide, planetaVulcano);
	double valorRedondeado = this.redondearDecimales(orientacionTriangulo, 4);

	//Calculo La Orientacion De ABP, BCP, CBP
	double orientacionTrianguloPunto1 = calculoOrientacion(planetaFerengi, planetaBetasoide);
	double valorRedondeado1 = this.redondearDecimales(orientacionTrianguloPunto1, 4);
	double orientacionTrianguloPunto2 = calculoOrientacion(planetaBetasoide, planetaVulcano);
	double valorRedondeado2 = this.redondearDecimales(orientacionTrianguloPunto2, 4);
	double orientacionTrianguloPunto3 = calculoOrientacion(planetaVulcano, planetaFerengi);
	double valorRedondeado3 = this.redondearDecimales(orientacionTrianguloPunto3, 4);
	
	//Los Agrego A La Lista
	valoresOrientados.add(valorRedondeado1);
	valoresOrientados.add(valorRedondeado2);
	valoresOrientados.add(valorRedondeado3);
	
	//Recorro La Lista De Planetas Y Si Todos Tienen La Misma Orientacion Que ABC Sucede El De Lluvia
	//Si Uno No Cumple La Condicion El Resto No Lo Comprueba Y Significa Que El Punto Esta Fuera Del Triangulo
	
	if(valoresOrientados.stream().allMatch(valor -> tienenLaMismaOrientacion(valorRedondeado, valor))) {	
		this.aumentarContadorLluvias();		
		double perimetroTriangulo = calcularPerimetroDelTriangulo(planetaFerengi, planetaBetasoide, planetaVulcano);
		double perimetro = this.redondearDecimales(perimetroTriangulo, 4);
		if(perimetro > this.getMaximoPerimetro()) { this.setMaximoPerimetro(perimetro); 
													this.setMaximoDia(diaSuceso);}
	}
}
	//Obtengo La Distancia Entre Los Puntos AB, AC, BC Siendo Este Cada Lado Del Triangulo
	//Sumo Sus Lados Para Obtener El Perimetro
	
	private double calcularPerimetroDelTriangulo(Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		
		double primerLado =  Point2D.distance(planetaA.trayectoria().getX(), planetaA.trayectoria().getY(), 
							 planetaB.trayectoria().getX(), planetaB.trayectoria().getY());
		double segundoLado = Point2D.distance(planetaA.trayectoria().getX(), planetaA.trayectoria().getY(), 
							 planetaC.trayectoria().getX(), planetaC.trayectoria().getY());
		double tercerLado =  Point2D.distance(planetaB.trayectoria().getX(), planetaB.trayectoria().getY(), 
				 			 planetaC.trayectoria().getX(), planetaC.trayectoria().getY());
		
		return primerLado + segundoLado + tercerLado;		
	}
	
	//Funcion Para Comprobar Si El Punto Respecto Al Triangulo Tienen La Misma Orientacion
	private boolean tienenLaMismaOrientacion(double primeraOrientacion, double segundaOrientacion) {	
		return ((primeraOrientacion > 0) && (segundaOrientacion > 0)) ||
			   ((primeraOrientacion < 0) && (segundaOrientacion < 0));
	}
	
	//Obtengo La Orientacion Del Triangulo ABC
	//Con La Siguiente Formula (A1.x - A3.x) * (A2.y - A3.y) - (A1.y - A3.y) * (A2.x - A3.x)
	
	private double calculoOrientacion(Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		
	return (planetaA.trayectoria().getX() - planetaC.trayectoria().getX()) *
		   (planetaB.trayectoria().getY() - planetaC.trayectoria().getY()) -
		   (planetaA.trayectoria().getY() - planetaC.trayectoria().getY()) *
		   (planetaB.trayectoria().getX() - planetaC.trayectoria().getX());
		
	}
	
	//Obtengo La Orientacion Que Forma El Punto P Con Los Vertices Del Triangulo ABC.
	//El Punto P Para Esta Situacion Es El Sol En (0,0)
	//Calculo Para Obtener Las Orientaciones De ABP, BCP, CBP
	
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
	
	//Si El Tercer Planeta (Ya Sacado El Segmento Con Los Dos Anteriores), Cumple Con La Condicion
	//Y El Sol No, Quiere Decir Que El Planeta Esta Dentro De La Recta Y El Sol No
	//Entonces Deberia Suceder La Condicion Optima De Presion Y Temperatura
	
	if(primeraCondicion && segundaCondicion) { this.aumentarContadorOptimo(); }
	}
	
	//Calculo El Segmento BA Entre Dos Planetas Para Obtener La Recta
	
	public Point calcularSegmentoEntreDosPlanetas(Planeta planetaA, Planeta planetaB) {
		
		double puntoX = planetaB.trayectoria().getX() - planetaA.trayectoria().getX();
		double puntoXRedondeado = this.redondearDecimales(puntoX, 4);
		double puntoY = planetaB.trayectoria().getY() - planetaA.trayectoria().getY();
		double puntoYRedondeado = this.redondearDecimales(puntoY, 4);
		
		return new Point((int) puntoXRedondeado, (int) puntoYRedondeado);
	}
	
	//Entre Los Planetas A y B Y El Segmento puntoXY Calculado Anteriormente,
	//Obtengo El Calculo De La Ecuacion De La Siguiente Forma
	//(B.x - A.x) / XY.x == (B.y - A.y) / XY.y
	
	public boolean compararPuntosConLaRecta(Planeta planetaA, Planeta planetaB, Point puntoXY) {
		
		double puntoEnX = (planetaB.trayectoria().getX() - planetaA.trayectoria().getX()) / puntoXY.getX();
		double puntoXRedondeado = this.redondearDecimales(puntoEnX, 4);
		double puntoEnY = (planetaB.trayectoria().getY() - planetaA.trayectoria().getY()) / puntoXY.getY();
		double puntoYRedondeado = this.redondearDecimales(puntoEnY, 4);
		
		System.out.println("Punto En X = " + puntoXRedondeado);
		System.out.println("Punto En Y = " + puntoYRedondeado);
		
		return puntoXRedondeado == puntoYRedondeado;
	}
	
	//Entre El Planeta A, El Sol Y El Segmento puntoXY Calculado Anteriormente,
	//Obtengo El Calculo De La Ecuacion De La Siguiente Forma
	//(0 - A.x) / XY.x == (0 - A.y) / XY.y
	//Si Los Resultados Son Distintos Quiere Decir Que El Sol No Se Encuentra En La Recta
	
	public boolean compararPuntosConLaRecta(Planeta planetaA, Point puntoXY) {
		
		double puntoEnX = (this.getCentro().getX() - planetaA.trayectoria().getX()) / puntoXY.getX();
		double puntoXRedondeado = this.redondearDecimales(puntoEnX, 4);
		double puntoEnY = (this.getCentro().getY() - planetaA.trayectoria().getY()) / puntoXY.getY();
		double puntoYRedondeado = this.redondearDecimales(puntoEnY, 4);
		
		return puntoXRedondeado != puntoYRedondeado;
	}
}