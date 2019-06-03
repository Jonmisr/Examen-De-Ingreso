package com.mvc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaSolar {

	private int centroX;
	private int centroY;
	private static SistemaSolar instanciaSistema;
	private ArrayList<Planeta> planetas;
	private int contadorSequias;
	private int contadorLluvias;
	private int contadorOptimo;
	private double maximoPerimetro;
	private int maximoDia;
	private int FPS;
	private ArrayList<Integer> diasCumplidosSequias = new ArrayList<>();
	private ArrayList<Integer> diasCumplidosOptimos = new ArrayList<>();

	private SistemaSolar() {
		planetas = new ArrayList<>();
		//Los FPS Son Para El JFrame
		this.FPS = 60;
		this.contadorSequias = 0;
		this.contadorLluvias = 0;
		this.contadorOptimo = 0;
		this.centroX = 0;
		this.centroY = 0;
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
	
	public ArrayList<Integer> getDiasOcurridosSequias(){
		
		return diasCumplidosSequias;
	}
	
	public ArrayList<Integer> getDiasOcurridosOptimos(){
		
		return diasCumplidosOptimos;
	}
	
	public ArrayList<Planeta> getPlanetas() {
		return planetas;
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

	public int getCentroX() {
		return centroX;
	}

	public void setCentroX(int centroX) {
		this.centroX = centroX;
	}

	public int getCentroY() {
		return centroY;
	}

	public void setCentroY(int centroY) {
		this.centroY = centroY;
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
	
//Metodo Para Redondear Valores De Tipo Double Definiendo Su Cantidad De Numeros Despues Del 0.-------------------------------------------------------------------
	
	public double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		if (resultado < parteEntera) return Math.ceil(resultado);
		return resultado;
	}
		
//Algoritmo Para Calcular La Alineacion Total En El Sistema------------------------------------------------------	

		public void sucesoPeriodoDeSequia(int dia) {

			Planeta unPlaneta = this.planetas.get(2);
			
			double pendiente = calcularPendiente(unPlaneta);
			double ordenadaAlOrigen = calcularOrdenada(pendiente, unPlaneta);

			//Filtro La Lista De Planetas Para Que Al Comparar Con La Recta No Calcule El Del Planeta Que Utilice
			List<Planeta> aux = this.planetas.stream().filter(planeta -> planeta.getClass() != unPlaneta.getClass()).collect(Collectors.toList());

			//Recorro La Lista De Planetas Y Si Todos Pertenecen A La Recta Entonces Deberia Suceder La Sequia
			//Si Uno No Cumple La Condicion El Resto No Lo Comprueba
			if (aux.stream().allMatch(planeta -> compararPuntoConRecta(pendiente, ordenadaAlOrigen, planeta))) {
				getDiasOcurridosSequias().add(dia);
				this.aumentarContadorSequias();
			}
		}

	//Esta Funcion Lo Que Hace Es Calcular La Diferencia Entre 2 Puntos Y Obtener Su DeltaX y DeltaY
	//Al Dividirlos Me Debe Dar La Pendiente m De La Ecuacion -> y = m.x + b	
	//El getCentro() Es De La Posicion Del Sol En (0,0)	
		
		private double calcularPendiente(Planeta unPlaneta) {
			
			double deltaX = unPlaneta.getMovimientoEnX() - getCentroX();		
			double nuevoDeltaX = this.redondearDecimales(deltaX, 4);
			double deltaY = unPlaneta.getMovimientoEnY() - getCentroY();
			double nuevoDeltaY = this.redondearDecimales(deltaY, 4);
			double pendiente = nuevoDeltaY/nuevoDeltaX;
			return this.redondearDecimales(pendiente, 4);
		}

		//Una Vez Obtenido La Pendiente -> m Sacada Entre Un Planeta Y El Sol,
		//Calculo Cual Es La Ordenada Al Origen -> b
		//Resto La Posicion Y Del Planeta Con La Pendiente Calculada Por La Posicion X Del Planeta
		//Quedando La Ecuacion b = y - m.x
		
		private double calcularOrdenada(double pendiente, Planeta unPlaneta) {
			double ordenada = (unPlaneta.getMovimientoEnY() - (pendiente * unPlaneta.getMovimientoEnX()));
			double nuevaOrdenada = this.redondearDecimales(ordenada, 4);
			if (Math.abs(nuevaOrdenada) < 1e-5) return 0.0;
			return nuevaOrdenada;
		}
		
		//Esta Funcion Al Sacar La Pendiente Y La Ordenada Entre 1 Planeta Y El Sol En (0,0)
		//Le Paso Los Demas Planetas Sus Puntos Y Reemplazo En La Ecuacion y = m.x + b Los Valores
		//Y Si El Punto X == Y Quiere Decir Que El P(x,y) Pertenece A La Recta

		private boolean compararPuntoConRecta(double pendiente, double ordenadaAlOrigen, Planeta unPlaneta) {
			
			double calculoDeX = pendiente * unPlaneta.getMovimientoEnX() + ordenadaAlOrigen;
	
			double calculoX = this.redondearDecimales(calculoDeX, 4);	
			double calculoY = this.redondearDecimales(unPlaneta.getMovimientoEnY(), 4);
			
			double resultado;
			
		if ((calculoX > 0 && calculoY > 0) || (calculoX < 0 && calculoY < 0)) {
			double valorX = Math.abs(calculoX);
			double valorY = Math.abs(calculoY);

			if (valorX > valorY) {
				resultado = valorX - valorY;
			} else {
				resultado = valorY - valorX;			
			}
			double resultadoRedondeado = this.redondearDecimales(resultado, 4);
			if(unPlaneta.getClass() == Ferengi.class) {
			System.out.println("Resultado Ferengi = " + resultadoRedondeado);
			}
			else { System.out.println("Resultado Betasoide = " + resultadoRedondeado); }
			return (resultadoRedondeado > 0 && resultadoRedondeado < 10);
		}

		return calculoY == calculoX;
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

	//Calculo La Orientacion De ABP, BCP, CBP
	double orientacionTrianguloPunto1 = calculoOrientacion(planetaFerengi, planetaBetasoide);
	double orientacionTrianguloPunto2 = calculoOrientacion(planetaBetasoide, planetaVulcano);
	double orientacionTrianguloPunto3 = calculoOrientacion(planetaVulcano, planetaFerengi);
	
	//Los Agrego A La Lista
	valoresOrientados.add(orientacionTrianguloPunto1);
	valoresOrientados.add(orientacionTrianguloPunto2);
	valoresOrientados.add(orientacionTrianguloPunto3);
	
	//Recorro La Lista De Planetas Y Si Todos Tienen La Misma Orientacion Que ABC Sucede El De Lluvia
	//Si Uno No Cumple La Condicion El Resto No Lo Comprueba Y Significa Que El Punto Esta Fuera Del Triangulo
	
	if(valoresOrientados.stream().allMatch(valor -> tienenLaMismaOrientacion(orientacionTriangulo, valor))) {	
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
		
		double primerLado =  Point2D.distance(planetaA.getMovimientoEnX(), planetaA.getMovimientoEnY(), 
							 planetaB.getMovimientoEnX(), planetaB.getMovimientoEnY());
		double segundoLado = Point2D.distance(planetaA.getMovimientoEnX(), planetaA.getMovimientoEnY(), 
							 planetaC.getMovimientoEnX(), planetaC.getMovimientoEnY());
		double tercerLado =  Point2D.distance(planetaB.getMovimientoEnX(), planetaB.getMovimientoEnY(), 
				 			 planetaC.getMovimientoEnX(), planetaC.getMovimientoEnY());
		
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
		
	double orientacion = (planetaA.getMovimientoEnX() - planetaC.getMovimientoEnX()) *
						 (planetaB.getMovimientoEnY() - planetaC.getMovimientoEnY()) -
						 (planetaA.getMovimientoEnY() - planetaC.getMovimientoEnY()) *
						 (planetaB.getMovimientoEnX() - planetaC.getMovimientoEnX());
	
	return this.redondearDecimales(orientacion, 4);	
	}
	
	//Obtengo La Orientacion Que Forma El Punto P Con Los Vertices Del Triangulo ABC.
	//El Punto P Para Esta Situacion Es El Sol En (0,0)
	//Calculo Para Obtener Las Orientaciones De ABP, BCP, CBP
	
	private double calculoOrientacion(Planeta planetaA, Planeta planetaB) {
		
	double orientacion = (planetaA.getMovimientoEnX() - this.getCentroX()) *
						 (planetaB.getMovimientoEnY() - this.getCentroY()) -
						 (planetaA.getMovimientoEnY() - this.getCentroY()) *
						 (planetaB.getMovimientoEnX() - this.getCentroX());	
		
	return this.redondearDecimales(orientacion, 4);
	}

//Algoritmo Para Calcular La Alineacion Solo Entre Planetas------------------------------------------------------

	public void sucesoPeriodoOptimo(int dia) {
		
	Ferengi planetaFerengi = (Ferengi) planetas.get(0);
	Betasoide planetaBetasoide = (Betasoide) planetas.get(1);
	Vulcano planetaVulcano = (Vulcano) planetas.get(2);	
		
	double puntoX = calcularSegmentoEntreDosPlanetasEnX(planetaFerengi,  planetaVulcano);
	double puntoY = calcularSegmentoEntreDosPlanetasEnY(planetaFerengi,  planetaVulcano);
	
	boolean primeraCondicion = compararPuntosConLaRecta(planetaFerengi, planetaBetasoide, puntoX, puntoY);
	boolean segundaCondicion = compararPuntosConLaRecta(planetaFerengi, puntoX, puntoY);
	
	//Si El Tercer Planeta (Ya Sacado El Segmento Con Los Dos Anteriores), Cumple Con La Condicion
	//Y El Sol No, Quiere Decir Que El Planeta Esta Dentro De La Recta Y El Sol No
	//Entonces Deberia Suceder La Condicion Optima De Presion Y Temperatura
	
	if(primeraCondicion && segundaCondicion) { this.aumentarContadorOptimo();
											   getDiasOcurridosOptimos().add(dia);}
	}
	
	//Calculo El Segmento BA Entre Dos Planetas Para Obtener La Recta
	
	public double calcularSegmentoEntreDosPlanetasEnX(Planeta planetaA, Planeta planetaB) {
		
		double puntoX = planetaB.getMovimientoEnX() - planetaA.getMovimientoEnX();
		double puntoXRedondeado = this.redondearDecimales(puntoX, 4);
		
		return puntoXRedondeado;
	}
	
	public double calcularSegmentoEntreDosPlanetasEnY(Planeta planetaA, Planeta planetaB) {
		
		double puntoY = planetaB.getMovimientoEnY() - planetaA.getMovimientoEnY();
		double puntoYRedondeado = this.redondearDecimales(puntoY, 4);
		
		return puntoYRedondeado;
	}
	
	//Entre Los Planetas A y B Y El Segmento puntoXY Calculado Anteriormente,
	//Obtengo El Calculo De La Ecuacion De La Siguiente Forma
	//(B.x - A.x) / x == (B.y - A.y) / y
	
	public boolean compararPuntosConLaRecta(Planeta planetaA, Planeta planetaB, double puntoX, double puntoY) {
		
		double puntoEnX = (planetaB.getMovimientoEnX() - planetaA.getMovimientoEnX()) / puntoX;
		double puntoXRedondeado = this.redondearDecimales(puntoEnX, 4);
		double puntoEnY = (planetaB.getMovimientoEnY() - planetaA.getMovimientoEnY()) / puntoY;
		double puntoYRedondeado = this.redondearDecimales(puntoEnY, 4);		
		double resultado;
		
		if((puntoXRedondeado > 0) && (puntoYRedondeado > 0) || (puntoXRedondeado < 0) && (puntoYRedondeado > 0)) {
			double valorX = Math.abs(puntoXRedondeado);
			double valorY = Math.abs(puntoYRedondeado);
			
			if(valorX > valorY) {			
				resultado = valorX - valorY;			
			} else {
				resultado = valorY - valorX;
			}
			System.out.println("Planeta = " + resultado);
			return resultado >= 0 && resultado < 0.4;
		}
		return puntoXRedondeado == puntoYRedondeado;
	}
	
	//Entre El Planeta A, El Sol Y El Segmento puntoXY Calculado Anteriormente,
	//Obtengo El Calculo De La Ecuacion De La Siguiente Forma
	//(0 - A.x) / x == (0 - A.y) / y
	//Si Los Resultados Son Distintos Quiere Decir Que El Sol No Se Encuentra En La Recta
	
	public boolean compararPuntosConLaRecta(Planeta planetaA ,double puntoX ,double puntoY) {
		
		double puntoEnX = (this.getCentroX() - planetaA.getMovimientoEnX()) / puntoX;
		double puntoXRedondeado = this.redondearDecimales(puntoEnX, 4);
		double puntoEnY = (this.getCentroY() - planetaA.getMovimientoEnY()) / puntoY;
		double puntoYRedondeado = this.redondearDecimales(puntoEnY, 4);
		double resultado;
		
		if((puntoXRedondeado > 0) && (puntoYRedondeado > 0) || (puntoXRedondeado < 0) && (puntoYRedondeado > 0)) {
			double valorX = Math.abs(puntoXRedondeado);
			double valorY = Math.abs(puntoYRedondeado);
			
			if(valorX > valorY) {			
				resultado = valorX - valorY;			
			} else {
				resultado = valorY - valorX;
			}
			//System.out.println("Sol = " + resultado);
			return resultado < 0 && resultado > 0.002;
		}
		return puntoXRedondeado != puntoYRedondeado;
	}
	
	public void sucesoSequia(int dia) {
		
		Planeta unPlaneta = this.planetas.get(2);
		
		double valorX = unPlaneta.getMovimientoEnX() - this.getCentroX();
		double puntoX = this.redondearDecimales(valorX, 4);
		
		double valorY = unPlaneta.getMovimientoEnY() - this.getCentroY();
		double puntoY = this.redondearDecimales(valorY, 4);

		//Filtro La Lista De Planetas Para Que Al Comparar Con La Recta No Calcule El Del Planeta Que Utilice
		List<Planeta> aux = this.planetas.stream().filter(planeta -> planeta.getClass() != unPlaneta.getClass()).collect(Collectors.toList());

		//Recorro La Lista De Planetas Y Si Todos Pertenecen A La Recta Entonces Deberia Suceder La Sequia
		//Si Uno No Cumple La Condicion El Resto No Lo Comprueba
		if (aux.stream().allMatch(planeta -> compararPuntosConLaRecta(unPlaneta, planeta, puntoX, puntoY))) {
			getDiasOcurridosSequias().add(dia);
			this.aumentarContadorSequias();
		}
	}

}