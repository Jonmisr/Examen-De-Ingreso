package com.mvc;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaSolar {

	private static RoundingMode RM = RoundingMode.HALF_EVEN;
	private static BigDecimal valorCero;
	private static BigDecimal limiteSuperiorSequia;
	private static BigDecimal limiteSuperiorOptimo;	
	private int centroX;
	private int centroY;
	private static SistemaSolar instanciaSistema;
	private ArrayList<Planeta> planetas;
	private int contadorSequias;
	private int contadorLluvias;
	private int contadorOptimo;
	private BigDecimal maximoPerimetro;
	private int maximoDia;
	private int FPS;
	private ArrayList<Integer> diasCumplidosSequias = new ArrayList<>();
	private ArrayList<Integer> diasCumplidosOptimos = new ArrayList<>();

	private SistemaSolar() {
		planetas = new ArrayList<>();
		// Los FPS Son Para El JFrame
		this.FPS = 30;
		this.contadorSequias = 0;
		this.contadorLluvias = 0;
		this.contadorOptimo = 0;
		this.centroX = 0;
		this.centroY = 0;
		// Maximo Dia En Base Al Perimetro Maximo
		this.maximoDia = 0;
		// Maximo Perimetro Para Guardar
		this.maximoPerimetro = new BigDecimal(0);
		SistemaSolar.valorCero = new BigDecimal(0);
		SistemaSolar.limiteSuperiorSequia = new BigDecimal(10);
		SistemaSolar.limiteSuperiorOptimo = new BigDecimal(0.02);//0.0210	
	}

	// Patron Singleton
	public static SistemaSolar getInstanceSistemaSolar() {

		if (instanciaSistema == null) {

			instanciaSistema = new SistemaSolar();
		}

		return instanciaSistema;
	}

	public ArrayList<Integer> getDiasOcurridosSequias() {

		return diasCumplidosSequias;
	}

	public ArrayList<Integer> getDiasOcurridosOptimos() {

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

	public BigDecimal getMaximoPerimetro() {
		return maximoPerimetro;
	}

	public int getMaximoDia() {
		return maximoDia;
	}

	public void setMaximoDia(int maximoDia) {
		this.maximoDia = maximoDia;
	}

	public void setMaximoPerimetro(BigDecimal maximoPerimetro) {
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

	// Algoritmo Para Calcular La Alineacion Total En El
	// Sistema------------------------------------------------------

	public void sucesoPeriodoDeSequia(int dia) {

		Planeta unPlaneta = this.planetas.get(2);

		BigDecimal pendiente = calcularPendiente(unPlaneta);
		BigDecimal ordenadaAlOrigen = calcularOrdenada(pendiente, unPlaneta);

		// Filtro La Lista De Planetas Para Que Al Comparar Con La Recta No Calcule El
		// Del Planeta Que Utilice
		List<Planeta> aux = this.planetas.stream().filter(planeta -> planeta.getClass() != unPlaneta.getClass())
				.collect(Collectors.toList());

		// Recorro La Lista De Planetas Y Si Todos Pertenecen A La Recta Entonces
		// Deberia Suceder La Sequia
		// Si Uno No Cumple La Condicion El Resto No Lo Comprueba
		if (aux.stream().allMatch(planeta -> compararPuntoConRecta(pendiente, ordenadaAlOrigen, planeta, dia))) {
			getDiasOcurridosSequias().add(dia);
			this.aumentarContadorSequias();
		}
	}

	// Esta Funcion Lo Que Hace Es Calcular La Diferencia Entre 2 Puntos Y Obtener
	// Su DeltaX y DeltaY
	// Al Dividirlos Me Debe Dar La Pendiente m De La Ecuacion -> y = m.x + b
	// El getCentro() Es De La Posicion Del Sol En (0,0)

	private BigDecimal calcularPendiente(Planeta unPlaneta) {

		BigDecimal deltaX = (new BigDecimal(unPlaneta.getMovimientoEnX() - getCentroX())).setScale(4, RM);

		BigDecimal deltaY = (new BigDecimal(unPlaneta.getMovimientoEnY() - getCentroY())).setScale(4, RM);

		BigDecimal total = deltaY.divide(deltaX, 4, RM);

		return total;

	}

	// Una Vez Obtenido La Pendiente -> m Sacada Entre Un Planeta Y El Sol,
	// Calculo Cual Es La Ordenada Al Origen -> b
	// Resto La Posicion Y Del Planeta Con La Pendiente Calculada Por La Posicion X
	// Del Planeta
	// Quedando La Ecuacion b = y - m.x

	private BigDecimal calcularOrdenada(BigDecimal pendiente, Planeta unPlaneta) {

		BigDecimal centroCero = new BigDecimal(0);
		BigDecimal exponente = new BigDecimal(1e-5);

		BigDecimal ecuacionDerecha = pendiente.multiply(new BigDecimal(unPlaneta.getMovimientoEnX()).setScale(4, RM));
		BigDecimal ecuacionIzquierda = new BigDecimal(unPlaneta.getMovimientoEnY());

		BigDecimal ordenada = ecuacionIzquierda.subtract(ecuacionDerecha).setScale(4, RM);
		BigDecimal resultado = ordenada.abs();
		if (resultado.compareTo(exponente) > -1)
			return centroCero;
		return ordenada;
	}

	// Esta Funcion Al Sacar La Pendiente Y La Ordenada Entre 1 Planeta Y El Sol En
	// (0,0)
	// Le Paso Los Demas Planetas Sus Puntos Y Reemplazo En La Ecuacion y = m.x + b
	// Los Valores
	// Y Si El Punto X == Y Quiere Decir Que El P(x,y) Pertenece A La Recta

	private boolean compararPuntoConRecta(BigDecimal pendiente, BigDecimal ordenadaAlOrigen, Planeta unPlaneta, int dia) {

		BigDecimal primerCalculo = pendiente.multiply(new BigDecimal(unPlaneta.getMovimientoEnX())).setScale(4, RM);

		BigDecimal calculoX = primerCalculo.add(ordenadaAlOrigen).setScale(4, RM);
		BigDecimal calculoY = (new BigDecimal(unPlaneta.getMovimientoEnY())).setScale(4, RM);

		BigDecimal resultado;

		if (((calculoX.compareTo(valorCero) == 1) && (calculoY.compareTo(valorCero) == 1))
				|| ((calculoX.compareTo(valorCero) == -1) && (calculoY.compareTo(valorCero) == -1))) {

			BigDecimal absCalculoX = calculoX.abs();
			BigDecimal absCalculoY = calculoY.abs();
			
			if (absCalculoX.compareTo(absCalculoY) == 1) {
				resultado = absCalculoX.subtract(absCalculoY).setScale(4, RM);
			} else {
				resultado = absCalculoY.subtract(absCalculoX).setScale(4, RM);
			}
//			if(unPlaneta.getClass() == Ferengi.class) { System.out.println("Dia: " + dia +" - Ferengi = " + resultado); }
//			else { System.out.println("Dia: " + dia + " - Betasoide = " + resultado); }

			return (resultado.compareTo(valorCero) == 1) && (resultado.compareTo(limiteSuperiorSequia) == -1);
		}

		return (calculoY.compareTo(calculoX) == 0);
	}

	// Algoritmo Para Calcular La Triangulacion Teniendo Dentro El
	// Sol------------------------------------------------------
	public void sucesoPeriodoDeLluvia(int diaSuceso) {

		// Esta Lista Es Para Los Valores De Segmento Entre Planetas Redondeado Ya Su
		// Lado
		ArrayList<BigDecimal> valoresOrientados = new ArrayList<>();

		Ferengi planetaFerengi = (Ferengi) planetas.get(0);
		Betasoide planetaBetasoide = (Betasoide) planetas.get(1);
		Vulcano planetaVulcano = (Vulcano) planetas.get(2);

		// Calculo La Orientacion De ABC
		BigDecimal orientacionTriangulo = calculoOrientacion(planetaFerengi, planetaBetasoide, planetaVulcano);

		// Calculo La Orientacion De ABP, BCP, CBP
		BigDecimal orientacionTrianguloPunto1 = calculoOrientacion(planetaFerengi, planetaBetasoide);
		BigDecimal orientacionTrianguloPunto2 = calculoOrientacion(planetaBetasoide, planetaVulcano);
		BigDecimal orientacionTrianguloPunto3 = calculoOrientacion(planetaVulcano, planetaFerengi);

		// Los Agrego A La Lista
		valoresOrientados.add(orientacionTrianguloPunto1);
		valoresOrientados.add(orientacionTrianguloPunto2);
		valoresOrientados.add(orientacionTrianguloPunto3);

		// Recorro La Lista De Planetas Y Si Todos Tienen La Misma Orientacion Que ABC
		// Sucede El De Lluvia
		// Si Uno No Cumple La Condicion El Resto No Lo Comprueba Y Significa Que El
		// Punto Esta Fuera Del Triangulo

		if (valoresOrientados.stream().allMatch(valor -> tienenLaMismaOrientacion(orientacionTriangulo, valor))) {
			this.aumentarContadorLluvias();
			BigDecimal perimetroTriangulo = calcularPerimetroDelTriangulo(planetaFerengi, planetaBetasoide,
					planetaVulcano);

			if (perimetroTriangulo.compareTo(this.getMaximoPerimetro()) == 1
			 || perimetroTriangulo.compareTo(this.getMaximoPerimetro()) == 0) {
				//System.out.println("Dia: " + diaSuceso + " Nuevo Perimetro Maximo = " + perimetroTriangulo);
				this.setMaximoPerimetro(perimetroTriangulo);
				this.setMaximoDia(diaSuceso);
			}
		}
	}
	// Obtengo La Distancia Entre Los Puntos AB, AC, BC Siendo Este Cada Lado Del
	// Triangulo
	// Sumo Sus Lados Para Obtener El Perimetro

	private BigDecimal calcularPerimetroDelTriangulo(Planeta planetaA, Planeta planetaB, Planeta planetaC) {

		BigDecimal total = new BigDecimal(0);

		BigDecimal primerLado = new BigDecimal(Point2D.distance(planetaA.getMovimientoEnX(),
				planetaA.getMovimientoEnY(), planetaB.getMovimientoEnX(), planetaB.getMovimientoEnY()));
		total = total.add(primerLado).setScale(4, RM);

		BigDecimal segundoLado = new BigDecimal(Point2D.distance(planetaA.getMovimientoEnX(),
				planetaA.getMovimientoEnY(), planetaC.getMovimientoEnX(), planetaC.getMovimientoEnY()));
		total = total.add(segundoLado).setScale(4, RM);

		BigDecimal tercerLado = new BigDecimal(Point2D.distance(planetaB.getMovimientoEnX(),
				planetaB.getMovimientoEnY(), planetaC.getMovimientoEnX(), planetaC.getMovimientoEnY()));
		return total.add(tercerLado).setScale(4, RM);

	}

	// Funcion Para Comprobar Si El Punto Respecto Al Triangulo Tienen La Misma
	// Orientacion
	private boolean tienenLaMismaOrientacion(BigDecimal primeraOrientacion, BigDecimal segundaOrientacion) {

		BigDecimal comparoCero = new BigDecimal(0);

		return ((primeraOrientacion.compareTo(comparoCero) == 1) && (segundaOrientacion.compareTo(comparoCero) == 1))
				|| ((primeraOrientacion.compareTo(comparoCero) == 1) && (segundaOrientacion.compareTo(comparoCero) == 1));
	}

	// Obtengo La Orientacion Del Triangulo ABC
	// Con La Siguiente Formula (A1.x - A3.x) * (A2.y - A3.y) - (A1.y - A3.y) *
	// (A2.x - A3.x)

	private BigDecimal calculoOrientacion(Planeta planetaA, Planeta planetaB, Planeta planetaC) {

		BigDecimal primero = (new BigDecimal(planetaA.getMovimientoEnX()))
				.subtract(new BigDecimal(planetaC.getMovimientoEnX())).setScale(4, RM);
		BigDecimal segundo = (new BigDecimal(planetaB.getMovimientoEnY()))
				.subtract(new BigDecimal(planetaC.getMovimientoEnY())).setScale(4, RM);
		BigDecimal tercero = (new BigDecimal(planetaA.getMovimientoEnY()))
				.subtract(new BigDecimal(planetaC.getMovimientoEnY())).setScale(4, RM);
		BigDecimal cuarto = (new BigDecimal(planetaB.getMovimientoEnX()))
				.subtract(new BigDecimal(planetaC.getMovimientoEnX())).setScale(4, RM);

		BigDecimal primeraCuenta = primero.multiply(segundo).setScale(4, RM);
		BigDecimal segundaCuenta = tercero.multiply(cuarto).setScale(4, RM);

		return primeraCuenta.subtract(segundaCuenta).setScale(4, RM);
	}

	// Obtengo La Orientacion Que Forma El Punto P Con Los Vertices Del Triangulo
	// ABC.
	// El Punto P Para Esta Situacion Es El Sol En (0,0)
	// Calculo Para Obtener Las Orientaciones De ABP, BCP, CBP

	private BigDecimal calculoOrientacion(Planeta planetaA, Planeta planetaB) {

		BigDecimal primero = (new BigDecimal(planetaA.getMovimientoEnX())).subtract(new BigDecimal(this.getCentroX()))
				.setScale(4, RM);
		BigDecimal segundo = (new BigDecimal(planetaB.getMovimientoEnY())).subtract(new BigDecimal(this.getCentroY()))
				.setScale(4, RM);
		BigDecimal tercero = (new BigDecimal(planetaA.getMovimientoEnY())).subtract(new BigDecimal(this.getCentroY()))
				.setScale(4, RM);
		BigDecimal cuarto = (new BigDecimal(planetaB.getMovimientoEnX())).subtract(new BigDecimal(this.getCentroX()))
				.setScale(4, RM);

		BigDecimal primeraCuenta = primero.multiply(segundo).setScale(4, RM);
		BigDecimal segundaCuenta = tercero.multiply(cuarto).setScale(4, RM);

		return primeraCuenta.subtract(segundaCuenta).setScale(4, RM);
	}

	// Algoritmo Para Calcular La Alineacion Solo Entre
	// Planetas------------------------------------------------------

	public void sucesoPeriodoOptimo(int dia) {

		Ferengi planetaFerengi = (Ferengi) planetas.get(0);
		Betasoide planetaBetasoide = (Betasoide) planetas.get(1);
		Vulcano planetaVulcano = (Vulcano) planetas.get(2);

		BigDecimal puntoX = calcularSegmentoEntreDosPlanetasEnX(planetaFerengi, planetaVulcano);
		BigDecimal puntoY = calcularSegmentoEntreDosPlanetasEnY(planetaFerengi, planetaVulcano);

		boolean primeraCondicion = compararPuntosConLaRecta(planetaFerengi, planetaBetasoide, puntoX, puntoY, dia);
		boolean segundaCondicion = compararPuntosConLaRecta(planetaFerengi, puntoX, puntoY, dia);

		// Si El Tercer Planeta (Ya Sacado El Segmento Con Los Dos Anteriores), Cumple
		// Con La Condicion
		// Y El Sol No, Quiere Decir Que El Planeta Esta Dentro De La Recta Y El Sol No
		// Entonces Deberia Suceder La Condicion Optima De Presion Y Temperatura

		if (primeraCondicion && segundaCondicion) {
			this.aumentarContadorOptimo();
			getDiasOcurridosOptimos().add(dia);
		}
	}

	// Calculo El Segmento BA Entre Dos Planetas Para Obtener La Recta

	public BigDecimal calcularSegmentoEntreDosPlanetasEnX(Planeta planetaA, Planeta planetaB) {

		BigDecimal planBX = (new BigDecimal(planetaB.getMovimientoEnX())).setScale(4, RM);
		BigDecimal planAX = (new BigDecimal(planetaA.getMovimientoEnX()).setScale(4, RM));
		
		return planBX.subtract(planAX).setScale(4, RM) ;
	}

	public BigDecimal calcularSegmentoEntreDosPlanetasEnY(Planeta planetaA, Planeta planetaB) {

		BigDecimal planBY = (new BigDecimal(planetaB.getMovimientoEnY())).setScale(4, RM);
		BigDecimal planAY = (new BigDecimal(planetaA.getMovimientoEnY()).setScale(4, RM));
		
		return planBY.subtract(planAY).setScale(4, RM);
	}

	// Entre Los Planetas A y B Y El Segmento puntoXY Calculado Anteriormente,
	// Obtengo El Calculo De La Ecuacion De La Siguiente Forma
	// (B.x - A.x) / x == (B.y - A.y) / y

	public boolean compararPuntosConLaRecta(Planeta planetaA, Planeta planetaB, BigDecimal puntoX, BigDecimal puntoY, int dia) {


		BigDecimal resultado;
		
		BigDecimal planAX = (new BigDecimal(planetaA.getMovimientoEnX())).setScale(4, RM);
		BigDecimal planBX = (new BigDecimal(planetaB.getMovimientoEnX())).setScale(4, RM);
		BigDecimal primeroX = planBX.subtract(planAX).setScale(4, RM);		
		BigDecimal puntoEnX = primeroX.divide(puntoX, 4, RM);
		
		BigDecimal planAY = (new BigDecimal(planetaA.getMovimientoEnY())).setScale(4, RM);
		BigDecimal planBY = (new BigDecimal(planetaB.getMovimientoEnY())).setScale(4, RM);
		BigDecimal primeroY = planBY.subtract(planAY).setScale(4, RM);	
		BigDecimal puntoEnY = primeroY.divide(puntoY, 4, RM);	

		if ((puntoEnX.compareTo(valorCero) == 1) && (puntoEnY.compareTo(valorCero) == 1) || 
			(puntoEnX.compareTo(valorCero) == -1) && (puntoEnY.compareTo(valorCero) == -1)) {
		
			BigDecimal valorX = puntoEnX.abs();
			BigDecimal valorY = puntoEnY.abs();

			if (valorX.compareTo(valorY) == 1) {
				resultado = valorX.subtract(valorY).setScale(4, RM);
			} else {
				resultado = valorY.subtract(valorX).setScale(4, RM);
			}
			//System.out.println("Dia: " + dia + " Planeta = " + resultado);
			return (resultado.compareTo(valorCero) == 1) && (resultado.compareTo(limiteSuperiorOptimo) == -1);
		}
		return puntoEnX.compareTo(puntoEnY) == 0;
	}

	// Entre El Planeta A, El Sol Y El Segmento puntoXY Calculado Anteriormente,
	// Obtengo El Calculo De La Ecuacion De La Siguiente Forma
	// (0 - A.x) / x == (0 - A.y) / y
	// Si Los Resultados Son Distintos Quiere Decir Que El Sol No Se Encuentra En La
	// Recta

	public boolean compararPuntosConLaRecta(Planeta planetaA, BigDecimal puntoX, BigDecimal puntoY, int dia) {

		BigDecimal resultado;
		
		BigDecimal planAX = (new BigDecimal(-planetaA.getMovimientoEnX())).setScale(4, RM);	
		BigDecimal planAY = (new BigDecimal(-planetaA.getMovimientoEnY())).setScale(4, RM);
		
		BigDecimal puntoEnX = planAX.divide(puntoX, 4, RM);
		BigDecimal puntoEnY = planAY.divide(puntoY, 4, RM);	

		if ((puntoEnX.compareTo(valorCero) == 1) && (puntoEnY.compareTo(valorCero) == 1) || 
			(puntoEnX.compareTo(valorCero) == -1) && (puntoEnY.compareTo(valorCero) == -1)) {
			
			BigDecimal valorX = puntoEnX.abs();
			BigDecimal valorY = puntoEnY.abs();

			if (valorX.compareTo(valorY) == 1) {
				resultado = valorX.subtract(valorY).setScale(4, RM);
			} else {
				resultado = valorY.subtract(valorX).setScale(4, RM);
			}
			//System.out.println("Dia: " + dia +" Sol = " + resultado);
			return (resultado.compareTo(valorCero) == 1) && (resultado.compareTo(limiteSuperiorOptimo) == -1);
		}
		return !(puntoEnX.compareTo(puntoEnY) == 0);
	}
}